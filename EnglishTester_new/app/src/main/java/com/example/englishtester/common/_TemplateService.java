package com.example.englishtester.common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.example.englishtester.MainActivity;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.Nullable;
import gtu.other.line.LineAppNotifiyHelper_Simple;

public class _TemplateService extends Service {

    private static final String TAG = _TemplateService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand == " + intent);
        if (intent != null) {
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    sb.append(key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL") + "\n");
                }
            }
            Log.line(TAG, "Intent = " + sb);
        }
        return START_NOT_STICKY;
    }

    public void onCreate() {
        Log.v(TAG, "onCreate");

        super.onCreate();
    }

    public void onDestroy() {
        Log.v(TAG, "onDestroy");

        super.onDestroy();
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }
}
