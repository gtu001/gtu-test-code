package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;

public class ViewGroupTestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewgroup_test);
    }
}
