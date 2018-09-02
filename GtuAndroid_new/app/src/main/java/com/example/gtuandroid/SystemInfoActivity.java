package com.example.gtuandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;

import dalvik.system.DexClassLoader;

public class SystemInfoActivity extends Activity {
    private static final String TAG = SystemInfoActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        back();

        screenSize();
        changeLanguage();
        checkSupport();

        Resources resources = getBaseContext().getResources();
        Drawable drawable = resources.getDrawable(android.R.drawable.btn_default);
        String titleActivityMain = getString(R.string.title_activity_main);
    }

    /**
     * 取得應用程式版本
     */
    private void getSystemVersion(){
        PackageManager pm = getPackageManager();
        int versionCode = 0;
        String versionName = "";
        try{
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;//AndroidManifest.xml android:versionCode
            versionName = packageInfo.versionName;//AndroidManifest.xml android:versionName
        }catch(Exception ex){
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    /**
     * 取得所安裝的應用程式總攬
     */
    private List<String> getInstalledApplaction(){
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> appInfoList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> currentAppList = new ArrayList<String>();
        for(ApplicationInfo appInfo : appInfoList){
            String appLabel = pm.getApplicationLabel(appInfo).toString();
            currentAppList.add(appLabel);
        }
        return currentAppList;
    }

    /**
     * 取得應用程式的對應動作總攬
     */
    private void getApplicationReflectMove(){
        PackageManager pm = getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    private void windowConfig() {
        // 無通知欄
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 無標題欄
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 直式橫式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void checkSupport() {
        if (getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.GINGERBREAD) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SystemInfoActivity.this);
            alert.setMessage("targetSdkVersion is " + getApplicationInfo().targetSdkVersion + " , need is " + Build.VERSION_CODES.GINGERBREAD)//
                    .setNeutralButton("exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            finish();
                        }
                    }).create();
            alert.show();
        }
    }

    private void googleSupport() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        String message = GooglePlayServicesUtil.getErrorString(result);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void changeLanguage() {
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = Locale.US;
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(conf, dm);
    }

    private void screenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        System.out.println("手機解析度 : " + dm.widthPixels + " x " + dm.heightPixels);
    }

    /**
     * 判斷是否聯網
     */
    private boolean isConnect() {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            result = false;
        } else {
            result = info.isAvailable();
        }
        return result;
    }

    private void keyboardTest(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 1.切換顯示軟鍵盤
        // 這個效果是:如果有軟鍵盤,那麼隱藏它;反之,把它顯示出來。代碼方法如下:
        // 調用toggleSoftInput方法,實現切換顯示軟鍵盤的功能。
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        // 2.顯示軟鍵盤
        // 調用showSoftInput方法顯示軟鍵盤,其中view為聚焦的view組件
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        // 3.隱藏軟鍵盤
        // 調用hideSoftInputFromWindow方法隱藏軟鍵盤
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隱藏鍵盤
        IBinder mIBinder = SystemInfoActivity.this.getCurrentFocus().getWindowToken();
        imm.hideSoftInputFromWindow(mIBinder, InputMethodManager.HIDE_NOT_ALWAYS);

        // 4.獲取輸入法打開的狀態
        // 獲取狀態信息
        boolean isOpen = imm.isActive();// isOpen若返回true,則表示輸入法打開

        final View v = getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            imm.toggleSoftInputFromWindow(v.getWindowToken(), 0, 0);
        }

        //隱藏鍵盤
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 讀取外部jar檔
     */
    private void addAndroidJar(Context context) throws ClassNotFoundException {
        String jarFile = "path/to/jarfile.jar";
        DexClassLoader classLoader = new DexClassLoader(jarFile, //
                "/data/data/" + context.getPackageName() + "/", null, getClass().getClassLoader());
        Class<?> myClass = classLoader.loadClass("MyClass");
    }

    //指定圖片大小
    private void indicateImageSize(){
        ImageView imageView = new ImageView(this);
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, android.R.drawable.btn_star_big_on);
        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, 200, 90, false);
        imageView.setImageBitmap(bitmap2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SystemInfoActivity.this.setResult(RESULT_CANCELED, SystemInfoActivity.this.getIntent());
                SystemInfoActivity.this.finish();
            }
        });
    }
}
