package com.example.englishtester.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

/**
 * Created by gtu001 on 2017/5/25.
 */

public class SharedPreferencesUtil {
    public static boolean hasData(Context context, String refKey, String bundleKey) {
        SharedPreferences settings = context.getSharedPreferences(refKey, 0);
        return settings.contains(bundleKey);
    }

    public static void putData(Context context, String refKey, String bundleKey, String value) {
        SharedPreferences settings = context.getSharedPreferences(refKey, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(bundleKey, value);
        editor.commit();
    }

    public static String getData(Context context, String refKey, String bundleKey) {
        SharedPreferences settings = context.getSharedPreferences(refKey, 0);
        String text = settings.getString(bundleKey, "");
        return text;
    }
}
