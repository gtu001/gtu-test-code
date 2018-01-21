package com.example.englishtester.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.example.englishtester.BuildConfig;

/**
 * Created by gtu001 on 2017/5/29.
 */

public class FloatViewChecker {

    /**
     * 判斷權限是否working
     */
    public static boolean isPermissionOk(Context context) {
        boolean needApply = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申請權限
     */
    public static void applyPermission(Context context, int requestCode) {
        boolean needApply = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                if(context instanceof Activity){
                    ((Activity)context).startActivityForResult(intent, requestCode);
                }else{
                    context.startActivity(intent);
                }
                needApply = true;
            }
        }
        if (!needApply) {
            Toast.makeText(context, "不需要此權限, 或權限已開啟!", Toast.LENGTH_SHORT).show();
        }
    }
}
