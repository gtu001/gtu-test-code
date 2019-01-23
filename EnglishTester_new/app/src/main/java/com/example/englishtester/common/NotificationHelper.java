package com.example.englishtester.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.englishtester.R;

public class NotificationHelper {
    private static final String TAG = NotificationHelper.class.getSimpleName();

    private Context context;
    private NotificationManager manager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyNow(int id, String title, String text, boolean autoCancel, Class<?> activityClz) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);//通知标题
        builder.setContentText(text);//主内容区
        builder.setContentInfo("補充內容 目前無作用");//补充内容
        builder.setSmallIcon(R.mipmap.janna_icon1);
        builder.setTicker("新消息 目前無作用");//新消息
        builder.setAutoCancel(autoCancel);
        builder.setWhen(System.currentTimeMillis());

        builder.setLights(Color.BLUE, 500, 500);
        builder.setStyle(new Notification.BigTextStyle());
        builder.setVibrate(new long[]{500, 500});

        //音效
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        Intent intent2 = new Intent();
        if (activityClz != null) {
            intent2 = new Intent(context, activityClz);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        this.manager.notify(TAG, id, notification);
    }

    public void cancel(int id) {
        this.manager.cancel(TAG, id);
    }
}
