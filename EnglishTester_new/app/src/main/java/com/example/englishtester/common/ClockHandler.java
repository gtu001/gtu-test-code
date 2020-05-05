package com.example.englishtester.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Pair;

import java.util.Calendar;
import java.util.Timer;

/**
 * requestCode 會蓋掉彼此 不可重複
 */
public class ClockHandler {

    private static String TAG = ClockHandler.class.getSimpleName();


    private static Pair<AlarmManager, PendingIntent> getManager(String text, int requestCode, Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ComponentName componentName = new ComponentName(context.getPackageName(), context.getPackageName() + ".AlarmReceiver");
            intent.setComponent(componentName);
        }
        intent.putExtra(AlarmReceiver.ALARM_TEXT_KEY, text);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return Pair.create(alarmMgr, alarmIntent);
    }

    public static void delay_wakeup(String text, long delay, int requestCode, Context context) {
        Pair<AlarmManager, PendingIntent> info = getManager(text, requestCode, context);
        AlarmManager alarmMgr = info.first;
        PendingIntent alarmIntent = info.second;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            delay, alarmIntent);
        } else {
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            delay, alarmIntent);
        }
    }

    public static void dealyPeriod_wakeup(String text, long delay, long period, int requestCode, Context context) {
        Pair<AlarmManager, PendingIntent> info = getManager(text, requestCode, context);
        AlarmManager alarmMgr = info.first;
        PendingIntent alarmIntent = info.second;

//        AlarmManager.INTERVAL_HALF_HOUR
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + delay,
                period, alarmIntent);
    }

    public static void setTimePeriod_wakeup(String text, Calendar calendar, long period, int requestCode, Context context) {
        Pair<AlarmManager, PendingIntent> info = getManager(text, requestCode, context);
        AlarmManager alarmMgr = info.first;
        PendingIntent alarmIntent = info.second;

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                period, alarmIntent);
    }
}
