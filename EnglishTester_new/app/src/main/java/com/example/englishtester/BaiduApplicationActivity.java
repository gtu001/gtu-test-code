package com.example.englishtester;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.englishtester.common.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.translate.demo.TransApi;
import com.example.englishtester.common.SharedPreferencesUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Callable;

public class BaiduApplicationActivity extends Activity {
    private static final String TAG = BaiduApplicationActivity.class.getSimpleName();

    ViewPager myViewPager;
    EditText appClientIdText;//AppId編輯方框
    EditText appClientSecretText;//密鑰編輯方框
    Button apiClientBtn;//accessToken啟用按鈕

    public static final String BAIDU_REF_KEY = BaiduApplicationActivity.class.getSimpleName() + "_REFKEY";
    public static final String BAIDU_BUNDLE_APPID_KEY = BaiduApplicationActivity.class.getSimpleName() + "_APPID_KEY";
    public static final String BAIDU_BUNDLE_SECRET_KEY = BaiduApplicationActivity.class.getSimpleName() + "_SECRET_KEY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout contentView = createContentView();

        contentView.addView(createButton("百度Api申請教學", new OnClickListener() {
            @Override
            public void onClick(View v) {
                SlidePageInstructionActivity.callInstrcutionActivity(BaiduApplicationActivity.this, getBaiduAppApplicatioinImageList(), false);
            }
        }));

        contentView.addView(createLabel("申請百度Api"));
        contentView.addView(createButton("前往申請App", new OnClickListener() {
            @Override
            public void onClick(View v) {
                //申請前先停掉懸浮字典
                stopThisService();

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://api.fanyi.baidu.com/api/trans/product/index")));
            }
        }));

        //accesstoken 輸入框
        contentView.addView(createLabel("輸入APP ID與密鑰"));
        appClientIdText = new EditText(this);
        appClientIdText.setHint("輸入APP ID");
        contentView.addView(appClientIdText);
        appClientIdText.addTextChangedListener(createTextWatcher());

        appClientSecretText = new EditText(this);
        appClientSecretText.setHint("輸入密鑰");
        contentView.addView(appClientSecretText);
        appClientSecretText.addTextChangedListener(createTextWatcher());

        //accesstoken 啟用按鈕
        apiClientBtn = new Button(this);
        apiClientBtn.setText("啟用Api");
        contentView.addView(apiClientBtn);
        afterApiClientBtnClick();
        apiClientBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotBlank(appClientIdText.getText().toString()) && //
                        StringUtils.isNotBlank(appClientSecretText.getText().toString())) {
                    String appId = appClientIdText.getText().toString();
                    String appSecret = appClientSecretText.getText().toString();
                    boolean isAppIdWork = isAppSecretWork(appId, appSecret);
                    if (isAppIdWork) {
                        setBaiduAppId(BaiduApplicationActivity.this, appId);
                        setBaiduSecret(BaiduApplicationActivity.this, appSecret);
                        Toast.makeText(BaiduApplicationActivity.this, "啟用成功!", Toast.LENGTH_SHORT).show();
                        afterApiClientBtnClick();
                    } else {
                        setBaiduAppId(BaiduApplicationActivity.this, "");
                        setBaiduSecret(BaiduApplicationActivity.this, "");
                        Toast.makeText(BaiduApplicationActivity.this, "啟用失敗!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //設定預設值
        afterApiClientBtnClick();
    }


    /**
     * 申請百度Api
     */
    private ArrayList<Integer> getBaiduAppApplicatioinImageList() {
        ArrayList<Integer> fList = new ArrayList<Integer>();
        fList.add(R.drawable.baidu_translate_01);
        fList.add(R.drawable.baidu_translate_02);
        fList.add(R.drawable.baidu_translate_03);
        return fList;
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (StringUtils.isNotBlank(appClientIdText.getText().toString()) && //
                        StringUtils.isNotBlank(appClientSecretText.getText().toString())) {
                    apiClientBtn.setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
    }

    /**
     * 若accessToken啟用鎖住按鈕
     */
    private void afterApiClientBtnClick() {
        String baiduAppId = getBaiduAppId(this);
        String baiduSecret = getBaiduSecret(this);
        if (StringUtils.isNotBlank(baiduAppId) && StringUtils.isNotBlank(baiduSecret)) {
            appClientIdText.setText(baiduAppId);
            appClientSecretText.setText(baiduSecret);
            apiClientBtn.setEnabled(false);
            apiClientBtn.setText("Api啟用中");
        } else {
            apiClientBtn.setEnabled(true);
        }
    }

    /**
     * 取得AppID
     */
    public static String getBaiduAppId(ContextWrapper context) {
        String token = SharedPreferencesUtil.getData(context, //
                BAIDU_REF_KEY, BAIDU_BUNDLE_APPID_KEY);
        if (StringUtils.isBlank(token)) {
            token = Constant.getAppConfig().getProperty(Constant.APPCONFIG__BAIDU_APPID_KEY);
        }
        return token;
    }

    /**
     * 設定AppID
     */
    public static void setBaiduAppId(ContextWrapper context, String token) {
        SharedPreferencesUtil.putData(context, //
                BAIDU_REF_KEY, BAIDU_BUNDLE_APPID_KEY, token);
    }

    /**
     * 取得AppID
     */
    public static String getBaiduSecret(ContextWrapper context) {
        String token = SharedPreferencesUtil.getData(context, //
                BAIDU_REF_KEY, BAIDU_BUNDLE_SECRET_KEY);
        if (StringUtils.isBlank(token)) {
            token = Constant.getAppConfig().getProperty(Constant.APPCONFIG__BAIDU_APPSECRET_KEY);
        }
        return token;
    }

    /**
     * 設定AppID
     */
    public static void setBaiduSecret(ContextWrapper context, String token) {
        SharedPreferencesUtil.putData(context, //
                BAIDU_REF_KEY, BAIDU_BUNDLE_SECRET_KEY, token);
    }

    private ImageView createImageView(int resId) {
        ImageView v = new ImageView(this);
        v.setImageDrawable(getResources().getDrawable(resId));
        return v;
    }

    private Button createButton(String text, OnClickListener listener) {
        Button b = new Button(this);
        b.setText(text);
        b.setOnClickListener(listener);
        return b;
    }

    private TextView createLabel(String message) {
        TextView v = new TextView(this);
        v.setText(message);
        return v;
    }

    /**
     * 停掉懸浮字典服務
     */
    public void stopThisService() {
        // uninstallApp("com.phicomm.hu");
        Intent intent = new Intent(this, FloatViewService.class);
        // 终止FxService
        stopService(intent);
    }

    /**
     * 判斷百度appId, securityKey是否正確
     */
    public static boolean isAppSecretWork(String appId, String securityKey) {
        final TransApi api = new TransApi(appId, securityKey);
        final Handler handler = new Handler();
        return DropboxEnglishService.getRunOnUiThread((new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    String content = api.getTransResult("", "en", "cht");
                    Log.v(TAG, "---" + content);
//                        {"error_code":"54000","error_msg":"PARAM_FROM_TO_OR_Q_EMPTY","from":"en","to":"cht","monLang":"zh","query":""}
                    final JSONObject obj = new JSONObject(content);
                    if ("PARAM_FROM_TO_OR_Q_EMPTY".equals(obj.getString("error_msg"))) {
                        Log.v(TAG, "appId, secret正確!");
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.e(TAG, "ERROR : " + e.getMessage(), e);
                    return false;
                }
            }
        }), -1L);
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        //讓layout吃掉focus
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        //layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        return layout;
    }
}
