package com.example.englishtester.common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
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

public class _TemplateService extends Service {

    private static final String TAG = _TemplateService.class.getSimpleName();

    // for activity ↓↓↓↓↓↓ --------------------------------------------------------------------

    /**
     * 使用 bind
     * AndroidManifest.xml
     * application 與 service 的 android:process=":location" 必須相同
     */
    private boolean isBound;

    private void doUnbindService() {
        if (isBound) {
            unbindService(myLocalServiceConnection);
            isBound = false;
        }
    }

    private void doBindService() {
        Log.i("bind", "begin to bind");
        Intent intent = new Intent(this, _TemplateService.class);
        bindService(intent, myLocalServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection myLocalServiceConnection = new ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName name,
                                       android.os.IBinder service) {
            // 因為 客戶端 與 服務 在同一個進程內，這樣一來，就可以知道參數 "service"的類型了，也就可以進行顯示的強制類型轉換了。
            // 而如果 客戶端與服務不在同一個進程中的話，那麼此處是不可以進行顯示強制類型轉換的，
            // 因為，通過Debug，可以發現此時傳進來的 Service 的類型是 BinderProxy
            PomodoroClockHandlerService.MyIBinder myIBinder = (PomodoroClockHandlerService.MyIBinder) service;
            PomodoroClockHandlerService bsi = (PomodoroClockHandlerService) myIBinder.getService();
            isBound = true;

            bsi.start();
        }

        public void onServiceDisconnected(android.content.ComponentName name) {
            isBound = false;
        }
    };

    // for activity ↑↑↑↑↑↑ --------------------------------------------------------------------

    private final _TemplateService.MyIBinder myIBinder = new _TemplateService.MyIBinder();

    /**
     * 聲明一個 Binder 類的實現類，供在 onBind() 方法中返回該類的一個實例
     *
     * @author 001718
     */
    public class MyIBinder extends Binder {
        public Service getService() {
            return _TemplateService.this;
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
