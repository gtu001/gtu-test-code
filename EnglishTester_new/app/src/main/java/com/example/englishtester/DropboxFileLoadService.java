package com.example.englishtester;

import android.content.Context;
import android.os.Handler;

import com.example.englishtester.common.Log;

import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.example.englishtester.common.DropboxUtilV2;
import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.MockServiceMacker;
import com.example.englishtester.common.interf.IDropboxFileLoadService;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DropboxFileLoadService implements IDropboxFileLoadService {

    private static final String TAG = DropboxFileLoadService.class.getSimpleName();
    private final static String ENGLISH_TXT_FOLDER = "/english_txt";

    Context context;
    String accessToken;

    Handler handler = new Handler();

    private DropboxFileLoadService(Context context, String accessToken) {
        Validate.notBlank(accessToken);
        this.context = context;
        this.accessToken = accessToken;
    }

    public static IDropboxFileLoadService newInstance(final Context context, final String accessToken) {
        try {
            return new DropboxFileLoadService(context, accessToken);
        } catch (Exception ex) {
            return MockServiceMacker.getMockStuff("dropbox accessToken未初始化!", context, DropboxFileLoadService.class);
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

    public static boolean isPicFileType(String name) {
        if (StringUtils.isNotBlank(name) &&
                name.matches(".*\\.(jpg|jpeg|png|gif|bmp|pcx|tiff|tga|exif|pfx|svg|psd|cdr|pcd|dxf|ufo|eps)")
                ) {
            return true;
        }
        return false;
    }

    public File downloadHtmlReferencePicDir(final String dropboxDirName, final long timeout) {
        return DropboxEnglishService.getRunOnUiThread(new Callable<File>() {
            @Override
            public File call() throws Exception {
                String tmpPath = "";
                try {
                    File dirFile = new File(context.getCacheDir(), dropboxDirName);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }

                    final DbxClientV2 client = getClient();
                    List<DropboxUtilV2.DropboxUtilV2_DropboxFile> picLst = DropboxUtilV2.listFilesV2(ENGLISH_TXT_FOLDER + File.separator + dropboxDirName, client);

                    //for check
                    List<File> downloadLst = new ArrayList<File>();

                    ExecutorService executor = Executors.newFixedThreadPool(5);
                    List<Callable<File>> downloadTaskLst = new ArrayList<>();

                    for (final DropboxUtilV2.DropboxUtilV2_DropboxFile pic : picLst) {
                        if (!isPicFileType(pic.getName())) {
                            continue;
                        }

                        tmpPath = pic.getFullPath();

                        final File targetPicFile = new File(dirFile, pic.getName());
                        downloadLst.add(targetPicFile);

                        if (targetPicFile.exists() && targetPicFile.canRead() && targetPicFile.length() > 0) {
                            continue;
                        }

                        downloadTaskLst.add(new Callable<File>() {
                            @Override
                            public File call() throws Exception {
                                FileOutputStream outputStream = new FileOutputStream(targetPicFile);
                                DropboxUtilV2.download(pic.getFullPath(), outputStream, client);
                                Log.v(TAG, "下載ref pic : " + targetPicFile);
                                return targetPicFile;
                            }
                        });
                    }

                    List<Future<File>> rtnLst = null;
                    if (timeout != -1) {
                        rtnLst = executor.invokeAll(downloadTaskLst, timeout, TimeUnit.MILLISECONDS);
                    } else {
                        rtnLst = executor.invokeAll(downloadTaskLst);
                    }

                    for (Future<File> fu : rtnLst) {
                        File f = fu.get();
                        Log.v(TAG, "####### 圖片 : " + (f.exists() && f.canRead()) + " -> " + f + " -> " + FileUtilGtu.getSizeDescription(f.length()));
                    }
                    return dirFile;
                } catch (Exception ex) {
                    Log.e(TAG, "downloadHtmlReferencePicDir ERR : " + ex.getMessage() + " --> " + tmpPath);
                    throw new RuntimeException("downloadHtmlReferencePicDir ERR : " + ex.getMessage() + " --> " + tmpPath, ex);
                } finally {
                }
            }
        }, -1L);
    }

    private DbxClientV2 getClient() {
        return DropboxUtilV2.getClient(accessToken);
    }
}
