package com.example.gtuandroid.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by gtu001 on 2017/11/20.
 */

public class StringResourceHelper  {

    public static String getString(int stringId, Object... arry){
        return Resources.getSystem().getString(stringId, arry);
    }

    public static String getStringByName(String name, Context context){
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(name, "string", packageName);
        return getString(resId);
    }
}
