package com.example.gtu001.qrcodemaker.service_sms.ex3;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

public class CustomIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;


    public CustomIntentService() {
        super(CustomIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        /*
         * Step 1: We pass the Handler from the activity to the intent service via intent.
         *  */
        final Handler handler = intent.getParcelableExtra("handler");

        //TODO: process background task here!

        /*
         * Step 2: Now background service is processed,
         * we can pass the status of the service back to the activity using the handler
         *  */
        Message msg = new Message();
        msg.obj = "Sending message to UI after completion of background task!";
        msg.what = STATUS_FINISHED;
        handler.sendMessage(msg);
    }
}