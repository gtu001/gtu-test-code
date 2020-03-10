package com.example.gtu001.qrcodemaker.common.mp3;

import android.content.Context;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.custom_dialog.UrlPlayerDialog_bg;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class PercentProgressBarTimer {
    private static final String TAG = PercentProgressBarTimer.class.getSimpleName();

    public AtomicBoolean isPercentProgressTrigger = new AtomicBoolean(false);
    private Context context;
    private Timer timer;
    private SeekBar progressBar;
    private TextView textTimer;
    private TextView textContent;
    private TextView textTitle;
    private final Handler handler = new Handler();

    private boolean isClose = false;

    public void close() {
        isClose = true;
    }

    public SeekBar getProgressBar() {
        return progressBar;
    }

    public TextView getTextTimer() {
        return textTimer;
    }

    public TextView getTextContent() {
        return textContent;
    }

    public TextView getTextTitle() {
        return textTitle;
    }

    public PercentProgressBarTimer() {
    }

    public PercentProgressBarTimer(final Context context, SeekBar progressBar, TextView textTimer, TextView textTitle, TextView textContent) {
        this.progressBar = progressBar;
        this.textTimer = textTimer;
        this.textContent = textContent;
        this.textTitle = textTitle;
        this.context = context;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (isClose && timer != null) {
                        cancel();
                    }

                    UrlPlayerServiceHander mUrlPlayerServiceHander = UrlPlayerDialog_bg.getUrlPlayerServiceHander();
                    if (mUrlPlayerServiceHander == null || !mUrlPlayerServiceHander.isInitOk()) {
                        return;
                    }

                    final int percent = mUrlPlayerServiceHander.getProgressPercent(context);
                    final String timeTxt = mUrlPlayerServiceHander.getProgressTime(context);
                    final Map<String, String> map = mUrlPlayerServiceHander.getCurrentBean(context);
                    Log.v(TAG, "[PercentProgressBarTimer] == " + timeTxt + "\t" + percent);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            isPercentProgressTrigger.set(true);
                            getProgressBar().setProgress(percent);
                            getTextTimer().setText(timeTxt);
                            getTextTitle().setText(map.get("name"));
                            getTextContent().setText(map.get("path"));
                        }
                    });
                } catch (final Exception e) {
                    Log.e(TAG, "[PercentProgressBarTimer] ERR : " + e.getMessage(), e);
                    try {
                        cancel();
                    } catch (Exception ex2) {
                    }
                }
            }
        }, 0, 1000L);
    }
}