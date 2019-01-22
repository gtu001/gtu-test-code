package com.example.englishtester.common;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

public class BackButtonPreventer {
    private static final String TAG = BackButtonPreventer.class.getSimpleName();

    private Activity activity;

    public BackButtonPreventer(Activity activity) {
        this.activity = activity;
    }

    AtomicLong firstBackPressTime = new AtomicLong(-1);

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (firstBackPressTime.get() == -1 || (System.currentTimeMillis() - firstBackPressTime.get()) > 500) {
                Toast.makeText(activity, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstBackPressTime.set(System.currentTimeMillis());

            } else {
                firstBackPressTime.set(-1);

                //回前頁
                activity.onBackPressed();
            }
        }
        return false;
    }
}
