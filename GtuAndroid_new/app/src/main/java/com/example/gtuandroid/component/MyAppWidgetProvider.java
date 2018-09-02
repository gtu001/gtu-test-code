package com.example.gtuandroid.component;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.gtuandroid.R;

/**
 * 根據我調研的結果,大家都說android.appwidget.action.APPWIDGET_UPDATE是必須要提供的,我試了下,如果不加,
 * 就無法出現在手機小工具列表中。其他action可以根據需要自行添加。
 * 
 * 其中AppWidgetProvider中的幾個回調方法:onEnabled,onDisabled,onDeleted,
 * onUpdated會自動被其onReceive方法在合適的時間調用
 * ,確切來說是,當廣播到來以後,AppWidgetProvider會自動根據廣播的action通過onReceive方法來自動派發廣播
 * ,也就是調用上述幾個方法。android源碼裏說的很清楚:
 */
public class MyAppWidgetProvider extends AppWidgetProvider {

    public static final String TAG = "MyAppWidgetProvider";
    public static final String CLICK_ACTION = "com.example.action.CLICK";
    private static RemoteViews mRemoteViews;

    /**
     * 每刪除一次窗口小部件就調用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.i(TAG, "onDeleted");
        showLogV("onDeleted", context);
    }

    /**
     * 當最後一個該窗口小部件刪除時調用該方法,注意是最後一個
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.i(TAG, "onDisabled");
        showLogV("onDisabled", context);
    }

    /**
     * 當該窗口小部件第一次添加到桌面時調用該方法,可添加多次但只第一次調用
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.i(TAG, "onEnabled");
        showLogV("onEnabled", context);
    }

    /**
     * 接收窗口小部件點擊時發送的廣播
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "onReceive : action = " + intent.getAction());
        showLogV("onReceive : action = " + intent.getAction(), context);

        // 這裏判斷是自己的action,做自己的事情,比如小工具被點擊了要幹啥,這裏是做來一個動畫效果
        if (intent.getAction().equals(CLICK_ACTION)) {
            Toast.makeText(context, "clicked it", Toast.LENGTH_SHORT).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap srcbBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.car4);
                    for (int i = 0; i < 20; i++) {
                        float degree = (i * 90) % 360;
                        mRemoteViews.setImageViewBitmap(R.id.imageView1, rotateBitmap(context, srcbBitmap, degree));
                        Intent intentClick = new Intent();
                        intentClick.setAction(CLICK_ACTION);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
                        mRemoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                        appWidgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), mRemoteViews);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }
    }

    /**
     * 每次窗口小部件被點擊更新都調用一次該方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate");
        final int counter = appWidgetIds.length;
        showLogV("onUpdate : counter = " + counter, context);
        Log.i(TAG, "counter = " + counter);
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * 窗口小部件更新
     * 
     * @param context
     * @param appWidgeManger
     * @param appWidgetId
     */
    private void onWidgetUpdate(Context context, AppWidgetManager appWidgeManger, int appWidgetId) {
        Log.i(TAG, "appWidgetId = " + appWidgetId);
        showLogV("onWidgetUpdate : appWidgetId = " + appWidgetId, context);
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

        // "窗口小部件"點擊事件發送的Intent廣播
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
        appWidgeManger.updateAppWidget(appWidgetId, mRemoteViews);
    }

    private Bitmap rotateBitmap(Context context, Bitmap srcbBitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap tmpBitmap = Bitmap.createBitmap(srcbBitmap, 0, 0, srcbBitmap.getWidth(), srcbBitmap.getHeight(), matrix, true);
        return tmpBitmap;
    }
    
    private void showLogV(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}