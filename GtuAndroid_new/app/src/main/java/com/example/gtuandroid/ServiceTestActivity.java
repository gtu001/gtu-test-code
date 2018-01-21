package com.example.gtuandroid;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ServiceTestActivity extends Activity {

    private boolean serviceRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView1 = (TextView) findViewById(R.id.text);
        final Button button = (Button) findViewById(R.id.back);
        button.setText("start/stop");

        final Intent intent = new Intent();
        intent.setClass(ServiceTestActivity.this, MyService.class);
        
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!serviceRunning) {
                    startService(intent);
                    serviceRunning = true;
                    textView1.setText("service running...");
                } else {
                    stopService(intent);
                    serviceRunning = false;
                    textView1.setText("service stoped");
                }
            }
        });
    }

    public static class MyService extends Service {
        private static final String TAG = "MyService";
        
        public MyService(){
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

        private MyHandler handler;
        private Thread thread;

        /*
         * 當啟動一個Service的時候,會調用該Service中的onCreate()和onStartCommand()方法。
         * 可以看到,這次只有onStartCommand
         * ()方法執行了,onCreate()方法並沒有執行,為什麼會這樣呢？這是由于onCreate(
         * )方法只會在Service第一次被創建的時候調用
         * ,如果當前Service已經被創建過了,不管怎樣調用startService()方法,onCreate()方法都不會再執行
         */
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d(TAG, "onStartCommand() executed");
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onCreate() {
            Log.d(TAG, "onCreate() executed");
            handler = new MyHandler();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    handler.postDelayed(thread, 1000);
                }
            });
            // handler.postDelayed(thread, 1000);
            handler.post(thread);
            super.onCreate();
        }

        @Override
        public void onDestroy() {
            Log.d(TAG, "onDestroy() executed");
            Message message = new Message();
            message.what = 1;
            handler.handleMessage(message);
            handler.removeCallbacks(thread);
            super.onDestroy();
        }

        private class MyHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case 0:
                    Log.v("givemepass", "service I am alive.");
                    break;
                case 1:
                    Log.v("givemepass", "service I am dead.");
                    break;
                }
                super.handleMessage(msg);
            }
        }
    }
}
