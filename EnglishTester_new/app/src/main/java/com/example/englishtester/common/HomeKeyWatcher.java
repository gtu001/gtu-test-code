package com.example.englishtester.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class HomeKeyWatcher {

    static final String TAG = HomeKeyWatcher.class.getSimpleName();
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerRecevier mRecevier;

    public HomeKeyWatcher(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
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
        final String SYSTEM_DIALOG_REASON_LONGPRESSED_1 = "assist";//常按home建
        final String SYSTEM_DIALOG_REASON_LONGPRESSED_2 = "voiceinteraction";//常按home建

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
    }

    public interface OnHomePressedListener {
        public void onHomePressed();

        public void onHomeLongPressed();

        public void onMultitaskPressed();
    }
}