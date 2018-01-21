package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.gtuandroid.bean.MyApp;
import com.example.gtuandroid.bean.Person;

public class MyAppTest2NextActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApp m = (MyApp)getApplication();
        Person p = m.getPerson();
        Log.e("MyAppTest2NextActivity person",p.getName());
        Log.e("MyAppTest2NextActivity person",Integer.toString(p.getAge()));
        
        back();
        
        TextView text = (TextView)findViewById(R.id.text);
        text.setText("共用資料 : " + p.getName() + "/" + p.getAge());
    }
    
    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                MyAppTest2NextActivity.this.setResult(RESULT_CANCELED, MyAppTest2NextActivity.this.getIntent());
                MyAppTest2NextActivity.this.finish();
            }
        });
    }
}
