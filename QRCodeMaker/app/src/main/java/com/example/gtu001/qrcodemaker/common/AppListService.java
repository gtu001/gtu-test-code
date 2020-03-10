package com.example.gtu001.qrcodemaker.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.gtu001.qrcodemaker.dao.AppInfoDAO;
import com.example.gtu001.qrcodemaker.services.AppInfoService;

import org.apache.commons.lang3.StringUtils;

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
        String tag;

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

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    //------------------------------------------------------------------------DB Process START

    public boolean updateAppInfoTag(AppInfo vo, Context context) {
        DBHolder d = new DBHolder(context);
        return d.updateTag(vo);
    }

    public DataWrapper loadAllAppListMaster(Context context, boolean isReload) {
        DataWrapper rtnObj = new DataWrapper();
        DBHolder d = new DBHolder(context);
        rtnObj.appLst = d.loadAllAppListMaster(isReload);
        for (AppInfo vo : rtnObj.appLst) {
            d.appendTagToLst(vo.getTag(), rtnObj.getTagLst());
        }
        return rtnObj;
    }

    public static class DataWrapper {
        List<AppInfo> appLst = new ArrayList<>();
        List<String> tagLst = new ArrayList<>();

        public List<AppInfo> getAppLst() {
            return appLst;
        }

        public List<String> getTagLst() {
            return tagLst;
        }
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

        public boolean updateTag(AppInfo vo) {
            AppInfoDAO.AppInfo vo2 = service.findByPk(vo.getInstalledPackage());
            vo2.setTag(vo.getTag());
            return service.update(vo2);
        }

        public List<AppInfo> loadAllAppListMaster(boolean isReload) {
            List<AppInfo> lst = null;
            int countSize = service.countAll();
            if (countSize == 0 || isReload) {
                reloadAllAppListToDB();
            }
            return loadAllAppListFromDB();
        }

        private AppInfo _findByLst1(String installedPackage, List<AppInfo> lst) {
            for (int jj = 0; jj < lst.size(); jj++) {
                AppInfo vo_ = lst.get(jj);
                if (StringUtils.equals(installedPackage, vo_.getInstalledPackage())) {
                    return vo_;
                }
            }
            return null;
        }

        private AppInfoDAO.AppInfo _findByLst2(String installedPackage, List<AppInfoDAO.AppInfo> lst) {
            for (int jj = 0; jj < lst.size(); jj++) {
                AppInfoDAO.AppInfo vo_ = lst.get(jj);
                if (StringUtils.equals(installedPackage, vo_.getInstalledPackage())) {
                    return vo_;
                }
            }
            return null;
        }

        private void reloadAllAppListToDB() {
            List<String> logLst = new ArrayList<>();
            List<AppInfo> lst = AppListService.getInstance().loadAllAppList(context);//系統重load
            List<AppInfoDAO.AppInfo> lst2 = service.queryAll();//資料庫load
            logLst.add("sysReload " + lst.size());
            logLst.add("dbReload " + lst2.size());
            for (int ii = 0; ii < lst.size(); ii++) {
                AppInfo vo = lst.get(ii);
                AppInfoDAO.AppInfo vo2 = _findByLst2(vo.getInstalledPackage(), lst2);
                if (vo2 == null) {
                    vo2 = new AppInfoDAO.AppInfo();
                    vo2.setInstalledPackage(vo.getInstalledPackage());
                    vo2.setSourceDir(vo.getSourceDir());
                    vo2.setLabel(vo.getLabel());
                    vo2.setIcon(ImageBase64Util.encodeToBase64(vo.getIcon(), "png", 100));
                    vo2.setTag(vo.getTag());
                    service.insertData(vo2);
                    logLst.add("A " + vo.getInstalledPackage());
                } else {
                }
            }
            for (int ii = 0; ii < lst2.size(); ii++) {
                AppInfoDAO.AppInfo vo = lst2.get(ii);
                AppInfo vo2 = _findByLst1(vo.getInstalledPackage(), lst);
                if (vo2 == null) {
                    service.deleteId(vo.getInstalledPackage());
                    logLst.add("D " + vo.getInstalledPackage());
                } else {
                }
            }
            Log.lineFix(TAG, StringUtils.join(logLst, "\r\n"));
        }

        private void appendTagToLst(String tag, List<String> tagLst) {
            tag = StringUtils.trimToEmpty(tag);
            String[] tags = tag.split(",", -1);
            for (String t : tags) {
                t = StringUtils.trimToEmpty(t);
                if (StringUtils.isNotBlank(t)) {
                    boolean alreadyHas = false;
                    A:
                    for (String t1 : tagLst) {
                        if (t1.equalsIgnoreCase(t)) {
                            alreadyHas = true;
                            break A;
                        }
                    }
                    if (!alreadyHas) {
                        tagLst.add(t);
                    }
                }
            }
        }

        public List<AppInfo> loadAllAppListFromDB() {
            List<AppInfoDAO.AppInfo> lst2 = service.queryAll();
            List<AppInfo> lst = new ArrayList<AppInfo>();
            List<String> tagLst = new ArrayList<>();
            for (AppInfoDAO.AppInfo vo : lst2) {
                AppInfo vo2 = new AppInfo();
                vo2.setInstalledPackage(vo.getInstalledPackage());
                vo2.setSourceDir(vo.getSourceDir());
                vo2.setLabel(vo.getLabel());
                vo2.setTag(vo.getTag());
                try {
                    vo2.setIcon(ImageBase64Util.decodeBase64ToDrawable(vo.getIcon(), context));
                } catch (Exception ex) {
                    Log.e(TAG, "loadAllAppListFromDB ERR : " + ex.getMessage(), ex);
                }
                appendTagToLst(vo.getTag(), tagLst);
                lst.add(vo2);
            }
            return lst;
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
            mIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);//不work
            mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//不work
            mIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//不work
            mIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);//不work
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//不work
            context.startActivity(mIntent);
        }
    }
}
