package com.example.gtu001.qrcodemaker.common;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class PhoneListener extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("PhoneListener", action);
        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
        } else {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            String incoming_number = "";
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    incoming_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    }
}