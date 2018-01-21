package com.example.gtuandroid;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class StringResourceUsageActivity extends Activity {
    
    private static final String TAG = StringResourceUsageActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = createContentView();

        String username = "Hevin";
        String welcome = getString(R.string.login_welcome_back, username);
        Log.v(TAG, welcome);
        // result: Hevin 欢迎回来

        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        String text = getResources().getQuantityString(R.plurals.minutes, minutes);
        Log.v(TAG, text);

        TextView tv = new TextView(this);
        layout.addView(tv);
        tv.setText(Html.fromHtml(getString(R.string.html_text)));
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
