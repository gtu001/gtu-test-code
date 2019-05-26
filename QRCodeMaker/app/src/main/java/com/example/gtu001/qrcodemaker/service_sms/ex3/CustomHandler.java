package com.example.gtu001.qrcodemaker.service_sms.ex3;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

public class CustomHandler extends Handler {

    private AppReceiver appReceiver;

    public CustomHandler(AppReceiver receiver) {
        appReceiver = receiver;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        appReceiver.onReceiveResult(msg);
    }


    public interface AppReceiver {
        void onReceiveResult(Message message);
    }

    public static class CustomHandlerHelper {
        private CustomHandler handler;

        /*
         * Step 1: Register the intent service in the activity
         * */
        private void registerService(Context context) {
            Intent intent = new Intent(context, CustomIntentService.class);

            /*
             * Step 2: We pass the handler via the intent to the intent service
             * */
            handler = new CustomHandler(new AppReceiver() {
                @Override
                public void onReceiveResult(Message message) {
                    /*
                     * Step 3: Handle the results from the intent service here!
                     * */
                    switch (message.what) {
                        //TODO
                    }
                }
            });
            intent.putExtra("handler", new Messenger(handler));
            context.startService(intent);
        }

    }
}