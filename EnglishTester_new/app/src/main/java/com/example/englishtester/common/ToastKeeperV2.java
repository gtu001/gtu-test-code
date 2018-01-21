package com.example.englishtester.common;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.englishtester.MainActivity;

public class ToastKeeperV2 {

    private final static String TAG = ToastKeeperV2.class.getSimpleName();

    Toast toast;
    String text;

    private static final ToastKeeperV2 _INST = new ToastKeeperV2();

    private ToastKeeperV2() {
    }

    public ToastKeeperV2 makeText(Context context, String text, boolean isShort) {
        int toastDuring = -1;
        if (isShort) {
            toastDuring = Toast.LENGTH_SHORT;
        } else {
            toastDuring = Toast.LENGTH_LONG;
        }
        if (!StringUtils.equals(text, this.text)) {
            this.text = text;
            toast = Toast.makeText(context, text, toastDuring);
            Log.v(TAG, "create new toast : " + text + ", " + this.text);
        }
        return this;
    }

    public static ToastKeeperV2 getInstance() {
        return _INST;
    }

    public void show() {
        if (toast != null && toast.getView().getWindowVisibility() != View.VISIBLE) {
            Log.v(TAG, "toast show!");
            toast.show();
        }
    }
}
