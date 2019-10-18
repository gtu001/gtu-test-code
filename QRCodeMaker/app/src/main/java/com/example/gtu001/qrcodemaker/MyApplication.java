package com.example.gtu001.qrcodemaker;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context mContext;
    private static MyApplication mMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mMyApplication = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Context getApplication() {
        return mMyApplication;
    }
}