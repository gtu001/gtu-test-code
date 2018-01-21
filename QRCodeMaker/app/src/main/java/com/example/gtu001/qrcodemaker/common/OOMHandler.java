package com.example.gtu001.qrcodemaker.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by gtu001 on 2017/10/21.
 */

public class OOMHandler {

    private static final String TAG = OOMHandler.class.getSimpleName();

    @Deprecated
    public static Bitmap new_decode_OLD(File f) {
        // decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;// 再載入時就先縮小
        o.inDither = false; // Disable Dithering mode

        o.inPurgeable = true; // Tell to gc that whether it needs free memory,
        // the Bitmap can be cleared

        o.inInputShareable = true; // Which kind of reference will be used to
        // recover the Bitmap data after being
        // clear, when it will be used in the future
        try {
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        // Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = 300;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 1.5 < REQUIRED_SIZE && height_tmp / 1.5 < REQUIRED_SIZE)
                break;
            width_tmp /= 1.5;
            height_tmp /= 1.5;
            scale *= 1.5;
        }

        // decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        // o2.inSampleSize=scale;
        o.inDither = false; // Disable Dithering mode

        o.inPurgeable = true; // Tell to gc that whether it needs free memory,
        // the Bitmap can be cleared

        o.inInputShareable = true; // Which kind of reference will be used to
        // recover the Bitmap data after being
        // clear, when it will be used in the future
        // return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        try {

            // return BitmapFactory.decodeStream(new FileInputStream(f), null,
            // null);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
            System.out.println(" IW " + width_tmp);
            System.out.println("IHH " + height_tmp);
            int iW = width_tmp;
            int iH = height_tmp;

            return Bitmap.createScaledBitmap(bitmap, iW, iH, true);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            // clearCache();

            // System.out.println("bitmap creating success");
            System.gc();
            return null;
            // System.runFinalization();
            // Runtime.getRuntime().gc();
            // System.gc();
            // decodeFile(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap new_decode(File f) {
        try {
            InputStream inputStream = new FileInputStream(f);
            Bitmap bitmap = null;
            for (int ii = 0; ; ii++) {
                try {
                    bitmap = BitmapFactory.decodeStream(inputStream, null, getBitmapOptions(ii));
                    Log.v(TAG, "Bitmap scale = " + ii);
                    return bitmap;
                } catch (OutOfMemoryError ex) {
                    Log.v(TAG, "Bitmap scale = " + ii);
                    bitmap.recycle();
                    System.gc();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static Bitmap new_decode(InputStream inputStream) {
        Bitmap bitmap = null;
        for (int ii = 0; ; ii++) {
            try {
                bitmap = BitmapFactory.decodeStream(inputStream, null, getBitmapOptions(ii));
                Log.v(TAG, "Bitmap scale = " + ii);
                return bitmap;
            } catch (OutOfMemoryError ex) {
                Log.v(TAG, "Bitmap scale = " + ii);
                bitmap.recycle();
                System.gc();
            }
        }
    }

    public static Bitmap new_decode(Context context, int resourceId) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        Bitmap bitmap = null;
        for (int ii = 0; ; ii++) {
            try {
                bitmap = BitmapFactory.decodeStream(inputStream, null, getBitmapOptions(ii));
                Log.v(TAG, "Bitmap scale = " + ii);
                return bitmap;
            } catch (OutOfMemoryError ex) {
                Log.v(TAG, "Bitmap scale = " + ii);
                bitmap.recycle();
                System.gc();
            }
        }
    }

    private static BitmapFactory.Options getBitmapOptions(int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;//這樣可以讓java系統記憶體不足時先行回收部分的記憶體
        options.inInputShareable = true;
        options.inSampleSize = scale;//大概設2~4就差不多了
        try {
//            BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return options;
    }
}
