package com.example.gtuandroid;

import com.example.gtuandroid.sub.FragmentTest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FragmentTestActivity extends FragmentTest0Activity {
    private Button backBtn, nextBtn;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test1);
        backBtn = (Button) findViewById(R.id.back_button);
        nextBtn = (Button) findViewById(R.id.next_button);
        changeFragment(FragmentTest.newInstance(page));
        
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page > 1) {
                    changeFragment(FragmentTest.newInstance(--page));
                } else {
                    changeFragment(FragmentTest.newInstance(page));
                }
            }
        });
        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(FragmentTest.newInstance(++page));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void changeFragment(Fragment f) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f);
        transaction.commitAllowingStateLoss();
    }
}