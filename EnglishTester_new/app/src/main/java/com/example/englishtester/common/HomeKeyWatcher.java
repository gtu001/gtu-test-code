package com.example.englishtester.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HomeKeyWatcher {

    static final String TAG = HomeKeyWatcher.class.getSimpleName();
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerRecevier mRecevier;

    public HomeKeyWatcher(Context context) {
        mContext = context;

        //多工返回鍵
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        //Power鍵
        mFilter.addAction(Intent.ACTION_SCREEN_ON);
        mFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mFilter.addAction(Intent.ACTION_USER_PRESENT);
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mRecevier = new InnerRecevier();
    }

    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, mFilter);
        }
    }

    public void stopWatch() {
        if (mRecevier != null) {
            mContext.unregisterReceiver(mRecevier);
        }
    }

    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";//多工建
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";//home建
        final String SYSTEM_DIALOG_REASON_LONGPRESSED_1 = "assist";//長按home建
        final String SYSTEM_DIALOG_REASON_LONGPRESSED_2 = "voiceinteraction";//長按home建

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    for (int i = 0; i < 10; i++)
                        Log.e(TAG, "action:" + action + ",reason:" + reason);
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            mListener.onHomePressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            mListener.onMultitaskPressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_LONGPRESSED_1) || //
                                reason.equals(SYSTEM_DIALOG_REASON_LONGPRESSED_2)) {
                            mListener.onHomeLongPressed();
                        }
                    }
                }
            }

            //電源鍵
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.v(TAG, "In Method:  ACTION_SCREEN_OFF");
                if (mListener != null) {
                    mListener.onPowerOff();
                }
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.v(TAG, "In Method:  ACTION_SCREEN_ON");
                if (mListener != null) {
                    mListener.onPowerOn();
                }
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                Log.v(TAG, "In Method:  ACTION_USER_PRESENT");
            }
        }
    }

    public abstract static class OnHomePressedListenerAdapter implements OnHomePressedListener {
        public abstract void onPressed();

        @Override
        public void onHomePressed() {
            onPressed();
        }

        @Override
        public void onHomeLongPressed() {
            onPressed();
        }

        @Override
        public void onMultitaskPressed() {
            onPressed();
        }

        @Override
        public void onPowerOff() {
            onPressed();
        }

        @Override
        public void onPowerOn() {
            //onPressed();
        }
    }

    public interface OnHomePressedListener {
        void onHomePressed();

        void onHomeLongPressed();

        void onMultitaskPressed();

        void onPowerOff();

        void onPowerOn();
    }
}