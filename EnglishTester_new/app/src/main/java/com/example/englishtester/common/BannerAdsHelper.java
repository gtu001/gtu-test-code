package com.example.englishtester.common;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.englishtester.MainActivity;
import com.example.englishtester.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by gtu001 on 2017/10/15.
 */

public class BannerAdsHelper {

    public static void initBannerAds(int unitId, AdSize adsize, LinearLayout contentView, final Context context) {
        MobileAds.initialize(context, context.getString(R.string.ad_app_id));
        com.google.android.gms.ads.AdView adview = new com.google.android.gms.ads.AdView(context);
        adview.setAdSize(adsize);
        adview.setAdUnitId(context.getString(unitId));
        adview.loadAd(AdMobHelper.getAdRequest(context));
        adview.setAdListener(new AdListener(){
            public void onAdFailedToLoad(int var1) {
                Toast.makeText(context, "ads載入失敗!", Toast.LENGTH_SHORT).show();
            }
        });
        contentView.addView(adview, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定
    }
}
