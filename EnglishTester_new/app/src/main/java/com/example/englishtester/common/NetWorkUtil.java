package com.example.englishtester.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gtu001 on 2017/5/29.
 */

public class NetWorkUtil {

    /**
     * 判斷網路是否連接 true 為可連 , false 為不可連
     */
    public static boolean connectionTest(Context context) {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            result = false;
        } else {
            if (!info.isAvailable()) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }
}
