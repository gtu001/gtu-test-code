package com.example.gtu001.qrcodemaker.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageBase64Util {

    public static String encodeToBase64(Drawable draw, String imgType, int quality) {
        BitmapDrawable drawable = (BitmapDrawable) draw;
        Bitmap bitmap = drawable.getBitmap();
        return encodeToBase64(bitmap, imgType, quality);
    }

    public static String encodeToBase64(Bitmap image, String imgType, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        Bitmap.CompressFormat imgEnumType = "png".equalsIgnoreCase(imgType) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
        image.compress(imgEnumType, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64ToBitmap(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static Drawable decodeBase64ToDrawable(String input, Context context) {
        Bitmap bitmap = decodeBase64ToBitmap(input);
        Drawable d = new BitmapDrawable(context.getResources(), bitmap);
        return d;
    }
}
