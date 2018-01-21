package com.example.gtuandroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.gtuandroid.R;

public class SMSBroadcastReceiverActivity extends Activity {
    /** Called when the activity is first created. */
    // 擷取系統資訊，簡訊的系統資訊為
    private final static String MSG_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private Button closeBroadcast;
    private TextView smsContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        closeBroadcast = (Button) findViewById(R.id.back);
        smsContent = (TextView) findViewById(R.id.text);
        closeBroadcast.setText("closeBroadcast");

        registerReceiver(mBroadcastReceiver, new IntentFilter(MSG_RECEIVED));
        closeBroadcast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 因為不知道簡訊何時會傳過來，等到傳過來的時候，再將註冊解除即可。
                try{
                    unregisterReceiver(mBroadcastReceiver);
                }catch(Exception ex){
                    Log.e("err", ex.getMessage());
                }
            }
        });
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(MSG_RECEIVED)) {
                Bundle msg = intent.getExtras();

                // 首先從Bundle取出簡訊，簡訊的識別字是pdus，它是一個物件陣列
                Object[] messages = (Object[]) msg.get("pdus");
                
                // 必須使用SmsMessage將陣列的每一列轉成SmsMessage，
                // 由於我們只有要示範傳一封簡訊，因此只取出第一列就可以了
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) messages[0]);

                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("From:" + sms.getDisplayOriginatingAddress() + "\n");
                strBuilder.append("text:" + sms.getMessageBody());
                smsContent.setText(strBuilder);
            }
        }
    };
}