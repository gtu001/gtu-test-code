package com.example.gtuandroid;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceTest2Activity extends Activity {

    private boolean serviceRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView1 = (TextView) findViewById(R.id.text);
        final Button button = (Button) findViewById(R.id.back);
        button.setText("start/stop");

        final Intent intent = new Intent();
        intent.setClass(ServiceTest2Activity.this, MyService.class);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!serviceRunning) {
                    startService(intent);
                    serviceRunning = true;
                    textView1.setText("service running...");
                } else {
                    stopService(intent);
                    serviceRunning = false;
                    textView1.setText("service stoped");
                }
            }
        });
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "AlarmReceiverTest", Toast.LENGTH_SHORT).show();
        }
    }

    public static class MyService extends Service {
        private static MyService myService = null;

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

        @Override
        public void onCreate() {
            myService = this;
            super.onCreate();
        }

        @Override
        public void onStart(Intent intent, int startId) {
            AlarmManager am = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 5);
            Intent ii = new Intent(this, AlarmReceiver.class);
            PendingIntent pii = PendingIntent.getBroadcast(this, 0, ii, 0);

            // ❑ ELAPSED_REALTIME
            // 在指定的延时过后，发送广播，但不唤醒设备。
            // ❑ ELAPSED_REALTIME_WAKEUP
            // 在指定的演示后，发送广播，并唤醒设备
            // 延时是要把系统启动的时间SystemClock.elapsedRealtime()算进去的，具体用法看代码。
            // ❑ RTC
            // 在指定的时刻，发送广播，但不唤醒设备
            // ❑ RTC_WAKEUP
            // 在指定的时刻，发送广播，并唤醒设备

            // am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pii);

            // 5秒后发送广播，然后每个2秒重复发广播。广播都是直接发到AlarmReceiver的
            int triggerAtTime = (int) (SystemClock.elapsedRealtime() + 5 * 1000);
            int interval = 2 * 1000;
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, interval, pii);
        }

        @Override
        public void onDestroy() {
            AlarmManager am = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
            Intent ii = new Intent(this, AlarmReceiver.class);
            PendingIntent pii = PendingIntent.getBroadcast(this, 0, ii, 0);

            // PendingIntent：简单的说就是在Intent上在加个指定的动作。Intent的话，
            // 我们还需要在执行startActivity、startService或sendBroadcast才能使Intent有用。
            // 而PendingIntent的话就是将这个动作包含在内了，
            // 如PendingIntent.getBroadcast就包含了sendBroadcast的动作。
            am.cancel(pii);
            // 与上面的intent匹配（filterEquals(intent)）的闹钟会被取消
            super.onDestroy();
        }

        public static MyService getMyService() {
            return myService;
        }

        public static void setMyService(MyService myService) {
            MyService.myService = myService;
        }
    }
}
