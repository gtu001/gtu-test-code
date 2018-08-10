package com.example.englishtester.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.example.englishtester.common.Log;

/**
 * Created by gtu001 on 2018/6/4.
 */

public class AppOpenHelper {

    private static final String TAG = AppOpenHelper.class.getSimpleName();

    /**
     * Open another app.
     *
     * 取得packageName的方式 , 可從 googleplay 的網址找到
     * Ex : https://play.google.com/store/apps/details?id=jp.naver.line.android
     *
     * @param context     current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @return true if likely successful, false if unsuccessful
     */
    public static boolean openApp(Context context, String packageName) {
        Log.v(TAG, "openApp :" + packageName);
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }
}
