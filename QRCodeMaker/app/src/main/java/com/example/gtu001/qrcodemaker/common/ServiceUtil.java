package com.example.gtu001.qrcodemaker.common;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by gtu001 on 2017/12/12.
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
}
