package com.example.gtu001.qrcodemaker.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AppListService {

    private final static String TAG = AppListService.class.getSimpleName();

    private final static AppListService _INST = new AppListService();

    private AppListService() {
    }

    public static AppListService getInstance() {
        return _INST;
    }

    public static class AppInfo {
        String installedPackage;
        String sourceDir;
        Intent launchActivity;
        String label;
        Drawable icon;

        public void run(Context context) {
            AppListService.launchApp(context, installedPackage);
        }

        public String getInstalledPackage() {
            return installedPackage;
        }

        public String getSourceDir() {
            return sourceDir;
        }

        public Intent getLaunchActivity() {
            return launchActivity;
        }

        public String getLabel() {
            return label;
        }

        public Drawable getIcon() {
            return icon;
        }
    }

    public List<AppInfo> loadAllAppList(Context context) {
        List<AppInfo> lst = new ArrayList<>();
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            AppInfo app = new AppInfo();

            app.installedPackage = packageInfo.packageName;
            app.sourceDir = packageInfo.sourceDir;
            app.launchActivity = pm.getLaunchIntentForPackage(packageInfo.packageName);
            app.label = getAppLabel(context, packageInfo.packageName);
            app.icon = getIcon(context, packageInfo.packageName);

            lst.add(app);
        }
        return lst;
    }

    public static Drawable getIcon(Context context, String packageName) {
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return icon;
    }

    public static String getAppLabel(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        String name = "";
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                name = packageManager.getApplicationLabel(applicationInfo).toString();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return name;
    }

    public static void launchApp(Context context, String packageName) {
        Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            context.startActivity(mIntent);
        }
    }
}
