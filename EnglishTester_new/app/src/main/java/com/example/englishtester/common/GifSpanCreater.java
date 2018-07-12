package com.example.englishtester.common;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by gtu001 on 2018/7/13.
 */

public class GifSpanCreater {

    private static final String TAG = GifSpanCreater.class.getSimpleName();

    public interface ResetScale {
        public int[] giveMeBackWidthAndHeight(int width, int height);
    }

    public static ImageSpan getGifSpan(File file, final TextView tv, ResetScale resetScale) {
        try {
            GifDrawable gd = new GifDrawable(file);

            //自訂寬高
            int[] widthAndHeight = new int[]{gd.getIntrinsicWidth(), gd.getIntrinsicHeight()};
            if (resetScale != null) {
                widthAndHeight = resetScale.giveMeBackWidthAndHeight(widthAndHeight[0], widthAndHeight[1]);
            }

            gd.setBounds(0, 0, widthAndHeight[0], widthAndHeight[1]);
            gd.setCallback(new Drawable.Callback() {
                @Override
                public void invalidateDrawable(Drawable who) {
                    tv.invalidate();
                }

                @Override
                public void scheduleDrawable(Drawable who, Runnable what, long when) {
                    tv.postDelayed(what, when);
                }

                @Override
                public void unscheduleDrawable(Drawable who, Runnable what) {
                    tv.removeCallbacks(what);
                }
            });
            return new ImageSpan(gd);
        } catch (Exception ex) {
            Log.e(TAG, "getGifSpan ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("getGifSpan ERR : " + ex.getMessage(), ex);
        }
    }
}
