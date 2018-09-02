package com.example.gtuandroid;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationTestActivity extends Activity {

    private static final String TAG = NotificationTestActivity.class.getSimpleName();
    private static final int NOTIFICATION_ID_REQUEST_CODE = 100;
    private static final String CALL_BACK_KEY = "callback_test_string";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView1 = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.back);
        button.setText("Notification");

        // 取得notification的callback資料
        Bundle bundle = getIntent().getExtras();
        String valStr = bundle.getString(CALL_BACK_KEY);
        Log.v(TAG, "callback : " + valStr);
        if (valStr != null) {
            Toast.makeText(getApplicationContext(), "callback : " + valStr, Toast.LENGTH_SHORT).show();
        }

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // 宣告一個Intent物件, 當我們按下訊息的時候,
                // 它會帶我們到另外一個Activity。
                Intent intent = new Intent();
                intent.putExtra(CALL_BACK_KEY, "哈哈哈!");
                intent.setClass(NotificationTestActivity.this, NotificationTestActivity.class);

                // 我們宣告一個PendingIntent的物件, 這個類別的物件用途很廣,
                // 跟Intent不太一樣, 它執行完並不會馬上啟動,
                // 你可以想像成一個延遲啟動的Intent, 當使用者點訊息的時候, 它才會跳到別的Activity,
                // 但是你會說, 我利用Intent也辦的到啊!
                // 不同的地方是, Intent是我們本身的Activity去啟動Intent讓它跳到別的Activity,
                // 而PendingIntent則是把Intent包起來, 丟給別人, 當有需要的時候,
                // 別人才會去啟動Intent。
                // 簡單來說, 就是我就不幫你轉到其他的Activity, 但是我幫你包好了,
                // 如果你想跳到別的Activity的話, 你只要執行PendingIntent就可以了。
                PendingIntent pendingIntent = PendingIntent.getActivity(//
                        NotificationTestActivity.this, //
                        NOTIFICATION_ID_REQUEST_CODE, //
                        intent, //
                        0);

                // 寫入訊息的標題以及內容, 然後告知這個訊息, 如果我點了它,
                // 該執行哪一個PendingIntent。
                Notification notification = createNotification(pendingIntent);

                notificationManger.notify(0, notification);
            }
        });
    }

    private Notification createNotification(PendingIntent contentIntent) {
        // 定義一個訊息, 第一個參數給它一個圖示, 使用內建的圖示,
        // 再來就是訊息出現在左上角時的標題,
        // 最後一個參數是現在的時間立刻出現。
        int icon = android.R.drawable.ic_delete;
        String title = "Hi";
        String text = "Nice to meet you.";
        long time = System.currentTimeMillis();

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            // 舊版寫法
//            notification = new Notification(icon, text, time);
//            notification.setLatestEventInfo(this, title, text, contentIntent);
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//            notification.defaults = Notification.DEFAULT_SOUND;
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            notification = builder.setContentIntent(contentIntent)//
                    .setSmallIcon(icon).setTicker(text).setWhen(time)//
                    .setAutoCancel(true).setContentTitle(title).setContentText(text)//
                    .setSound(Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "4"))//
                    .setVibrate(new long[] { 0, 100, 200, 300 })// 要加權限
                    .setLights(0xff00ff00, 300, 1000)//
                    .build();
        }
        return notification;
    }

    // 目前這段無效果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("# onActivityResult");
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle = data.getExtras();
        }
        System.out.println("requestCode = " + requestCode);
        System.out.println("resultCode = " + resultCode);
        switch (resultCode) {
        case RESULT_CANCELED:
            System.out.println("RESULT_CANCELED");
            break;
        case RESULT_FIRST_USER:
            System.out.println("RESULT_FIRST_USER");
            break;
        case RESULT_OK:
            System.out.println("RESULT_OK");
            break;
        default:
            break;
        }
    }
}
