package com.example.englishtester;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.common.SharedPreferencesUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

public class DropboxApplicationActivity extends Activity {

    private static final String TAG = DropboxApplicationActivity.class.getSimpleName();

    ViewPager myViewPager;
    EditText accessTokenText;//accessToken編輯方框
    Button accessTokenBtn;//accessToken啟用按鈕

    public static final String DROPBOX_REF_KEY = DropboxApplicationActivity.class.getSimpleName() + "_REFKEY";
    public static final String DROPBOX_BUNDLE_TOKEN_KEY = DropboxApplicationActivity.class.getSimpleName() + "_TokenKEY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout contentView = createContentView();

        contentView.addView(createButton("Dropbox App申請教學", new OnClickListener() {
            @Override
            public void onClick(View v) {
                SlidePageInstructionActivity.callInstrcutionActivity(DropboxApplicationActivity.this, getDropAppApplicatioinImageList(), false);
            }
        }));

        contentView.addView(createLabel("申請Dropbox App取得Access token"));
        contentView.addView(createButton("前往申請App", new OnClickListener() {
            @Override
            public void onClick(View v) {
                //申請前先停掉懸浮字典
                stopThisService();

                //webView.loadUrl("https://www.dropbox.com/developers/apps/create");
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.dropbox.com/developers/apps/create")));
            }
        }));

        //accesstoken 輸入框
        contentView.addView(createLabel("輸入Access token字串"));
        accessTokenText = new EditText(this);
        contentView.addView(accessTokenText);
        accessTokenText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                accessTokenBtn.setEnabled(true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //accesstoken 啟用按鈕
        accessTokenBtn = new Button(this);
        accessTokenBtn.setText("啟用Token");
        contentView.addView(accessTokenBtn);
        afterAccessTokenBtnClick();
        accessTokenBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String token = accessTokenText.getText().toString();
                if (StringUtils.isBlank(token)) {
                    Toast.makeText(DropboxApplicationActivity.this, "未輸入access token!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean result = DropboxEnglishService.isAccessTokenWork(token);
                if (result) {
                    Toast.makeText(DropboxApplicationActivity.this, "已設定access token!", Toast.LENGTH_SHORT).show();
                    setDropboxAccessToken(DropboxApplicationActivity.this, token);
                    afterAccessTokenBtnClick();
                } else {
                    setDropboxAccessToken(DropboxApplicationActivity.this, "");
                    Toast.makeText(DropboxApplicationActivity.this, "無效的access token!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        contentView.addView(createButton("本app應用說明", new OnClickListener() {
            @Override
            public void onClick(View v) {
                SlidePageInstructionActivity.callInstrcutionActivity(DropboxApplicationActivity.this, getDropAppUsageImageList(), false);
            }
        }));
    }

    /**
     * 若accessToken啟用鎖住按鈕
     */
    private void afterAccessTokenBtnClick(){
        String accessTokenStr = getDropboxAccessToken(this);
        if (StringUtils.isNotBlank(accessTokenStr)) {
            accessTokenText.setText(accessTokenStr);
            accessTokenBtn.setEnabled(false);
            accessTokenBtn.setText("token啟用中");
        }
    }

    /**
     * 申請accesstoken教學
     */
    private ArrayList<Integer> getDropAppApplicatioinImageList() {
        ArrayList<Integer> fList = new ArrayList<Integer>();
        fList.add(R.drawable.dropbox_create_step1);
        fList.add(R.drawable.dropbox_create_step2);
        fList.add(R.drawable.dropbox_create_step3);
        fList.add(R.drawable.dropbox_create_step4);
        fList.add(R.drawable.dropbox_create_step5);
        return fList;
    }

    /**
     * dropbox使用對應教學
     */
    private ArrayList<Integer> getDropAppUsageImageList() {
        ArrayList<Integer> fList = new ArrayList<Integer>();
        fList.add(R.drawable.dropbox_create_use_step1);
        fList.add(R.drawable.dropbox_create_use_step2);
        fList.add(R.drawable.dropbox_create_use_step3);
        fList.add(R.drawable.dropbox_create_use_step4);
        return fList;
    }

    /**
     * 取得Dropbox AccessToken
     */
    public static String getDropboxAccessToken(Context context) {
        String token = SharedPreferencesUtil.getData(context, //
                DROPBOX_REF_KEY, DROPBOX_BUNDLE_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            token = Constant.getAppConfig().getProperty(Constant.APPCONFIG__DROPBOX_TOKEN_KEY);
            if(StringUtils.isBlank(token)){
                Toast.makeText(context, "尚未申請Dropbox Access Token!", Toast.LENGTH_SHORT).show();
            }
        }
        return token;
    }

    /**
     * 設定Dropbox AccessToken
     */
    public static void setDropboxAccessToken(ContextWrapper context, String token) {
        SharedPreferencesUtil.putData(context, //
                DROPBOX_REF_KEY, DROPBOX_BUNDLE_TOKEN_KEY, token);
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
    public void stopThisService(){
        // uninstallApp("com.phicomm.hu");
        Intent intent = new Intent(this, FloatViewService.class);
        // 终止FxService
        stopService(intent);
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
