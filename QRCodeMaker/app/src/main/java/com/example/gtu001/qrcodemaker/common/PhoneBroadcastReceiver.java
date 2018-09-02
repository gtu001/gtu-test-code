package com.example.gtu001.qrcodemaker.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class PhoneBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = PhoneBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, ">>PhoneBroadcastReceiver>>" + Environment.getExternalStorageDirectory());
        Intent service = new Intent(context, PhoneService.class);
        context.startService(service);   //启动服务
        Toast.makeText(context, "已啟動電話自動錄音功能!", Toast.LENGTH_SHORT).show();
    }
}
