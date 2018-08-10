package com.example.englishtester.common;

import android.content.Context;
import com.example.englishtester.common.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gtu001 on 2017/6/2.
 */

public class FileConstantAccessUtil {

    private static final String TAG = FileConstantAccessUtil.class.getSimpleName();

    /**
     * 取得檔案OuputStream
     */
    public static FileOutputStream getOutputStream(Context context, File file) throws FileNotFoundException {
        boolean result = file.getParentFile().mkdirs();
        boolean exists = file.getParentFile().exists();
        Log.v(TAG, "#### Dir : " + file.getParentFile() + " - " + exists);
        if (!exists) {
            Log.v(TAG, "### go Internal DIR");
            return context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
        } else {
            Log.v(TAG, "### go Custom DIR");
            return new FileOutputStream(file);
        }
    }

    /**
     * 取得檔案InputStream
     */
    public static FileInputStream getInputStream(Context context, File file, boolean ifNotExistMakeOne) throws FileNotFoundException {
        boolean result = file.getParentFile().mkdirs();
        boolean exists = file.exists();
        //是否要產生空檔
        if (ifNotExistMakeOne) {
            createEmptyFile(context, file);
        }
        Log.v(TAG, "#### Dir : " + file.getParentFile() + " - " + exists);
        if (!exists) {
            Log.v(TAG, "### go Internal DIR");
            return context.openFileInput(file.getName());
        } else {
            Log.v(TAG, "### go Custom DIR");
            return new FileInputStream(file);
        }
    }

    /**
     * 建立空檔案
     */
    public static void createEmptyFile(Context context, File file) {
        try {
            File chkFile = getFile(context, file);
            if (!chkFile.exists()) {
                Log.v(TAG, "建立空檔 : " + chkFile);
                file.createNewFile();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 取得實體檔案
     */
    public static File getFile(Context context, File file) {
        file.getParentFile().mkdirs();
        if (file.getParentFile().exists()) {
            return file;
        } else {
            return new File(context.getFilesDir(), file.getName());
        }
    }

    /**
     * 取得實體目錄
     */
    public static File getFileDir(Context context, File dir){
        if(dir.exists() && dir.isDirectory()){
            return dir;
        }else{
            return context.getFilesDir();
        }
    }
}
