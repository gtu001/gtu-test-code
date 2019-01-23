package com.example.englishtester.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

    public void notifyNow(int id, String title, String text, String info, String ticker, boolean autoCancel, Class<?> activityClz) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);//通知标题
        builder.setContentText(text);//主内容区
        builder.setContentInfo(info);//补充内容
        builder.setSmallIcon(R.mipmap.janna_icon1);
        builder.setTicker(ticker);//新消息
        builder.setAutoCancel(false);
        builder.setWhen(System.currentTimeMillis());
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
