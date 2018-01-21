package com.example.gtuandroid.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.gtuandroid._MainActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent bootIntent = new Intent(context,_MainActivity.class);
        context.startActivity(bootIntent);
    }

    /*
     * 接著在AndroidManifest.xml裡面新加入receiver
     * 
     * <receiver android:name=".BootBroadcastReceiver"> <intent-filter> <action
     * android:name="android.intent.action.BOOT_COMPLETED" /> <category
     * android:name="android.intent.category.HOME" /> </intent-filter>
     * </receiver>
     * 
     * 注意：receiver放的類別名稱是BroadcastReceiver子類別的名稱。
     * Android在開機的時候，會送出android.intent.action.BOOT_COMPLETED這個廣播訊息，
     * 因此只要設定好了，一開機就會執行指定的Activity。
     */
}