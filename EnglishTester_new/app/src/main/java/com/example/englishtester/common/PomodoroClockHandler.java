package com.example.englishtester.common;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.englishtester.MainActivity;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import gtu.other.line.LineAppNotifiyHelper_Simple;

public class PomodoroClockHandler {
    //https://proandroiddev.com/using-vibrate-in-android-b0e3ef5d5e07
    private Vibrator myVibrator;
    private static AtomicReference<Timer> TIMER = new AtomicReference<Timer>();
    private Context context;
    private NotificationHelper notificationHelper;

    public PomodoroClockHandler(Context context) {
        this.context = context;
        this.myVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        this.notificationHelper = new NotificationHelper(context);
    }

    private void customVibratePatternNoRepeat() {
        // 0 : Start without a delay
        // 400 : Vibrate for 400 milliseconds
        // 200 : Pause for 200 milliseconds
        // 400 : Vibrate for 400 milliseconds
        long[] mVibratePattern = new long[]{0, 400, 200, 400};

        // -1 : Do not repeat this pattern
        // pass 0 if you want to repeat this pattern from 0th index
        myVibrator.vibrate(mVibratePattern, -1);
    }

    private void showMessage(final String message) {
        LineAppNotifiyHelper_Simple.getInstance().send(message);

        this.notificationHelper.notifyNow(1, "番茄鐘", message, message + "_2", "番茄鐘_2", false, MainActivity.class);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFinishTimeNotify() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 25);
        String message = "工作結束時間為未來 : " + DateFormatUtils.format(cal, "yyyy/MM/dd HH:mm:ss.SSS");
        this.notificationHelper.notifyNow(1, "番茄鐘", message, message + "_2", "番茄鐘_2", false, MainActivity.class);
    }

    public void start() {
        if (TIMER.get() != null) {
            TIMER.get().cancel();
        }
        TIMER.set(new Timer());
        TIMER.get().schedule(new TimerTask() {
            @Override
            public void run() {
                showFinishTimeNotify();
                customVibratePatternNoRepeat();
                showMessage("工作開始!..");
            }
        }, 0);
        TIMER.get().schedule(new TimerTask() {
            @Override
            public void run() {
                customVibratePatternNoRepeat();
                showMessage("工作時間到!..");
            }
        }, 25 * 60 * 1000);
        TIMER.get().schedule(new TimerTask() {
            @Override
            public void run() {
                customVibratePatternNoRepeat();
                showMessage("休息時間到!..");
            }
        }, 30 * 60 * 1000);
    }
}
