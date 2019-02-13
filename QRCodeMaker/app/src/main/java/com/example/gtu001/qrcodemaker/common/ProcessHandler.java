package com.example.gtu001.qrcodemaker.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ProcessHandler {

    private static final String TAG = ProcessHandler.class.getSimpleName();

    public static void killSelf() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 不能殺自己
     */
    public static void killProcessByPackage1(Context context, String pkgName) {
        List<ApplicationInfo> packages;
        PackageManager pm = context.getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }

            Log.v(TAG, "pkgName : " + packageInfo.packageName);

            if (StringUtils.equals(pkgName, packageInfo.packageName)) {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);


                Toast.makeText(context, "停止Pkg : " + packageInfo.packageName, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 應該可以殺自己 未測試
     */
    public static void killProcessByPackage2(Context context, String pkgName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> pids = am.getRunningAppProcesses();
        for (int i = 0; i < pids.size(); i++) {
            ActivityManager.RunningAppProcessInfo info = pids.get(i);

            Log.v(TAG, "pkgName : " + info.processName + " \t PID : " + info.pid);

            if (info.processName.equalsIgnoreCase(pkgName)) {
                android.os.Process.killProcess(info.pid);
            }
        }
    }
}
