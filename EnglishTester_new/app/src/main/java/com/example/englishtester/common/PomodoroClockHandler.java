package com.example.englishtester.common;

import android.app.Service;
import android.content.Context;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.example.englishtester.MainActivity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import gtu.other.line.LineAppNotifiyHelper_Simple;

public class PomodoroClockHandler {
    private Vibrator myVibrator;
    private static AtomicReference<Timer> TIMER = new AtomicReference<Timer>();
    private Context context;
    private NotificationHelper notificationHelper;
    private static AtomicBoolean isStarting = new AtomicBoolean(false);

    public PomodoroClockHandler(Context context) {
        this.context = context;
        this.myVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        this.notificationHelper = new NotificationHelper(context);
    }

    private void customVibratePatternNoRepeat() {
        //https://proandroiddev.com/using-vibrate-in-android-b0e3ef5d5e07
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
        showMessage(message, null, null);
    }

    private void showMessage(final String message, String endDialogBtnText, Long during) {
        LineAppNotifiyHelper_Simple.getInstance().send(message);

        this.notificationHelper.notifyNow(1, "番茄鐘", message, false, MainActivity.class);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

        if (StringUtils.isNotBlank(endDialogBtnText)) {
            showEndDialog(endDialogBtnText, during, context);
        }
    }

    private void showEndDialog(final String btnText, Long during, Context context) {
        final Ringtone ringtone = RingNotificationHelper.getInstance().ring(context, 0.3f, null);
        final WindowTomatoDialog dlg2 = new WindowTomatoDialog(context);
        dlg2.setCloseTomatoBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtone.stop();
                dlg2.dismiss();
            }
        });
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                dlg2.showDialog(btnText);
            }
        });
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ringtone.stop();
                dlg2.dismiss();
            }
        }, during);
    }

    private void showFinishTimeNotify() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 25);
        String message = "工作時間:" + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss") + " ～ " + DateFormatUtils.format(cal, "yyyy/MM/dd HH:mm:ss");
        LineAppNotifiyHelper_Simple.getInstance().send("番茄鐘起訖時間 : " + message);
        this.notificationHelper.notifyNow(2, "番茄鐘起訖時間", message, false, MainActivity.class);
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
                showMessage("工作時間到!..", "番茄鐘工作完成", 20000L);
            }
        }, 25 * 60 * 1000);
        TIMER.get().schedule(new TimerTask() {
            @Override
            public void run() {
                customVibratePatternNoRepeat();
                showMessage("休息時間到!..", "番茄鐘休息結束", 10000L);
                //設定為初始狀態
                isStarting.set(false);
            }
        }, 30 * 60 * 1000);
        //設定為開始
        isStarting.set(true);
    }

    public boolean isStart() {
        return TIMER.get() != null && isStarting.get();
    }

    public void cancel() {
        if (TIMER.get() != null) {
            TIMER.get().cancel();
        }
        TIMER.set(null);

        this.notificationHelper.cancel(1);
        this.notificationHelper.cancel(2);

        showMessage("取消番茄鐘!!");

        isStarting.set(false);
    }
}
