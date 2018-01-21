package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class TestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView1 = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.back);
        button.setText("Notification");

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = Locale.getDefault();

                Locale.setDefault(Locale.CHINA);
                String testMsg = getString(R.string.hello_world);
                Toast.makeText(TestActivity.this, locale + " - " + testMsg, Toast.LENGTH_SHORT).show();
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

    private void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                TestActivity instance = TestActivity.this;
                instance.setResult(RESULT_CANCELED, instance.getIntent());
                instance.finish();
            }
        });
    }
}
