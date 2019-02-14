package com.example.englishtester.common;

import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

/**
 * Created by gtu001 on 2018/7/30.
 */

public class ScrollViewHelper {

    @Deprecated
    public static int getMaxHeight2(ScrollView scrollView) {
        return scrollView.getChildAt(0).getHeight();
    }

    public static int getMaxHeight(ScrollView scrollView) {
        int scrollRange = 0;
        if (scrollView.getChildCount() > 0) {
            View child = scrollView.getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (scrollView.getHeight() - scrollView.getPaddingBottom() - scrollView.getPaddingTop()));
        }
        return scrollRange;
    }

    public static void scrollToButtom(final ScrollView scrollView) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }).start();
    }
}
