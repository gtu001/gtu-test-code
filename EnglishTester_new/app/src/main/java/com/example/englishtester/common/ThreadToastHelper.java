package com.example.englishtester.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * 在 new thread 外面先 newInstance , 在 new Thread 內呼叫 toast
 */
public class ThreadToastHelper {

    private Context context;
    private static final String MESSAGE_KEY = "MESSAGE_KEY";
    private static final String MESSAGE_DURING = "MESSAGE_DURING";

    public static ThreadToastHelper newInstance(Context context) {
        return new ThreadToastHelper(context);
    }

    private ThreadToastHelper(Context context) {
        this.context = context;
    }

    public void toast(String text, boolean isShort) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        bundle.putString(MESSAGE_KEY, text);
        bundle.putInt(MESSAGE_DURING, Toast.LENGTH_SHORT);
        if (!isShort) {
            bundle.putInt(MESSAGE_DURING, Toast.LENGTH_LONG);
        }
        toastHandler.sendMessage(msg);
    }

    private final Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(context, msg.getData().getString(MESSAGE_KEY), msg.getData().getInt(MESSAGE_DURING)).show();
        }
    };
}
