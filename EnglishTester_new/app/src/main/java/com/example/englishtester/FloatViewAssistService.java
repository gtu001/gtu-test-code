package com.example.englishtester;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import com.example.englishtester.common.Log;
import android.widget.Toast;

import com.example.englishtester.common.FloatServiceHolderBroadcastReceiver;
import com.example.englishtester.common.ServiceUtil;

import java.util.Calendar;
import java.util.List;

/**
 * @author gtu001_5F
 */
public class FloatViewAssistService extends Service {

    private static final String TAG = FloatViewAssistService.class.getSimpleName();

    private static final int PID = 1338;

    private boolean enable = true;
    private AssistThreadClass assistThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "oncreat");

        //啟動守護線程
        assistThread = FloatViewAssistService.createAssistThread(this, FloatViewService.class);
    }

    /**
     * 啟動守護線程
     */
    public static AssistThreadClass createAssistThread(final Context fromContext, final Class<? extends Service> targetServiceClass) {
        AssistThreadClass assistThread = new AssistThreadClass(fromContext, targetServiceClass);
        assistThread.start();
        return assistThread;
    }

    /**
     * 啟動守護線程
     */
    public static class AssistThreadClass extends Thread {
        Context fromContext;
        Class<? extends Service> targetServiceClass;

        AssistThreadClass(Context fromContext, Class<? extends Service> targetServiceClass) {
            super();
            this.fromContext = fromContext;
            this.targetServiceClass = targetServiceClass;
        }

        @Override
        public void run() {
            while (FloatServiceHolderBroadcastReceiver.isFloatViewServiceEnable()) {
                Log.i(TAG, " 时间： " + Calendar.getInstance().get(Calendar.SECOND));
                keepAssistService(fromContext, targetServiceClass);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.v(TAG, "# AssistThreadClass Thread STOP ");
        }

        private void keepAssistService(Context fromContext, Class<? extends Service> targetServiceClass) {
            boolean isRun = ServiceUtil.isServiceRunning(fromContext, targetServiceClass);
            if (isRun == false) {
                Log.i(TAG, "test001 - 守護線程重新啟動!~");
                Intent intent = new Intent(fromContext, targetServiceClass);
                intent.setAction(fromContext.getPackageName());
                fromContext.startService(intent);
                Toast.makeText(fromContext, "test001 - 守護線程重新啟動!~", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 啟動通知欄
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        builder.setContentInfo("补充内容");
        builder.setContentText("主内容区");
        builder.setContentTitle("通知标题");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("新消息");
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        Intent intent2 = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //先显示
        startForeground(PID, notification);
        //再销毁
        stopForeground(true);
        return START_STICKY;
    }

    /**
     * 啟動通知欄
     */
    public static int onStartCommandForOrignService(Intent intent, int flags, int startId, Service service) {
        Context context = service.getApplicationContext();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.janna_icon1);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources()
                , R.drawable.janna_icon1));
        builder.setContentTitle("開啟懸浮字典");
        builder.setContentText("開啟懸浮字典");
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true); // 需要VIBRATE权限
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT); // Creates an explicit intent for an Activity in your app
        builder.setContentInfo("补充内容");
        builder.setTicker("新消息");
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());

        // 自定义打开的界面
        Intent resultIntent = new Intent(context, FloatViewService.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = builder.build();
//        notificationManager.notify(pId, notification);

        //设置为前台服务，必须绑定一个notification对象，实际上也就是说如果你想做持久化的Service就得让用户知道，PID是自定义的整数表明notification的ID
        service.startForeground(PID, notification);

        //启动辅助Service
        Intent intent3 = new Intent(service, FloatViewAssistService.class);
        service.startService(intent3);

        //註冊廣播器
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        context.registerReceiver(new FloatServiceHolderBroadcastReceiver(), filter);

        return START_STICKY;
    }

    public void onTaskRemoved(Intent rootIntent) {
        //unregister listeners
        //do any other cleanup if required
        //stop service
        stopSelf();
    }
}