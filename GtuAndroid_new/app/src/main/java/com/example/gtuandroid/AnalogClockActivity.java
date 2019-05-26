package com.example.gtuandroid;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.TextView;

public class AnalogClockActivity extends Activity {

    static final int GUINOTIFIER = 0x1234;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog_clock);
        back();

        AnalogClock analogClock1 = (AnalogClock) findViewById(R.id.analogClock1);
        final TextView textView1 = (TextView) findViewById(R.id.textView1);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case GUINOTIFIER:
                    int hour = msg.getData().getInt("hour");
                    int minute = msg.getData().getInt("minute");
                    int second = msg.getData().getInt("second");
                    textView1.setText(hour + ":" + minute + ":" + second);
                    break;
                }
                System.out.println("msg.what = " + msg.what);
                super.handleMessage(msg);
            }
        };

        Thread mClockThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                try {
                    Calendar mcal = Calendar.getInstance();
                    do {
                        long time = System.currentTimeMillis();
                        mcal.setTimeInMillis(time);
                        int hour = mcal.get(Calendar.HOUR);
                        int minute = mcal.get(Calendar.MINUTE);
                        int second = mcal.get(Calendar.SECOND);

                        Thread.sleep(1000);
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("hour", hour);
                        bundle.putInt("minute", minute);
                        bundle.putInt("second", second);
                        message.setData(bundle);
                        message.what = AnalogClockActivity.GUINOTIFIER;
                        handler.sendMessage(message);
                    } while (true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, "ClockThread..");

        mClockThread.start();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                AnalogClockActivity.this.setResult(RESULT_CANCELED, AnalogClockActivity.this.getIntent());
                AnalogClockActivity.this.finish();
            }
        });
    }
}
