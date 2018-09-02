package com.example.gtuandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.gtuandroid.component.FloatViewService;

public class FloatViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout layout = createContentView();

        Button start = new Button(this);
        start.setText("start");
        layout.addView(start);
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FloatViewActivity.this, FloatViewService.class);
                // 启动FxService
                startService(intent);
                finish();
            }
        });

        Button remove = new Button(this);
        remove.setText("remove");
        layout.addView(remove);
        remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // uninstallApp("com.phicomm.hu");
                Intent intent = new Intent(FloatViewActivity.this, FloatViewService.class);
                // 终止FxService
                stopService(intent);
            }
        });
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
