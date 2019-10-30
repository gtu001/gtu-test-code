app->gradle
---
	defaultConfig {
        multiDexEnabled true //method量超標
    }


jar
---
	implementation 'com.android.support:multidex:1.0.1'  //method量超標



Java
---
	package com.example.englishtester.common;

	import android.content.Context;

	import androidx.multidex.MultiDex;
	import androidx.multidex.MultiDexApplication;

	/**
	 * method量超標
	 */
	public class MyApplication extends MultiDexApplication {
	    @Override
	    protected void attachBaseContext(Context base) {
	        super.attachBaseContext(base);
	        MultiDex.install(this);
	    }
	}


AndroidManifest.xml
---
	<application
        android:name="com.example.englishtester.common.MyApplication"