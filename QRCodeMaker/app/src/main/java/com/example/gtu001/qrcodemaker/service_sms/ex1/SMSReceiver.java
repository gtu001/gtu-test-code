package com.example.gtu001.qrcodemaker.service_sms.ex1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/*
 * Step 1: Create a broadcastReceiver class called SMSReceiver
 * */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras() != null) {

            /*
             * Step 2: We need to fetch the incoming sms that is broadcast.
             * For this we check the intent of the receiver.
             * */

            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (int i = 0; i < pdus.length; i++) {

                SmsMessage smsMessage = Build.VERSION.SDK_INT >= 19
                        ? Telephony.Sms.Intents.getMessagesFromIntent(intent)[i]
                        : SmsMessage.createFromPdu((byte[]) pdus[i]);



                /*
                 * Step 3: We can get the sender & body of the incoming sms.
                 * The actual parsing of otp is not done here since that is not
                 * the purpose of this implementation
                 *  */
                String sender = smsMessage.getOriginatingAddress();
                String body = smsMessage.getMessageBody().toString();
                String otpCode = "123456";



                /*
                 * Step 4: We have parsed the otp. Now we can create an intent
                 * and pass the otp data via that intent.
                 * We have to specify an action for this intent. Now this can be anything String.
                 * This action is important because this action identifies the broadcast event
                 *  */

                Intent in = new Intent("com.an.sms.example");
                Bundle extras = new Bundle();
                extras.putString("com.an.sms.example.otp", otpCode);
                in.putExtras(extras);
                context.sendBroadcast(in);
            }
        }
    }


    public static class SmsReceiverHelper {
        private BroadcastReceiver broadcastReceiver;

        /*
         * Step 2: Register the broadcast Receiver in the activity
         * */
        public void registerReceiver(Context context) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String otpCode = intent.getStringExtra("com.an.sms.example.otp");

                    /*
                     * Step 3: We can update the UI of the activity here
                     * */
                }
            };
            context.registerReceiver(broadcastReceiver, new IntentFilter("com.an.sms.example"));
        }

        public void onStop(Context context) {
            /*
             * Step 4: Ensure to unregister the receiver when the activity is destroyed so that
             * you don't face any memory leak issues in the app
             */
            if (broadcastReceiver != null) {
                context.unregisterReceiver(broadcastReceiver);
            }
        }
    }

}