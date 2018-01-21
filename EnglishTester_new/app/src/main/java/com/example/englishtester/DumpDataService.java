package com.example.englishtester;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import com.example.englishtester.common.FileConstantAccessUtil;
import com.example.englishtester.common.FileUtilAndroid;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by gtu001 on 2017/5/20.
 */
public class DumpDataService {

    private static final String TAG = DumpDataService.class.getSimpleName();

    Context context;
    final EnglishwordInfoDAO dao;
    XStream xstream = new XStream(new DomDriver());
    final Handler handler = new Handler();

    public DumpDataService(Context context) {
        this.context = context;
        this.dao = new EnglishwordInfoDAO(context);
    }

    /**
     * 匯出系統單字
     */
    public void exportResult() {
        final ProgressDialog myDialog = ProgressDialog.show(context, "系統狀態", "啟動系統備份!", true);
        new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();

                boolean resultOk = true;
                try {
                    exportResult_JAVA();
                    if (BuildConfig.DEBUG) {
                        exportResult_JSON();
                        exportResult_XML();
                    }
                } catch (Exception ex) {
                    resultOk = false;
                }

                long during = System.currentTimeMillis() - startTime;

                if (resultOk) {
                    showDialogMessage("完成", "匯出完成, 耗時 : " + during);
                } else {
                    showDialogMessage("失敗", "匯出失敗, 耗時 : " + during);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.cancel();
                    }
                });
            }
        }, "autoSysBackup").start();
    }

    /**
     * 匯入系統單字
     */
    public void importResult() {
        final ProgressDialog myDialog = ProgressDialog.show(context, "系統狀態", "匯入字庫!", true);
        new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();

                boolean resultOk = true;
                try {
                    importResult_JAVA();
//                    importResult_JSON();
//                    importResult_XML();
                } catch (Exception ex) {
                    resultOk = false;
                }

                long during = System.currentTimeMillis() - startTime;

                if (resultOk) {
                    showDialogMessage("完成", "匯完成, 耗時 : " + during);
                } else {
                    showDialogMessage("失敗", "匯入失敗, 耗時 : " + during);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.cancel();
                    }
                });
            }
        }, "autoSysBackup").start();
    }

    /**
     * 回覆系統單字
     */
    public void autoRestoreBackup(final Context context) {
        int sizeOfWords = dao.sizeOfWords();
        if (sizeOfWords == 0) {
            //第一次安裝
            final ProgressDialog myDialog = ProgressDialog.show(context, "系統狀態", "安裝單字中..", true);
            new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();

                    boolean resultOk = true;
                    try {
                        //判斷匯入來源
                        InputStream inputStream = null;
                        if(FileConstantAccessUtil.getFile(context, Constant.EXPORT_FILE_JAVA).exists() &&
                                FileConstantAccessUtil.getFile(context, Constant.EXPORT_FILE_JAVA).length() != 0){
                            inputStream = FileConstantAccessUtil.getInputStream(context, Constant.EXPORT_FILE_JAVA, false);
                            Log.v(TAG, "使用 custom 備份還原!");

                            //匯入資料
                            _importResult_JAVA(inputStream, false);
                        }else{
                            inputStream = context.getAssets().open("exportFileJava.bin");
                            Log.v(TAG, "使用 APK 備份還原!");

                            //匯入資料
                            _importResult_JAVA(inputStream, true);

                            //產生備份檔
                            FileOutputStream fos = FileConstantAccessUtil.getOutputStream(context, Constant.EXPORT_FILE_JAVA);
                            FileUtilAndroid.copyFile(inputStream, fos);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "autoRestoreBackup : " + e.getMessage(), e);
                        resultOk = false;
                    }

                    long during = System.currentTimeMillis() - startTime;

                    if (resultOk) {
                        showDialogMessage("完成", "安裝完成, 耗時 : " + during);
                    } else {
                        showDialogMessage("失敗", "安裝失敗, 耗時 : " + during);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            myDialog.cancel();
                            myDialog.dismiss();
                        }
                    });
                }
            }, "autoSysBackup").start();
        } else {
            //每月固定備份
            File file = FileConstantAccessUtil.getFile(context, Constant.EXPORT_FILE);
            boolean executeBackUp = false;
            if (!file.exists() || (file.exists() && file.length() == 0)) {
                executeBackUp = true;
            } else if (file.exists()) {
                //每月一號備份
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(file.lastModified());
                int backupMonth = cal.get(Calendar.MONTH) + 1;
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                Log.v(TAG, "compare --> " + backupMonth + ", " + currentMonth);
                if (backupMonth != currentMonth) {
                    executeBackUp = true;
                }
            }
            if (executeBackUp) {
                Log.v(TAG, "匯出exportResult.bin檔!");
                exportResult();
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------------

    public void exportResult_JAVA() {
        Log.v(TAG, "exportResult");
        try {
            List<EnglishwordInfoDAO.EnglishWord> allList = dao.queryAll(true);
            FileOutputStream fos = FileConstantAccessUtil.getOutputStream(context, Constant.EXPORT_FILE_JAVA);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            for (EnglishwordInfoDAO.EnglishWord word : allList) {
                out.writeObject(word);
            }
            out.flush();
            out.close();
            Log.v(TAG, "匯出完成 size = " + allList.size());
            Log.v(TAG, "匯出完成 done...");
        } catch (Exception ex) {
            Log.v(TAG, ex.getMessage());
            Log.e(TAG, "ERROR : " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    private void _importResult_JAVA(InputStream inputStream, boolean doImportAsNewWord) {
        Log.v(TAG, "importResult");
        try {
            ObjectInputStream input = new ObjectInputStream(inputStream);
            List<EnglishwordInfoDAO.EnglishWord> list = new ArrayList<EnglishwordInfoDAO.EnglishWord>();

            int count = 0;
            try {
                for (Object readObj = null; (readObj = input.readObject()) != null; ) {
                    EnglishwordInfoDAO.EnglishWord word = (EnglishwordInfoDAO.EnglishWord) readObj;
                    list.add(word);
                    count++;
                    Log.v(TAG, "-----" + count);
                }
                input.close();
            } catch (java.io.EOFException ex) {
                Log.v(TAG, "讀取完成");
            }

            //匯入
            dao.importData(list, doImportAsNewWord);
            Log.v(TAG, "done...");
        } catch (Exception ex) {
            Log.e(TAG, "匯入單字錯誤 : " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
        }
    }

    private void importResult_JAVA() {
        Log.v(TAG, "importResult");
        try {
            File file = Constant.EXPORT_FILE_JAVA;
            FileInputStream fis = FileConstantAccessUtil.getInputStream(context, file, false);
            ObjectInputStream input = new ObjectInputStream(fis);
            List<EnglishwordInfoDAO.EnglishWord> list = new ArrayList<EnglishwordInfoDAO.EnglishWord>();

            int count = 0;
            try {
                for (Object readObj = null; (readObj = input.readObject()) != null; ) {
                    EnglishwordInfoDAO.EnglishWord word = (EnglishwordInfoDAO.EnglishWord) readObj;
                    list.add(word);
                    count++;
                    Log.v(TAG, "-----" + count);
                }
                input.close();
            } catch (java.io.EOFException ex) {
                Log.v(TAG, "讀取完成");
            }

            //匯入
            dao.importData(list, false);
            Log.v(TAG, "done...");
        } catch (Exception ex) {
            Log.e(TAG, "匯入單字錯誤 : " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
        }
    }

    private void exportResult_XML() {
        Log.v(TAG, "exportResult");
        try {
            List<EnglishwordInfoDAO.EnglishWord> allList = dao.queryAll(true);
            File file = Constant.EXPORT_FILE_XML;
            FileOutputStream fos = FileConstantAccessUtil.getOutputStream(context, file);
            ObjectOutputStream out = xstream.createObjectOutputStream(fos);
            for (EnglishwordInfoDAO.EnglishWord word : allList) {
                out.writeObject(word);
            }
            out.flush();
            out.close();
            Log.v(TAG, "匯出完成 size = " + allList.size());
            Log.v(TAG, "匯出完成 done...");
        } catch (Exception ex) {
            Log.v(TAG, ex.getMessage());
            Log.e(TAG, "ERROR : " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    private void importResult_XML() {
        Log.v(TAG, "importResult");

        try {
            File file = Constant.EXPORT_FILE_XML;
            FileInputStream fis = FileConstantAccessUtil.getInputStream(context, file, false);

            List<EnglishwordInfoDAO.EnglishWord> list = new ArrayList<EnglishwordInfoDAO.EnglishWord>();
            try {
                int count = 0;
                ObjectInputStream input = xstream.createObjectInputStream(fis);
                for (Object readObj = null; (readObj = input.readObject()) != null; ) {
                    EnglishwordInfoDAO.EnglishWord word = (EnglishwordInfoDAO.EnglishWord) readObj;
                    list.add(word);
                    count++;
                    Log.v(TAG, "-----" + count);
                }
                input.close();
            } catch (java.io.EOFException ex) {
                Log.v(TAG, "讀取完成");
            }

            //匯入資料庫
            dao.importData(list, false);

            Log.v(TAG, "done...");
        } catch (Exception ex) {
            Log.e(TAG, "匯入單字錯誤 : " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
        }
    }

    private void exportResult_JSON() {
        Log.v(TAG, "exportResult");
        try {
            List<EnglishwordInfoDAO.EnglishWord> allList = dao.queryAll(true);
            File file = Constant.EXPORT_FILE_JSON;
            FileOutputStream fos = FileConstantAccessUtil.getOutputStream(context, file);

            JsonWriteForWord util = new JsonWriteForWord();
            util.writeJsonStream(fos, allList);

            Log.v(TAG, "匯出完成 size = " + allList.size());
            Log.v(TAG, "匯出完成 done...");
        } catch (Exception ex) {
            Log.e(TAG, "ERROR : " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    private void importResult_JSON() {
        Log.v(TAG, "importResult");
        try {
            File file = Constant.EXPORT_FILE_JSON;
            FileInputStream fis = FileConstantAccessUtil.getInputStream(context, file, false);

            JsonReaderForWord util = new JsonReaderForWord();
            List<EnglishwordInfoDAO.EnglishWord> list = util.readJsonStream(fis);
            dao.importData(list, false);

            Log.v(TAG, "done...");
        } catch (java.io.EOFException ex) {
            Log.v(TAG, "匯入完成");
        } catch (Exception ex) {
            Log.e(TAG, "匯入單字錯誤 : " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
        }
    }

    private void toastShow(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogMessage(final String title, final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)//
                        .setTitle(title).setMessage(message).show();
            }
        });
    }

    /*
    * 讀取備份檔成Json
     */
    private class JsonReaderForWord {
        private int readCount = 0;

        public List<EnglishwordInfoDAO.EnglishWord> readJsonStream(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                return readInfoArray(reader);
            } finally {
                reader.close();
            }
        }

        public List<EnglishwordInfoDAO.EnglishWord> readInfoArray(JsonReader reader) throws IOException {
            List<EnglishwordInfoDAO.EnglishWord> wordList = new ArrayList<EnglishwordInfoDAO.EnglishWord>();
            reader.beginArray();
            while (reader.hasNext()) {
                wordList.add(readWord(reader));
            }
            reader.endArray();
            return wordList;
        }

        public EnglishwordInfoDAO.EnglishWord readWord(JsonReader reader) throws IOException {
            EnglishwordInfoDAO.EnglishWord word = new EnglishwordInfoDAO.EnglishWord();
            Class<?> wordClz = EnglishwordInfoDAO.EnglishWord.class;
            reader.beginObject();
            while (reader.hasNext()) {
                try {
                    String name = reader.nextName();
                    Field f = wordClz.getDeclaredField(name);

                    Object val = null;
                    boolean setValueOk = true;
                    if (reader.peek() != JsonToken.NULL) {
                        if (f.getType() == String.class) {
                            val = reader.nextString();
                        } else if (f.getType() == int.class) {
                            val = reader.nextInt();
                        } else if (f.getType() == long.class) {
                            val = reader.nextLong();
                        } else {
                            throw new RuntimeException("不可預期的欄位 : " + f.getName() + " - " + f.getType());
                        }
                    } else {
                        reader.skipValue();
                        setValueOk = false;
                    }
                    if (setValueOk) {
                        f.setAccessible(true);
                        f.set(word, val);
                    }
                } catch (Throwable ex) {
                    //Log.e(TAG, "jsonReader EnglishWord error ! : " + word, ex);
                    throw new RuntimeException("jsonReader EnglishWord error ! : " + word, ex);
                }
            }
            reader.endObject();

            readCount++;
            Log.v(TAG, "read " + readCount + " : " + ReflectionToStringBuilder.toString(word));
            return word;
        }
    }

    /*
    * 寫檔工具 Json EnglishWord
     */
    private class JsonWriteForWord {
        private int writeCount = 0;

        public void writeJsonStream(OutputStream out, List<EnglishwordInfoDAO.EnglishWord> messages) throws IOException {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.setIndent("  ");
            writeInfoArray(writer, messages);
            writer.close();
        }

        public void writeInfoArray(JsonWriter writer, List<EnglishwordInfoDAO.EnglishWord> wordList) throws IOException {
            writer.beginArray();
            for (EnglishwordInfoDAO.EnglishWord message : wordList) {
                writeWord(writer, message);
            }
            writer.endArray();
        }

        public void writeWord(JsonWriter writer, EnglishwordInfoDAO.EnglishWord word) throws IOException {
            writer.beginObject();
            Log.v(TAG, "write : " + ReflectionToStringBuilder.toString(word));
            Class<?> wordClz = EnglishwordInfoDAO.EnglishWord.class;
            for (Field f : wordClz.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    Object val = f.get(word);
                    if (f.getType() == String.class) {
                        writer.name(f.getName()).value((String) val);
                    } else if (f.getType() == int.class) {
                        writer.name(f.getName()).value((Integer) val);
                    } else if (f.getType() == long.class) {
                        writer.name(f.getName()).value((Long) val);
                    } else {
                        if (f.getName().equals("$change")) {
                            //TODO
                        } else {
                            throw new RuntimeException("不可預期的欄位 : " + f.getName() + " - " + f.getType());
                        }
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "JsonWriter EnglishWord ERROR", ex);
                }
            }
            writer.endObject();

            writeCount++;
            Log.v(TAG, "write " + writeCount + " : " + ReflectionToStringBuilder.toString(word));
        }
    }
}
