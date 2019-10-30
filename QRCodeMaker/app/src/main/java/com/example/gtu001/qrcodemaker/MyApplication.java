package com.example.gtu001.qrcodemaker;

import android.app.Application;
import android.content.Context;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {
//    private static Context mContext;
//    private static MyApplication mMyApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mContext = getApplicationContext();
//        mMyApplication = this;
//    }

//    public static Context getContext() {
//        return mContext;
//    }
//
//    public static Context getApplication() {
//        return mMyApplication;
//    }
}
