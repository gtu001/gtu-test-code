package com.example.gtu001.qrcodemaker.common;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.example.gtu001.qrcodemaker.services.UrlPlayerService;

import java.util.List;

/**
 * Created by gtu001 on 2017/12/12.
 */

public class ServiceUtil {

    private static final String TAG = ServiceUtil.class.getSimpleName();

    /**
     * 判斷service是否執行中
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static void startStopService(boolean isStart, Context context, Class<?> serviceClz) {
        boolean isRunning = ServiceUtil.isServiceRunning(context, serviceClz);
        if (!isRunning && isStart) {
            Intent intent = new Intent(context, serviceClz);
            context.startService(intent);
            Log.v(TAG, "[startStopService] start");
        } else {
            Intent intent = new Intent(context, serviceClz);
            context.stopService(intent);
            Log.v(TAG, "[startStopService] end");
        }
    }


    public static void sendSMS(Context context, String contentMessage) {
        // This is the code to find the running services
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(Integer.MAX_VALUE);
        String message = null;

        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            Log.i("Service", "Process " + rsi.process + " with component " + rsi.service.getClassName());
            message = message + rsi.process;
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(contentMessage, null, message, null, null);
    }
}
