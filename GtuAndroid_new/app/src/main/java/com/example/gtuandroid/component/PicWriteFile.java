package com.example.gtuandroid.component;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class PicWriteFile {

    private static final String TAG = PicWriteFile.class.getSimpleName();

    public enum PicExtension {
        png, jpeg
    }

    /* 將圖像保存到Data目錄 */
    public static boolean saveBitmapToData(Activity act, Bitmap bmpToSave, String fileNameWithoutExtension, PicExtension ext, int quality) {// 參數依次為：調用的
        try {
            if (quality > 100) {
                quality = 100;
            } else if (quality < 1) {
                quality = 1;
            }
            FileOutputStream fos = act.openFileOutput(//
                    fileNameWithoutExtension + "." + ext.toString(), //
                    Context.MODE_PRIVATE); // 這裡是關鍵，其實就是一個不含路徑但包含副檔名的檔案名
            if (ext == PicExtension.png) {
                bmpToSave.compress(Bitmap.CompressFormat.PNG, quality, fos);
            } else if (ext == PicExtension.jpeg) {
                bmpToSave.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            }
            // 寫入檔
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /* 從Data目錄讀取圖像 */
    public static Bitmap getBitmapFromData(Activity act, String FileName) {
        FileInputStream fis = null;
        try {
            fis = act.openFileInput(FileName);
        } catch (FileNotFoundException e) {
            Log.w(TAG, e.getMessage());
            e.printStackTrace();
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        Bitmap bmpRet = BitmapFactory.decodeStream(bis);
        try {
            bis.close();
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
            e.printStackTrace();
        }
        return bmpRet;
    }
}
