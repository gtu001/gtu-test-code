package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CountdownTimerActivity extends Activity {
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        back();
        new CountDownTimer(30000, 1000) {
            @Override
            public void onFinish() {
                mTextView.setText("Done!");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                mTextView.setText("seconds remaining:" + millisUntilFinished / 1000);
            }
        }.start();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                CountdownTimerActivity.this.setResult(RESULT_CANCELED, CountdownTimerActivity.this.getIntent());
                CountdownTimerActivity.this.finish();
            }
        });
    }
}