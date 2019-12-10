package com.example.englishtester.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ScrollView;

import java.util.concurrent.Callable;

public class AutoScrollDownHandler {
    private static final String TAG = AutoScrollDownHandler.class.getSimpleName();

    final Callable<ScrollView> scrollViewGetter;
    Thread scrollThread;
    boolean isStop = false;
    Handler handler = new Handler();
    SpeedType speedType = SpeedType.SLOW;
    Context context;

    public enum SpeedType {
        SLOW(50, 3),//
        MIDDLE(50, 5),//
        FAST(50, 7),//
        ;
        final int scrollStep;
        final long sleepTime;

        SpeedType(long sleepTime, int scrollStep) {
            this.scrollStep = scrollStep;
            this.sleepTime = sleepTime;
        }
    }

    public AutoScrollDownHandler(final Context context, final ScrollView scrollView) {
        this.context = context;
        this.scrollViewGetter = new Callable<ScrollView>() {
            @Override
            public ScrollView call() throws Exception {
                return scrollView;
            }
        };
    }

    public AutoScrollDownHandler(final Context context, final Callable<ScrollView> scrollViewGetter) {
        this.context = context;
        this.scrollViewGetter = scrollViewGetter;
    }

    private ScrollView getScrollView() {
        try {
            return scrollViewGetter.call();
        } catch (Exception e) {
            throw new RuntimeException("getScrollView ERR : " + e.getMessage(), e);
        }
    }

    public void stop() {
        isStop = true;
    }

    public boolean start(final int scrollStep, final long sleepTime) {
        if (getScrollView() == null) {
            Log.v(TAG, "尚未init scrollView!");
            return false;
        }
        boolean isStart = false;
        if (!isRunning()) {
            isStop = false;
            scrollThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isStop) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                getScrollView().smoothScrollTo(0, getScrollView().getScrollY() + scrollStep);
                            }
                        });

                        if (getScrollView().getScrollY() >= ScrollViewHelper.getMaxHeight(getScrollView())) {
                            break;
                        }
                    }
                }
            });
            scrollThread.start();
            isStart = true;
        }
        return isStart;
    }

    public boolean isRunning() {
        return !(scrollThread == null || scrollThread.getState() == Thread.State.TERMINATED);
    }

    public boolean start() {
        return start(speedType.scrollStep, speedType.sleepTime);
    }

    public boolean toggle() {
        return toggle(speedType.scrollStep, speedType.sleepTime);
    }

    public boolean toggle(int scrollStep, long sleepTime) {
        boolean isStart = false;
        if (!isRunning()) {
            isStart = start(scrollStep, sleepTime);
        } else {
            stop();
        }
        return isStart;
    }

    public void setSpeedType(SpeedType speedType) {
        this.speedType = speedType;
    }
}