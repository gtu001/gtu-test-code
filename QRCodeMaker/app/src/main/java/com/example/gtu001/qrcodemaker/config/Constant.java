package com.example.gtu001.qrcodemaker.config;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Constant {

    private static final String TAG = Constant.class.getSimpleName();

    static {
        try {
            Log.v("!!!!!!", "externalStorageDirectory = " + Environment.getExternalStorageDirectory()); ///storage/sdcard0

            String prefix = Environment.getExternalStorageDirectory().getAbsolutePath();

            //確定SD卡可讀寫
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.v(TAG, "可讀寫SD卡!");
            }

            RECORD_DIR = mkdir(prefix + File.separator + "My Documents" + File.separator + "phoneRecorder" + File.separator);

        } catch (Exception e) {
            Log.e(TAG, "Load Constant ERROR : " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static String RECORD_DIR;

    //method 放在此線下 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    private static String mkdir(String paths) {
        File file = new File(paths);
        if (!file.exists()) {
            boolean makeDefaultDir = file.mkdirs();
            Log.v(TAG, "建立基礎目錄 - " + makeDefaultDir + " - " + file);
        }
        return paths;
    }
}
