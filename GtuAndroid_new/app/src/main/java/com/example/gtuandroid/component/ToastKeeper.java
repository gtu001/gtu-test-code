package com.example.gtuandroid.component;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class ToastKeeper {
    
    private final static String TAG = ToastKeeper.class.getSimpleName();

    final Toast toast;

    private ToastKeeper(Context context, String text, boolean isShort) {
        int toastDuring = -1;
        if (isShort) {
            toastDuring = Toast.LENGTH_SHORT;
        } else {
            toastDuring = Toast.LENGTH_LONG;
        }
        toast = Toast.makeText(context, text, toastDuring);
    }

    public static ToastKeeper newInstance(Context context, String text, boolean isShort) {
        return new ToastKeeper(context, text, isShort);
    }

    public void show() {
        if (toast != null && toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast.show();
        }
    }

}
