package com.example.englishtester.common;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import gtu.other.line.LineAppNotifiyHelper_Simple;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    public static String ALARM_TEXT_KEY = "ALARM_TEXT_KEY";
    public static String POMODORO_CLOCK_OPERATOR = "POMODORO_CLOCK_OPERATOR";

    private Vibrator myVibrator;
    private Context context;
    private NotificationHelper notificationHelper;

    private void initServ(Context context) {
        this.context = context;
        this.myVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        this.notificationHelper = new NotificationHelper(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        IntentShower.show(intent, "AlarmReceiver");

        this.initServ(context);


        // 開機設鬧鐘
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            // Set the alarm here.
        }

        String text = intent.getStringExtra(ALARM_TEXT_KEY);
        if (StringUtils.isNotBlank(text)) {
            showEndDialogAndRing(text, 0L, context);
        }


        // 番茄鐘 ↓↓↓↓↓↓↓↓
        String operator = intent.getStringExtra(POMODORO_CLOCK_OPERATOR);
        if ("start".equals(operator)) {
            showFinishTimeNotify();
            customVibratePatternNoRepeat();
            showMessage("工作開始!..");
//            showEndDialogAndRing("番茄鐘工作開始", 5000L, context);
        } else if ("workdone".equals(operator)) {
            customVibratePatternNoRepeat();
            showMessage("工作時間到!..");
            showEndDialogAndRing("番茄鐘工作完成", 20000L, context);
        } else if ("restdone".equals(operator)) {
            customVibratePatternNoRepeat();
            showMessage("休息時間到!..");
            showEndDialogAndRing("番茄鐘休息結束", 10000L, context);
        }
        // 番茄鐘 ↑↑↑↑↑↑↑↑
    }

    private void showEndDialogAndRing(final String btnText, Long during, Context context) {
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

    private void showMessage(final String message) {
        LineAppNotifiyHelper_Simple.getInstance().send(message);

        this.notificationHelper.notifyNow(1, "番茄鐘", message, false, MainActivity.class);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
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

    private void showFinishTimeNotify() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 25);
        String message = "工作時間:" + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss") + " ～ " + DateFormatUtils.format(cal, "yyyy/MM/dd HH:mm:ss");
        LineAppNotifiyHelper_Simple.getInstance().send("番茄鐘起訖時間 : " + message);
        this.notificationHelper.notifyNow(2, "番茄鐘起訖時間", message, false, MainActivity.class);
    }

    public void cancelAlarm() {
        this.notificationHelper.cancel(1);
        this.notificationHelper.cancel(2);
        showMessage("取消番茄鐘!!");
    }
}