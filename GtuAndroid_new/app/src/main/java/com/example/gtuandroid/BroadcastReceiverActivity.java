package com.example.gtuandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BroadcastReceiverActivity extends Activity {

    private final static String MY_MESSAGE = "com.givemepass.sendmessage";
    private Button send_broadcast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send_broadcast = (Button) findViewById(R.id.back);
        send_broadcast.setText("send_broadcast");

        send_broadcast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 按下button的時候, 就對系統註冊我們下面所寫的廣播類別,
                registerReceiver(mBroadcast, new IntentFilter(MY_MESSAGE));

                // 送出廣播
                Intent intent = new Intent();
                intent.setAction(MY_MESSAGE);
                sendBroadcast(intent);
            }
        });
    }

    private BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        private final static String MY_MESSAGE = "com.givemepass.sendmessage";

        @Override
        public void onReceive(Context mContext, Intent mIntent) {
            // 這個廣播類別是專門接收傳送出來的各類訊息, 而我們篩選出MY_MESSAGE裡面的字串,
            if (MY_MESSAGE.equals(mIntent.getAction())) {
                new AlertDialog.Builder(BroadcastReceiverActivity.this)//
                        .setMessage("收到訊息!")//
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                unregisterReceiver(mBroadcast);
                            }
                        }).show();
            }
        }
    };
}