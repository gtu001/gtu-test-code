package com.example.englishtester.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {

    public static void makeToast(final Context context, final String message, boolean isShort) {
        final int during = isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, during).show();
            }
        });
    }
}
