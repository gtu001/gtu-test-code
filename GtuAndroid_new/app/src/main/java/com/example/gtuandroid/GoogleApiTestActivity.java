package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

public class GoogleApiTestActivity extends Activity {

    private static final String TAG = GoogleApiTestActivity.class.getSimpleName();

     private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout contentView = createContentView();

        Button button1 = new Button(this);
        contentView.addView(button1);
        button1.setText("測試");
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }

    private void init() {
         mGoogleApiClient = new
         GoogleApiClient.Builder(GoogleApiTestActivity.this)//
         .addApi(Drive.API)//
         .addScope(Drive.SCOPE_FILE)//
         .build();//

         mGoogleApiClient.connect();

         Log.v(TAG, "connected : " + mGoogleApiClient.isConnected());
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
}
