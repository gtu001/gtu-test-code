package com.example.englishtester.common;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
            public boolean setViewValue(View view, Object contentObj, String s) {
                if (view instanceof ImageView) {
                    ImageView i = (ImageView) view;
                    if (contentObj instanceof Bitmap) {
                        i.setImageBitmap((Bitmap) contentObj);
                        return true;
                    } else if (contentObj instanceof Drawable) {
                        i.setImageDrawable((Drawable) contentObj);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
