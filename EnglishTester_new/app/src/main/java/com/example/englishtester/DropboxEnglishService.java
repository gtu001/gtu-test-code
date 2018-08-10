package com.example.englishtester;

import android.content.Context;
import android.os.Handler;
import com.example.englishtester.common.Log;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.common.DropboxUtilV2;
import com.example.englishtester.common.FileConstantAccessUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;

public class DropboxEnglishService {

    private static final String TAG = DropboxEnglishService.class.getSimpleName();
    private final static String NEW_ENGLISH_WORD_FILE = "/english_prop/new_word.txt";
    private final static String NEW_ENGLISH_PROP_FILE = "/english_prop/new_word_%s.properties";

    Context context;
    EnglishwordInfoDAO englishwordInfoDAO;
    EnglishTester_Diectory diectory = new EnglishTester_Diectory();
    String accessToken;

    File tempEmptyFile;

    Handler handler = new Handler();

    public DropboxEnglishService(Context context, String accessToken) {
        this.context = context;
        englishwordInfoDAO = new EnglishwordInfoDAO(context);
        this.accessToken = accessToken;
    }

    /**
     * 主邏輯
     */
    public void executeAddDropboxWord(final boolean isDeleteSearchWordFile) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File tmpFile = downloadTempEnglish(getClient());

                    if (tmpFile == null || !tmpFile.exists()) {
                        toastShow("dropbox檔案不存在");
                        return;
                    }
                    DbxClientV2 client = getClient();

                    List<String> wordList = getReadList(tmpFile);

                    int count = addToProperties(wordList);

                    uploadSearchFileToCloud(client);

                    removeEnglishSearchFile(client);

                    resetEnglishSearchFile(client);

                    String msg = "dropbox單字數 : " + wordList.size() + "/ 成功新增新字 :" + count;

                    //是否刪除newSearchWord.properties
                    if (isDeleteSearchWordFile) {
                        deleteSearchwordFile();
                        msg += ", 成功後刪除";
                    }

