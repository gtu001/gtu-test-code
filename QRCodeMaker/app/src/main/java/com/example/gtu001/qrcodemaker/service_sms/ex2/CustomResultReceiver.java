package com.example.gtu001.qrcodemaker.service_sms.ex2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class CustomResultReceiver extends ResultReceiver {

    /*
     * Step 1: The AppReceiver is just a custom interface class we created.
     * This interface is implemented by the activity
     */
    private AppReceiver appReceiver;

    public CustomResultReceiver(Handler handler,
                                AppReceiver receiver) {
        super(handler);
        appReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (appReceiver != null) {
            /*
             * Step 2: We pass the resulting data from the service to the activity
             * using the AppReceiver interface
             */
            appReceiver.onReceiveResult(resultCode, resultData);
        }
    }

    public void setAppReceiver(AppReceiver appReceiver) {
        this.appReceiver = appReceiver;
    }

    public interface AppReceiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    public static class CustomResultReceiverHelper {
        private CustomResultReceiver resultReceiver;

        /*
         * Step 1: Register the intent service in the activity
         * */
        public void registerService(Context context) {
            Intent intent = new Intent(context, CustomIntentService.class);

            /*
             * Step 2: We pass the ResultReceiver via the intent to the intent service
             * */
            resultReceiver = new CustomResultReceiver(new Handler(), new AppReceiver() {
                @Override
                public void onReceiveResult(int resultCode, Bundle resultData) {
                    /*
                     * Step 3: Handle the results from the intent service here!
                     * */
                    //TODO
                }
            });
            intent.putExtra("receiver", resultReceiver);
            context.startService(intent);
        }

        public void onStop() {
            /*
             * Step 4: don't forget to clear receiver in order to avoid leaks.
             * */
            if (resultReceiver != null) {
                resultReceiver.setAppReceiver(null);
            }
        }
    }
}