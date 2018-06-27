package com.example.englishtester.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.example.englishtester.FloatViewActivity;
import com.example.englishtester.FloatViewService;

/**
 * Created by wistronits on 2018/6/27.
 */

public class ServiceUtil {

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

    public static void startStopService(boolean isStart, Context context, Class<? extends Service> serviceClz) {
        boolean isRunning = isServiceRunning(context, serviceClz);
        if (!isRunning && isStart) {
            Intent intent = new Intent(context, serviceClz);
            context.startService(intent);
        } else {
            Intent intent = new Intent(context, serviceClz);
            context.stopService(intent);
        }
    }
}
