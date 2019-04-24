package com.example.englishtester;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.example.englishtester.common.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.englishtester.common.FloatServiceHolderBroadcastReceiver;
import com.example.englishtester.common.FloatViewChecker;
import com.example.englishtester.common.InterstitialAdHelper;
import com.example.englishtester.common.MainAdViewHelper;
import com.example.englishtester.common.ServiceUtil;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;

public class FloatViewActivity extends Activity {

    private static final String TAG = FloatViewActivity.class.getSimpleName();

    private Button startBtn;
    private NativeExpressAdView mAdView;

    public static final int FLOATVIEW_REQUESTCODE = 1234;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

//        LinearLayout layout = createContentView();
        setContentView(R.layout.activity_main);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout2);
        mAdView = (NativeExpressAdView) findViewById(R.id.adView);

        //初始化廣告框
        MainAdViewHelper.getInstance().initAdView(mAdView, this);

        layout.addView(createLabel(""));
        layout.addView(createButton("懸浮字典使用教學", new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> imageList = new ArrayList<Integer>();
                imageList.add(R.drawable.float_dictionary_step_01);
                imageList.add(R.drawable.float_dictionary_step_02);
                imageList.add(R.drawable.float_dictionary_step_03);
                imageList.add(R.drawable.float_dictionary_step_04);
                imageList.add(R.drawable.float_dictionary_step_05);
                imageList.add(R.drawable.float_dictionary_step_06);
                imageList.add(R.drawable.float_dictionary_step_07);
                imageList.add(R.drawable.float_dictionary_step_08);
                imageList.add(R.drawable.float_dictionary_step_09);
                imageList.add(R.drawable.float_dictionary_step_10);
                imageList.add(R.drawable.float_dictionary_step_11);
                imageList.add(R.drawable.float_dictionary_step_12);
                SlidePageInstructionActivity.callInstrcutionActivity(FloatViewActivity.this, imageList, true);
            }
        }));

        layout.addView(createLabel(""));
        layout.addView(createButton("申請開啟懸浮視窗權限", new OnClickListener() {
            @Override
            public void onClick(View v) {
                //申請開啟懸浮視窗權限
                FloatViewChecker.applyPermission(FloatViewActivity.this, FLOATVIEW_REQUESTCODE);
            }
        }));

        layout.addView(createLabel(""));
        startBtn = createButton("開啟懸浮字典", new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FloatViewChecker.isPermissionOk(FloatViewActivity.this)) {
                    //申請開啟懸浮視窗權限
                    FloatViewChecker.applyPermission(FloatViewActivity.this, FLOATVIEW_REQUESTCODE);
                } else {
                    startStopService(true);
                }
            }
        }, R.drawable.answer_button_lightblue);
        layout.addView(startBtn);
        layout.addView(createLabel(""));

        //修正案紐文字
        fixButtonText();
    }

    /**
     * 開啟/停止 服務
     */
    private void startStopService(boolean isStart) {
        boolean isRunning = ServiceUtil.isServiceRunning(this, FloatViewService.class);
        if (!isRunning && isStart) {
            Intent intent = new Intent(FloatViewActivity.this, FloatViewService.class);
            // 启动FxService
            startService(intent);
            finish();

            FloatServiceHolderBroadcastReceiver.setFloatViewServiceEnable(true);
        } else {
            // uninstallApp("com.phicomm.hu");
            Intent intent = new Intent(FloatViewActivity.this, FloatViewService.class);
            // 终止FxService
            stopService(intent);

            FloatServiceHolderBroadcastReceiver.setFloatViewServiceEnable(false);
        }
        fixButtonText();
    }

    /**
     * 建立標籤
     */
    private TextView createLabel(String message) {
        TextView v = new TextView(this);
        v.setText(message);
        return v;
    }

    /**
     * 建立Btn
     */
    private Button createButton(String text, OnClickListener onClickListener) {
        return createButton(text, onClickListener, null);
    }

    private Button createButton(String text, OnClickListener listener, Integer backGroundId) {
        Button b = new Button(this);
        b.setText(text);
        b.setOnClickListener(listener);
        if (backGroundId == null) {
            backGroundId = R.drawable.answer_button_yellow;
        }
//        b.setBackground(getResources().getDrawable(backGroundId));
        b.setBackgroundResource(backGroundId);
        b.setPadding(5, 5, 5, 5);
        return b;
    }

    /**
     * 修正按鈕文字
     */
    private void fixButtonText() {
        boolean isRunning = ServiceUtil.isServiceRunning(this, FloatViewService.class);
        startBtn.setText((isRunning ? "關閉" : "開啟") + "即時字典");
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "# onActivityResult");
        if (requestCode == FLOATVIEW_REQUESTCODE) {
            if (!FloatViewChecker.isPermissionOk(FloatViewActivity.this)) {
                //申請開啟懸浮視窗權限
                FloatViewChecker.applyPermission(FloatViewActivity.this, FLOATVIEW_REQUESTCODE);
            } else {
                startStopService(true);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // Resume the AdView.
        mAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        mAdView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        mAdView.destroy();
        super.onDestroy();
    }
}
