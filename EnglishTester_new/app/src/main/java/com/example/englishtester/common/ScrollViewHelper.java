package com.example.englishtester.common;

import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

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

    public static class AutoScrollDownHandler {
        final ScrollView scrollView;
        Thread scrollThread;
        boolean isStop = false;
        Handler handler = new Handler();

        private static final int SCROLL_STEP = 3;
        private static final int SLEEP_TIME = 50;

        public AutoScrollDownHandler(final ScrollView scrollView) {
            this.scrollView = scrollView;
        }

        public void stop() {
            isStop = true;
        }

        public boolean start(int scrollStep, long sleepTime) {
            boolean isStart = false;
            if (!isRunning()) {
                isStop = false;
                scrollThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!isStop) {
                            try {
                                sleep(SLEEP_TIME);
                            } catch (InterruptedException e) {
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.smoothScrollTo(0, scrollView.getScrollY() + SCROLL_STEP);
                                }
                            });

                            if (scrollView.getScrollY() >= ScrollViewHelper.getMaxHeight(scrollView)) {
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
            return start(SCROLL_STEP, SLEEP_TIME);
        }

        public boolean toggle() {
            return toggle(SCROLL_STEP, SLEEP_TIME);
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
    }
}