                    Log.v(TAG, msg);
                    toastShow(msg);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    toastShow("失敗新增新字!");
                }
            }
        });
        thread.start();
    }

    private void toastShow(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int addToProperties(List<String> wordList) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(FileConstantAccessUtil.getInputStream(context, Constant.SEARCHWORD_FILE, true));

        int count = 0;
        for (String word : wordList) {
            if (prop.containsKey(word)) {
                continue;
            }

            EnglishWord wordInd = englishwordInfoDAO.queryOneWord(word);
            String desc = "";
            if (wordInd != null) {
                desc = wordInd.englishDesc;
            }
            if (StringUtils.isBlank(desc)) {
                WordInfo wordInd2 = diectory.parseToWordInfo(word, context, null);
                if (wordInd2 != null) {
                    desc = wordInd2.getMeaning();
                }
            }

            prop.setProperty(word, StringUtils.trimToEmpty(desc));
            count++;
        }
        String comment = DateFormatUtils.format(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss") + " add " + wordList.size() + ", actual : " + count;
        prop.store(FileConstantAccessUtil.getOutputStream(context, Constant.SEARCHWORD_FILE), comment);
        return count;
    }

    /**
     * 讀檔案單字
     */
    private List<String> getReadList(File file) throws IOException {
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for (String line = null; (line = reader.readLine()) != null; ) {
                String word = StringUtils.trimToEmpty(line).toLowerCase();
                if (StringUtils.isNotBlank(word)) {
                    list.add(word);
                }
            }
        } finally {
            reader.close();
        }
        return list;
    }

    private FileInputStream getFileInputStream(File file) throws FileNotFoundException {
        boolean result = file.getParentFile().mkdirs();
        if (file.exists() && file.getParentFile().exists()) {
            return new FileInputStream(file);
        } else {
            return context.openFileInput(file.getName());
        }
    }

    private long getFileSize(File file) {
        if (file.exists()) {
            return file.length();
        } else {
            File anotherFile = new File(context.getFilesDir(), file.getName());
            if (anotherFile.exists()) {
                return anotherFile.length();
            } else {
                throw new RuntimeException("檔案不存在 : " + anotherFile.getAbsolutePath());
            }
        }
    }

    /**
     * 上傳英文單字檔自雲端
     */
    private void uploadSearchFileToCloud(final DbxClientV2 client) throws DbxException, IOException {
        FileInputStream inputStream = FileConstantAccessUtil.getInputStream(context, Constant.SEARCHWORD_FILE, true);
        try {
            String name = String.format(NEW_ENGLISH_PROP_FILE, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss"));

            boolean result = DropboxUtilV2.upload(name, inputStream, client);

//            DbxEntry.File uploadedFile = client.uploadFile(name, DbxWriteMode.add(), getFileSize(Constant.SEARCHWORD_FILE), inputStream);
//            Log.v(TAG, "Uploaded: " + uploadedFile.toString());
            toastShow("檔案已上傳 : " + name);
        } finally {
            inputStream.close();
        }
    }

    /**
     * 刪除newSearchWord.properties
     */
    public boolean deleteSearchwordFile() {
        if (Constant.SEARCHWORD_FILE.exists()) {
            return Constant.SEARCHWORD_FILE.delete();
        } else {
            File anotherFile = new File(context.getFilesDir(), Constant.SEARCHWORD_FILE.getName());
            if (anotherFile.exists()) {
                return anotherFile.delete();
            }
        }
        return false;
    }

    /**
     * 下載dropbox檔案
     */
    private File downloadTempEnglish(final DbxClientV2 client) throws DbxException, IOException, InterruptedException, ExecutionException {
        File tmpFile = File.createTempFile("temp_new_word_", ".txt");
        final FileOutputStream outputStream = new FileOutputStream(tmpFile);
        try {
//            DbxEntry.File downloadedFile = client.getFile(NEW_ENGLISH_WORD_FILE, null, outputStream);
            boolean result = DropboxUtilV2.download(NEW_ENGLISH_WORD_FILE, outputStream, client);
//            if (downloadedFile != null) {
            if (result) {
//                Log.v(TAG, "Metadata: " + downloadedFile.toString());
            } else {
                resetEnglishSearchFile(client);
            }
        } finally {
            outputStream.close();
        }
        return tmpFile;
    }

    public static <T> T getRunOnUiThread(Callable<T> task, long timeout) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Future<T> future = executor.submit(task);
            System.out.println("future done? " + future.isDone());
            T result = null;
            if (timeout == -1) {
                result = future.get();
            } else {
                result = future.get(timeout, TimeUnit.MILLISECONDS);
            }
            Log.v(TAG, "future done? " + future.isDone());
            Log.v(TAG, "result: " + result);
            return result;
        } catch (Exception ex) {
            Log.e(TAG, "getRunInThread ERROR", ex);
            return null;
        }
    }

    private DbxClientV2 getClient() {
        return DropboxUtilV2.getClient(accessToken);
    }

    /**
     * 刪除暫存檔
     */
    private void removeEnglishSearchFile(final DbxClientV2 client) throws DbxException {
//        client.delete(NEW_ENGLISH_WORD_FILE);
        DropboxUtilV2.delete(NEW_ENGLISH_WORD_FILE, client);
    }

    /**
     * 重設此檔案
     */
    private void resetEnglishSearchFile(final DbxClientV2 client) throws DbxException, IOException, InterruptedException, ExecutionException {
        if (tempEmptyFile == null || !tempEmptyFile.exists()) {
            tempEmptyFile = File.createTempFile("NEW_ENGLISH_WORD_FILE", ".txt");
        }
        final FileInputStream inputStream = new FileInputStream(tempEmptyFile);
        try {
//            DbxEntry.File uploadedFile = client.uploadFile(NEW_ENGLISH_WORD_FILE, DbxWriteMode.add(), tempEmptyFile.length(), inputStream);
//            Log.v(TAG, "Uploaded: " + uploadedFile.toString());
            DropboxUtilV2.upload(NEW_ENGLISH_WORD_FILE, inputStream, client);
        } finally {
            inputStream.close();
        }
    }

    /**
     * 判斷accessToken是否有效
     */
    public static boolean isAccessTokenWork(final String token) {
        return DropboxEnglishService.getRunOnUiThread(new Callable<Boolean>() {
            public Boolean call() {
                //檢查token是否有作用
                return DropboxUtilV2.isAccessTokenOk(token);
            }
        }, -1L);
    }
}
