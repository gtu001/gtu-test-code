package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProgressBarActivity extends Activity {

    static final int GUI_STOP_NOTIFIER = 0x108;
    static final int GUI_THREADING_NOTIFIER = 0x109;

    int intCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);// for progressBar2
        setProgressBarVisibility(true);// for progressBar2
        setContentView(R.layout.activity_progressbar);
        back();

        final TextView textView1 = (TextView) findViewById(R.id.textView1);
        Button button1 = (Button) findViewById(R.id.button1);
        final ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        final ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        progressBar1.setIndeterminate(false);

        final Handler myMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case GUI_STOP_NOTIFIER:
                    System.out.println("GUI_STOP_NOTIFIER : " + Thread.currentThread().isInterrupted());
                    textView1.setText("執行完畢..");
                    progressBar1.setVisibility(View.GONE);
                    Thread.currentThread().interrupt();
                    break;
                case GUI_THREADING_NOTIFIER:
                    System.out.println("GUI_THREADING_NOTIFIER : " + Thread.currentThread().isInterrupted());
                    //if (!Thread.currentThread().isInterrupted()) {
                    progressBar1.setProgress(intCounter);
                    textView1.setText("載入中(" + intCounter + "%) Progress:" + progressBar1.getProgress() + "\nIndeterminate:" + progressBar1.isIndeterminate());
                    //}
                    break;
                }
                super.handleMessage(msg);
            }
        };

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intCounter = 0;
                textView1.setText("開始執行");
                progressBar1.setVisibility(View.VISIBLE);
                progressBar1.setProgress(0);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int ii = 0; ii < 10; ii++) {
                            try {
                                intCounter = (ii + 1) * 20;
                                Thread.sleep(1000);
                                Message message = new Message();
                                if (ii == 4) {
                                    message.what = ProgressBarActivity.GUI_STOP_NOTIFIER;
                                } else {
                                    message.what = ProgressBarActivity.GUI_THREADING_NOTIFIER;
                                }
                                myMessageHandler.sendMessage(message);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ progressBar1
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ progressBar2

        final Handler handler = new Handler();

        //次要進度顯示處理
        final Runnable callback1 = new Runnable() {
            @Override
            public void run() {
                progressBar2.incrementSecondaryProgressBy(1);//設定每次增加1
                setSecondaryProgress(100 * progressBar2.getSecondaryProgress());//顯示要執行的進度 %
            }
        };

        //主要進度顯示處理
        final Runnable callback2 = new Runnable() {
            @Override
            public void run() {
                progressBar2.incrementProgressBy(10);//設定每次增加10
                setProgress(100 * progressBar2.getProgress());//設定執行進度%
                progressBar2.incrementSecondaryProgressBy(-100);//次要進度-100
                setSecondaryProgress(100 * progressBar2.getSecondaryProgress());//清空次要進度
            }
        };

        //完成
        final Runnable callback3 = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ProgressBarActivity.this, "OK", Toast.LENGTH_SHORT).show();
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    for (int ii = 0; ii < progressBar2.getMax() / 10; ii++) {
                        for (int jj = 0; jj < progressBar2.getMax(); jj++) {
                            Thread.sleep(20);
                            handler.post(callback1);
                        }
                        handler.post(callback2);
                    }
                    handler.post(callback3);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        thread.start();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ProgressBarActivity.this.setResult(RESULT_CANCELED, ProgressBarActivity.this.getIntent());
                ProgressBarActivity.this.finish();
            }
        });
    }
}
