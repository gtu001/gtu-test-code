package com.example.englishtester.common;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by wistronits on 2018/8/27.
 */

public class PixelMetricUtil {

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static int convertDpToPx(int dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));

    }

    public static int convertPxToDp(int px) {
        return Math.round(px / (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float getDpFromPx(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public static float getPxFromDp(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }
}
