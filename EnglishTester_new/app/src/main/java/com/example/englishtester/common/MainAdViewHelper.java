package com.example.englishtester.common;

import android.content.Context;
import com.example.englishtester.common.Log;

import com.example.englishtester.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

/**
 * Created by gtu001 on 2017/6/29.
 */

public class MainAdViewHelper {

    private static final String TAG = MainAdViewHelper.class.getSimpleName();
    private static final MainAdViewHelper _INST = new MainAdViewHelper();
    private MainAdViewHelper(){
    }

    public static MainAdViewHelper getInstance(){
        return _INST;
    }

    /**
     * 載入廣告
     */
    public void initAdView(NativeExpressAdView mAdView, Context context) {
        try{
            MobileAds.initialize(context, context.getString(R.string.ad_app_id));
        }catch(Throwable ex){
            Log.e(TAG, "handler ignore error by gtu001 : " + ex.getMessage(), ex);
        }


        // Create a banner ad. The ad size and ad unit ID must be set before calling loadAd.
//        mAdView = (NativeExpressAdView) findViewById(R.id.adView);

        //mAdView.setAdSize(AdSize.BANNER);//SMART_BANNER
        //mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));
//        mAdView.setAdUnitId("ca-app-pub-3940256099942544/2177258514");//for Test

        // Set its video options.
        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());

        // The VideoController can be used to get lifecycle events and info about an ad's video
        // asset. One will always be returned by getVideoController, even if the ad has no video
        // asset.
        final VideoController mVideoController = mAdView.getVideoController();
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                Log.d(TAG, "Video playback is finished.");
                super.onVideoEnd();
            }
        });

        // Set an AdListener for the AdView, so the Activity can take action when an ad has finished
        // loading.
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                    Log.d(TAG, "Received an ad that contains a video asset.");
                } else {
                    Log.d(TAG, "Received an ad that does not contain a video asset.");
                }
            }
        });

        // Optionally populate the ad request builder.
        // Start loading the ad.
        mAdView.loadAd(AdMobHelper.getAdRequest(context));

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                String msg = "廣告載入onAdLoaded";
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.w(TAG, msg);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                String errMsg = "廣告載入錯誤 : " + String.format("Ad failed to load with error code %d.", errorCode);
//                Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, errMsg);
            }

            @Override
            public void onAdOpened() {
                String msg = "廣告載入onAdOpened";
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.w(TAG, msg);
            }

            @Override
            public void onAdClosed() {
                String msg = "廣告載入onAdClosed";
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.w(TAG, msg);
            }

            @Override
            public void onAdLeftApplication() {
                String msg = "廣告載入onAdLeftApplication";
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.w(TAG, msg);
            }
        });
    }
}
