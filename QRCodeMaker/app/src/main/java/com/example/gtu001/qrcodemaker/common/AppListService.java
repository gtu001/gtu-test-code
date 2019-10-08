package com.example.gtu001.qrcodemaker.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.example.gtu001.qrcodemaker.services.AppInfoService;


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

        public void setInstalledPackage(String installedPackage) {
            this.installedPackage = installedPackage;
        }

        public void setSourceDir(String sourceDir) {
            this.sourceDir = sourceDir;
        }

        public void setLaunchActivity(Intent launchActivity) {
            this.launchActivity = launchActivity;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }
    }

    //------------------------------------------------------------------------DB Process START

    public static List<AppInfo> loadAllAppListMaster(Context context, boolean isReload) {
        DBHolder d = new DBHolder(context);
        return d.loadAllAppListMaster(isReload);
    }


    private static class DBHolder {
        AppInfoService service;
        Context context;
        final PackageManager pm;

        private DBHolder(Context context) {
            service = new AppInfoService(context);
            this.context = context;
            pm = context.getPackageManager();
        }

        public List<AppInfo> loadAllAppListMaster(boolean isReload) {
            List<AppInfo> lst = null;
            int countSize = service.countAll();
            if (countSize == 0 || isReload) {
                reloadAllAppListToDB();
            }
            return loadAllAppListFromDB();
        }

        private void reloadAllAppListToDB() {
            List<com.example.gtu001.qrcodemaker.dao.AppInfoDAO.AppInfo> lst2 = service.queryAll();
            for (com.example.gtu001.qrcodemaker.dao.AppInfoDAO.AppInfo vo : lst2) {
                service.deleteId(vo.getInstalledPackage());
            }
            List<AppInfo> lst = AppListService.getInstance().loadAllAppList(context);
            for (AppInfo vo : lst) {
                com.example.gtu001.qrcodemaker.dao.AppInfoDAO.AppInfo vo2 = new com.example.gtu001.qrcodemaker.dao.AppInfoDAO.AppInfo();
                vo2.setInstalledPackage(vo.getInstalledPackage());
                vo2.setSourceDir(vo.getSourceDir());
                vo2.setLabel(vo.getLabel());
                vo2.setIcon(ImageBase64Util.encodeToBase64(vo.getIcon(), "png", 100));
                service.insertData(vo2);
            }
        }

        public List<AppInfo> loadAllAppListFromDB() {
            List<com.example.gtu001.qrcodemaker.dao.AppInfoDAO.AppInfo> lst2 = service.queryAll();
            List<AppInfo> lst = new ArrayList<AppInfo>();
            for (com.example.gtu001.qrcodemaker.dao.AppInfoDAO.AppInfo vo : lst2) {
                AppInfo vo2 = new AppInfo();
                vo2.setInstalledPackage(vo.getInstalledPackage());
                vo2.setSourceDir(vo.getSourceDir());
                vo2.setLabel(vo.getLabel());
                try {
                    vo2.setIcon(ImageBase64Util.decodeBase64ToDrawable(vo.getIcon(), context));
                } catch (Exception ex) {
                    Log.e(TAG, "loadAllAppListFromDB ERR : " + ex.getMessage(), ex);
                }
                lst.add(vo2);
            }
            return lst;
        }

        private void setDetailVo(AppInfo vo2) {
            vo2.setLaunchActivity(pm.getLaunchIntentForPackage(vo2.getInstalledPackage()));
            vo2.label = getAppLabel(context, vo2.getInstalledPackage());
            vo2.icon = getIcon(context, vo2.getInstalledPackage());
        }
    }

    //------------------------------------------------------------------------DB Process END

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
