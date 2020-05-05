package com.example.englishtester.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.example.englishtester.MainActivity;
import com.example.englishtester.R;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import gtu.other.line.LineAppNotifiyHelper_Simple;

public class PomodoroClockHandlerService extends Service {

    private static final String TAG = PomodoroClockHandlerService.class.getSimpleName();

    private TaskHandler mTaskHandler;

    private class TaskHandler {

        private Pair<AlarmManager, PendingIntent> getManager(String operator, int requestCode, Context context) {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ComponentName componentName = new ComponentName(context.getPackageName(), context.getPackageName() + ".AlarmReceiver");
                intent.setComponent(componentName);
            }
            intent.putExtra(AlarmReceiver.POMODORO_CLOCK_OPERATOR, operator);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            return Pair.create(alarmMgr, alarmIntent);
        }

        public void delay_wakeup(String text, long delay, int requestCode, Context context) {
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

        public void start() {
            delay_wakeup("start", 0, 111, getApplicationContext());
            delay_wakeup("workdone", 25 * 60 * 1000, 112, getApplicationContext());
            delay_wakeup("restdone", 30 * 60 * 1000, 113, getApplicationContext());
        }
    }

    public void start() {
        mTaskHandler = new TaskHandler();
        mTaskHandler.start();
    }

    // --------------------------------------------------------------------------
    // --------------------------------------------------------------------------

    private final MyIBinder myIBinder = new MyIBinder();

    /**
     * 聲明一個 Binder 類的實現類，供在 onBind() 方法中返回該類的一個實例
     *
     * @author 001718
     */
    public class MyIBinder extends Binder {
        public Service getService() {
            return PomodoroClockHandlerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand == " + intent);
        start();
        return START_NOT_STICKY;
    }

    public void onCreate() {
        Log.v(TAG, "onCreate");

        super.onCreate();

        // foreground ---
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.getApplicationContext(), "MyNotifi")//
                .setSmallIcon(R.drawable.janna_icon1)//
                .setContentTitle("ha")//
                .setContentText("pp")//
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);//
        startForeground(1, mBuilder.build());
    }

    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }
}
