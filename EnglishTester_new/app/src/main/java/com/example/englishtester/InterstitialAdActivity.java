package com.example.englishtester;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.englishtester.common.Log;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.englishtester.common.AdMobHelper;
import com.example.englishtester.common.InterstitialAdHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

public class InterstitialAdActivity extends Activity {

    private static final String TAG = InterstitialAdActivity.class.getSimpleName();

    InterstitialAd mInterstitialAd = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

//        setTheme(R.style.Transparent2);

        LinearLayout layout =  createContentView();

//        TextView text = new TextView(this);
//        text.setText("讀取中...");
//        layout.addView(text);

        initAdView();
    }

    /**
     * 載入廣告
     */
    public void initAdView() {
        Context context = InterstitialAdActivity.this;

        MobileAds.initialize(context, context.getString(R.string.ad_app_id));

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.ad_unit_id_FloatService));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "###### onAdLoaded");
                mInterstitialAd.show();
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
                finish();
            }
        });

        mInterstitialAd.loadAd(AdMobHelper.getAdRequest(context));
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }

    public static void startThisActivity(Context context){
        Intent i = new Intent();
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.setClass(context, InterstitialAdActivity.class);
        context.startActivity(i);
    }
}
