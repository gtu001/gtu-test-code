package com.example.gtu001.qrcodemaker.common;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class TitleUtil {

    public static void showTitle(Activity activity, boolean isShow) {
        if (isShow) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }
}
