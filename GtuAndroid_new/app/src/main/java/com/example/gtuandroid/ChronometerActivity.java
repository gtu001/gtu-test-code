package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;

public class ChronometerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        back();

        final Button button1 = (Button) findViewById(R.id.button1);
        final Chronometer chronometer1 = (Chronometer) findViewById(R.id.chronometer1);

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (button1.getText().equals("start")) {
                    chronometer1.start();
                    button1.setText("stop");
                } else {
                    chronometer1.stop();
                    button1.setText("start");
                }
            }
        });
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ChronometerActivity.this.setResult(RESULT_CANCELED, ChronometerActivity.this.getIntent());
                ChronometerActivity.this.finish();
            }
        });
    }
}
