package com.example.englishtester.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.example.englishtester.common.Log;

import com.example.englishtester.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by gtu001 on 2017/6/29.
 */
public class InterstitialAdHelper {

    private static final String TAG = "Ads_" + InterstitialAdHelper.class.getSimpleName();

    InterstitialAd mInterstitialAd = null;
    Context context = null;

    private InterstitialAdHelper(Context context) {
        this.context = context;
    }

    public static InterstitialAdHelper newInstance(final Context context) {
        InterstitialAdHelper t = new InterstitialAdHelper(context);
        t.initAdView();
        return t;
    }

    public boolean showAd() {
        Log.v(TAG, "##### SHOW_AD");
        boolean isShow = false;
        if (mInterstitialAd.isLoaded()) {
            try {
                mInterstitialAd.show();
                isShow = true;
                Log.d(TAG, "##### show success!");
            } catch (Exception ex) {
                Log.e(TAG, "##### show fail!", ex);
            }
            //重整admob
            //initAdView();
        } else {
            Log.d(TAG, "##### The interstitial wasn't loaded yet.");
        }
        return isShow;
    }

    /**
     * 載入廣告
     */
    public void initAdView() {
        MobileAds.initialize(context, context.getString(R.string.ad_app_id));

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.ad_unit_id_FloatService));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "###### onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "###### onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                Log.i(TAG, "##### onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.i(TAG, "##### onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.i(TAG, "##### onAdClosed");
                super.onAdClosed();
                loadAd();
            }
        });

        loadAd();
    }

    private void loadAd() {
        mInterstitialAd.loadAd(AdMobHelper.getAdRequest(context));
    }
}
