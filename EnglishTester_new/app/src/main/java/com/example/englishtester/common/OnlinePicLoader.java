package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.englishtester.R;

/**
 * Created by wistronits on 2018/7/10.
 */

public class OnlinePicLoader {

    private Context context;
    private static Bitmap NOTFOUND404;
    private int fixWidth;

    public OnlinePicLoader(Context context, int fixWidth) {
        this.context = context;
        this.fixWidth = fixWidth;
        getNotfound404();
    }

    public Bitmap getNotfound404() {
        if (NOTFOUND404 == null || NOTFOUND404.isRecycled()) {
            NOTFOUND404 = OOMHandler2.decodeSampledBitmapFromResource(context.getResources(), R.drawable.notfound_404, OOMHandler2.getCustomFixWidth(fixWidth));
        }
        return NOTFOUND404;
    }

    public Bitmap getBitmapFromURL_waiting(final String url, long wattingTime, Integer scale) {
        Bitmap bitmap = OOMHandler.getBitmapFromURL_waiting(url, wattingTime, scale);
        bitmap = OOMHandler.fixPicScaleFixScreenWidth(bitmap, fixWidth);
        if (OOMHandler.DEFAULT_EMPTY_BMP != bitmap) {
            return bitmap;
        }
        return null;
    }
}
