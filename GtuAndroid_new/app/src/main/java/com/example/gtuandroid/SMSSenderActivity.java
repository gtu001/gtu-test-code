package com.example.gtuandroid;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SMSSenderActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sender);

        final EditText sendTo = (EditText) findViewById(R.id.sendTo);
        final EditText content = (EditText) findViewById(R.id.content);
        final Button sendBtn = (Button) findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true){
                    return;
                }
                
                SmsManager smsManager = SmsManager.getDefault();
                try {
                    smsManager.sendTextMessage(sendTo.getText().toString(),//
                            null,//
                            content.getText().toString(), //
                            PendingIntent.getBroadcast(//
                                    getApplicationContext(),//
                                    0,//
                                    new Intent(), 0),//
                            null);//
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
