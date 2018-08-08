package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by gtu001 on 2018/8/8.
 */

public class ReaderCommonHelper {

    public static class PaddingAdjuster {
        Display d;
        int padWidth;
        int padHeight;
        int maxWidth;
        int maxHeight;

        public PaddingAdjuster(Context context) {
            d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            padWidth = (int) ((double) d.getWidth() * 0.1 / 2);
            padHeight = (int) ((double) d.getHeight() * 0.1 / 2);

            maxWidth = d.getWidth() - (2 * padWidth);
            maxHeight = d.getHeight() - (2 * padHeight);
        }

        public void applyPadding(TextView textView){
            textView.setPadding(padWidth, padHeight, padWidth, padHeight);
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
        }
    }

    public static class AppleFontApplyer {
        Typeface myriadProRegular;

        public AppleFontApplyer(Context context) {
            myriadProRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Myriad Pro Regular.ttf");
        }

        public void apply(TextView view) {
            view.setTypeface(myriadProRegular);
        }
    }
}
