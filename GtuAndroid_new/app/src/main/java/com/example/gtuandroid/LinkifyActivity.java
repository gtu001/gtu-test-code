package com.example.gtuandroid;

import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LinkifyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, //設定與螢幕同寬
                LayoutParams.WRAP_CONTENT));//高度隨內容而定
        
        setContentView(scroll);
        
        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        TextView textView3 = new TextView(this);
        TextView textView4 = new TextView(this);
        TextView textView5 = new TextView(this);
        
        layout.addView(textView1);
        layout.addView(textView2);
        layout.addView(textView3);
        layout.addView(textView4);
        layout.addView(textView5);
        
        textView1.setText("http://www.google.com");
        Linkify.addLinks(textView1, Linkify.WEB_URLS);
        
        textView2.setText("5552323233");
        Linkify.addLinks(textView2, Linkify.PHONE_NUMBERS);
        
        textView3.setText("1 Dizengoff, Tel Aviv, IL");
        Linkify.addLinks(textView3, Linkify.MAP_ADDRESSES);
        
        textView4.setText("aviyehuda@gmail.com");
        Linkify.addLinks(textView4, Linkify.EMAIL_ADDRESSES);
        
        textView5.setText("press Linkify& or on Android& to search it on google");
        Pattern pattern = Pattern.compile("[a-zA-Z]+&");
        Linkify.addLinks(textView5, pattern, "http://www.google.com/search?q=");
    }
}
