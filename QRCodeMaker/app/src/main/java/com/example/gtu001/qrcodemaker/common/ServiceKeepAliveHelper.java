package com.example.gtu001.qrcodemaker.common;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

public class ServiceKeepAliveHelper {


    /**
     * api 28 後本後要要求此權限
     * @param activity
     * @return
     */
    /*
    public static boolean verifyForegroundPermissions(Activity activity) {
        // Check if we have write permission
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.FOREGROUND_SERVICE);
        String[] PERMISSIONS_ARRY = {
                Manifest.permission.FOREGROUND_SERVICE,
        };
        int REQUEST_PERMISSION_VAL = 1;
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_ARRY,
                    REQUEST_PERMISSION_VAL
            );
            return true;
        }
        return false;
    }
    */

    /**
     * Show a notification while this service is running.
     */
    public static void showForegroundNotification(Service self, Class<?> targetActivityClz, int icon, int id, String statusMessage, String title) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "local_service_started";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(self, 0,
                new Intent(self, targetActivityClz), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(self)
                .setSmallIcon(icon)  // the status icon
                .setTicker(statusMessage)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(title)  // the label of the entry
                .setContentText(statusMessage)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setAutoCancel(false)//是否自動取消
                .build();

        // Send the notification.
//        mNM.notify(NOTIFICATION, notification);

        self.startForeground(id, notification);
    }
}
