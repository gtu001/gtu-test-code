package com.example.gtuandroid.bean;

import android.app.Application;

public class MyApp extends Application {
    private Person p;

    @Override
    public void onCreate() {
        super.onCreate();
        p = new Person();
    }

    public Person getPerson() {
        return p;
    }
}