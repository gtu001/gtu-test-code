package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.englishtester.R;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by wistronits on 2018/7/10.
 */

public class OnlinePicLoader {

    private Context context;
    private static Bitmap NOTFOUND404;

    public OnlinePicLoader(Context context) {
        this.context = context;
        getNotfound404();
    }

    public Bitmap getNotfound404() {
        if (NOTFOUND404 == null) {
            NOTFOUND404 = OOMHandler.new_decode(context, R.drawable.notfound_404);
        }
        return NOTFOUND404;
    }

    public Bitmap getBitmapFromURL_waiting(final String url, long wattingTime) {
        Bitmap bitmap = OOMHandler.getBitmapFromURL_waiting(url, wattingTime);
        if (OOMHandler.DEFAULT_EMPTY_BMP == bitmap) {
            return getNotfound404();
        }
        return bitmap;
    }
}
