package com.example.englishtester.common;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

/**
 * Created by wistronits on 2018/8/28.
 */

public class SimpleAdapterDecorator {

    /**
     * 若是bitmap需要此段code
     */
    public static void apply4Bitmap(SimpleAdapter aryAdapter) {
        aryAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object bitmapData, String s) {
                if (view instanceof ImageView && bitmapData instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) bitmapData);
                    return true;
                }
                return false;
            }
        });
    }
}
