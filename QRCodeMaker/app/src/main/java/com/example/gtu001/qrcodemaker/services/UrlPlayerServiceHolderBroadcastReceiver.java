package com.example.gtu001.qrcodemaker.services;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.gtu001.qrcodemaker.common.Log;

import java.util.List;

public class UrlPlayerServiceHolderBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = UrlPlayerServiceHolderBroadcastReceiver.class.getSimpleName();

    /**
     * 判斷是否要關閉時強制重開
     */
    private static volatile boolean FLOAT_VIEW_SERVICE_ENABLE = false;

    public static boolean isFloatViewServiceEnable() {
        Log.v(TAG, "### isFloatViewServiceEnable : " + FLOAT_VIEW_SERVICE_ENABLE);
        for(StackTraceElement e : Thread.currentThread().getStackTrace()){
            Log.v(TAG, "\t\t##" + e);
        }
        Log.v(TAG, "### isFloatViewServiceEnable : " + FLOAT_VIEW_SERVICE_ENABLE);
        return FLOAT_VIEW_SERVICE_ENABLE;
    }

    public static void setFloatViewServiceEnable(boolean val) {
        Log.v(TAG, "### setFloatViewServiceEnable : " + val);
        for(StackTraceElement e : Thread.currentThread().getStackTrace()){
            Log.v(TAG, "\t\t##" + e);
        }
        Log.v(TAG, "### setFloatViewServiceEnable : " + val);
        UrlPlayerServiceHolderBroadcastReceiver.FLOAT_VIEW_SERVICE_ENABLE = val;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            System.out.println("手机开机了....");
            startUploadService(context);
        } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            startUploadService(context);
        } else if (Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())) {
            startUploadService(context);
        } else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            startUploadService(context);
        }
    }

    private void startUploadService(Context context) {
        if (!isServiceRunning(context, UrlPlayerService.class.getName()) && //
                UrlPlayerServiceHolderBroadcastReceiver.isFloatViewServiceEnable()) {
            Log.i(TAG, "test001 - 廣播重新啟動!~");
            Intent intent2 = new Intent(context, UrlPlayerService.class);
            intent2.setAction("FloatViewService");
            context.startService(intent2);
//            Toast.makeText(context, "test001 - 廣播重新啟動!~", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isServiceRunning(Context context, String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }
}