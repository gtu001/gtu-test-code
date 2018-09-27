package com.example.englishtester.common;

import android.content.Context;
import android.provider.Settings;
import com.example.englishtester.common.Log;
import android.widget.Toast;

import com.example.englishtester.BuildConfig;
import com.example.englishtester.R;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;

/**
 * Created by gtu001 on 2017/5/31.
 */
public class AdMobHelper {

    private static final String TAG = "Ads_" + AdMobHelper.class.getSimpleName();

    public static AdLoader.Builder getAdLoaderBuilder(Context context) {
        String AD_UNIT_ID = context.getString(R.string.ad_unit_id_Main);
        if (BuildConfig.DEBUG) {
            AD_UNIT_ID = "ca-app-pub-3940256099942544/3986624511";//測試用
            Log.e(TAG, "Is debug ad unit id! " + AD_UNIT_ID);
        } else {
            Log.e(TAG, "Is release ad unit id! " + AD_UNIT_ID);
        }
        return new AdLoader.Builder(context, AD_UNIT_ID);
    }

    public static AdRequest getAdRequest(Context context) {
        AdRequest request;
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "Is debug AdRequest!");
//            String ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//            Log.w(TAG, "androidId - " + ANDROID_ID);
            request = new AdRequest.Builder()//
                    .addTestDevice("1F1C5D0E8ABC54FD716053C9385C49A4")//
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)//
//                    .addTestDevice(ANDROID_ID)//
                    .build();
        } else {
            Log.e(TAG, "Is release AdRequest!");
            request = new AdRequest.Builder().build();
        }
        showIsTestMode(request, context);
        return request;
    }

    /**
     * 判斷是否為測試模式
     */
    private static void showIsTestMode(AdRequest request, Context context){
        boolean isTestMode = request.isTestDevice(context);
        String testModeMsg = "是否為測試模式 = " + isTestMode;
        if(isTestMode){
            Toast.makeText(context, testModeMsg, Toast.LENGTH_SHORT).show();
        }
        Log.e(TAG, testModeMsg);
    }
}