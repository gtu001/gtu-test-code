package com.example.englishtester;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.example.englishtester.common.DropboxUtilV2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DropboxFileLoadService {

    private static final String TAG = DropboxFileLoadService.class.getSimpleName();
    private final static String ENGLISH_TXT_FOLDER = "/english_txt";

    Context context;
    String accessToken;

    Handler handler = new Handler();

    public DropboxFileLoadService(Context context, String accessToken) {
        this.context = context;
        this.accessToken = accessToken;
    }

    private void toastShow(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 列出檔案清單
     */
    public Map<String, String> listFile() {
        return DropboxEnglishService.getRunOnUiThread(new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {
                DbxClientV2 client = getClient();
//                Map<String, DbxEntry> fileMap = new LinkedHashMap<String, DbxEntry>();
//                DbxEntry.WithChildren listing = client.getMetadataWithChildren(ENGLISH_TXT_FOLDER);
//                Log.v(TAG, "Files in the root path:");
//                for (DbxEntry child : listing.children) {
//                    System.out.println("    " + child.name + ": " + child.toString());
//                    fileMap.put(child.name, child);
//                }
//                return fileMap;
                return DropboxUtilV2.listFiles(ENGLISH_TXT_FOLDER, client);
            }
        }, -1L);
    }

    /**
     * 列出檔案清單
     */
    public List<DropboxUtilV2.DropboxUtilV2_DropboxFile> listFileV2() {
        return DropboxEnglishService.getRunOnUiThread(new Callable<List<DropboxUtilV2.DropboxUtilV2_DropboxFile>>() {
            @Override
            public List<DropboxUtilV2.DropboxUtilV2_DropboxFile> call() throws Exception {
                DbxClientV2 client = getClient();
                return DropboxUtilV2.listFilesV2(ENGLISH_TXT_FOLDER, client);
            }
        }, -1L);
    }

    /**
     * 下載dropbox檔案
     */
    public File downloadFile(final String path) {
        return DropboxEnglishService.getRunOnUiThread(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File tmpFile = File.createTempFile("temp_txt_", ".txt");
                final FileOutputStream outputStream = new FileOutputStream(tmpFile);
                try {
                    DbxClientV2 client = getClient();
//                    DbxEntry.File downloadedFile = client.getFile(path, null, outputStream);
//                    if (downloadedFile != null) {
//                        Log.v(TAG, "Metadata: " + downloadedFile.toString());
//                    }
                    DropboxUtilV2.download(path, outputStream, client);
                } finally {
                    outputStream.close();
                }
                return tmpFile;
            }
        }, -1L);
    }

    /**
     * 下載dropbox檔案
     */
    public File downloadFile(final String path, final String fileExtension) {
        final String newExtension = fileExtension.startsWith(".") ? fileExtension : "." + fileExtension;
        return DropboxEnglishService.getRunOnUiThread(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File tmpFile = File.createTempFile("temp_txt_", newExtension);
                final FileOutputStream outputStream = new FileOutputStream(tmpFile);
                try {
                    DbxClientV2 client = getClient();
                    DropboxUtilV2.download(path, outputStream, client);
                } finally {
                    outputStream.close();
                }
                return tmpFile;
            }
        }, -1L);
    }

    /**
     * 上傳英文單字檔自雲端
     */
    public void uploadFile(final File file, final String fileName) throws DbxException, IOException {
        DropboxEnglishService.getRunOnUiThread(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                FileInputStream inputStream = new FileInputStream(file);
                try {
                    DbxClientV2 client = getClient();
//                    DbxEntry.File uploadedFile = client.uploadFile(ENGLISH_TXT_FOLDER + "/" + fileName, DbxWriteMode.add(), file.length(), inputStream);
//                    Log.v(TAG, "Uploaded: " + uploadedFile.toString());
                    DropboxUtilV2.upload(ENGLISH_TXT_FOLDER + "/" + fileName, inputStream, client);
                    toastShow("檔案已上傳 : " + fileName);
                } finally {
                    inputStream.close();
                }
                return null;
            }
        }, -1L);
    }

    private DbxClientV2 getClient() {
        return DropboxUtilV2.getClient(accessToken);
    }
}
