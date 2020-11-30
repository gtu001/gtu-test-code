package com.example.gtu001.qrcodemaker.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.IUrlPlayerService;
import com.example.gtu001.qrcodemaker.Mp3Bean;
import com.example.gtu001.qrcodemaker.Mp3PlayerActivity;
import com.example.gtu001.qrcodemaker.R;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.Mp3BroadcastReceiver;
import com.example.gtu001.qrcodemaker.common.mp3.Mp3PlayerHandler;
import com.example.gtu001.qrcodemaker.common.ServiceKeepAliveHelper;
import com.example.gtu001.qrcodemaker.util.RandomUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wistronits on 2018/6/27.
 */

public class UrlPlayerService extends Service {

    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = -9999;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        UrlPlayerService getService() {
            return UrlPlayerService.this;
        }
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    //-----------------------------------------------------------------

    private static final String TAG = UrlPlayerService.class.getSimpleName();

    private Handler handler = new Handler();
    private List<Mp3Bean> totalLst = new ArrayList<>();

    private MyMp3BroadcastReceiver mMp3BroadcastReceiver;

    //↓↓↓↓↓↓↓↓ service logical ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        Log.i(TAG, "----------------------onCreate");
        super.onCreate();

        //-----------------------------------------------------------------
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        ServiceKeepAliveHelper.showForegroundNotification(this, Mp3PlayerActivity.class, R.drawable.qr_code_icon, NOTIFICATION, DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss") + "播放開始!!", "Mp3播放");

        //-----------------------------------------------------------------

        mMp3BroadcastReceiver = new MyMp3BroadcastReceiver(this);
        this.registerReceiver(mMp3BroadcastReceiver, mMp3BroadcastReceiver.getFilter());
        mMp3BroadcastReceiver.register1(this.getApplicationContext());

        Log.i(TAG, "oncreat");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "----------------------onBind");
        return mBinderNew;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "----------------------onDestroy");

        this.stopPlay();

        this.unregisterReceiver(mMp3BroadcastReceiver);
        mMp3BroadcastReceiver.unregister1(this.getApplicationContext());
        //-----------------------------------------------------------------
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, "local_service_stopped", Toast.LENGTH_SHORT).show();

        //-----------------------------------------------------------------
        super.onDestroy();
    }

    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "----------------------onUnbind");
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        Log.i(TAG, "----------------------onRebind");
        super.onRebind(intent);
    }

    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "----------------------onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "----------------------onStartCommand");
        return START_STICKY;
    }


    //↓↓↓↓↓↓↓↓ business logical ------------------------------------------------------------------------------------------------------------------------------------
    private Mp3PlayerHandler mp3Helper;

    private Mp3PlayerHandler getMp3Helper() {
        if (mp3Helper == null) {
            mp3Helper = new Mp3PlayerHandler();
        }
        return mp3Helper;
    }

    private String startPlay(String name, String url, int currentPosition) {
        Log.v(TAG, "#---startPlay : " + url);
        List<Mp3Bean> lst = new ArrayList<>();
        this.totalLst = lst;

        Mp3Bean bean = new Mp3Bean();
        bean.setName(name);
        bean.setUrl(url);
        lst.add(bean);
        getMp3Helper().setReplayMode(this.getApplicationContext(), name, currentPosition, lst);
        return "";
    }

    private String stopPlay() {
        Log.v(TAG, "#---stopPlay");
        getMp3Helper().release();
        return "";
    }

    public boolean isPlaying() {
        try {
            return getMp3Helper().isPlaying();
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("isPlaying ERR : " + ex.getMessage(), ex);
        }
    }

    public void pauseAndResume() {
        try {
            getMp3Helper().pauseAndResume();
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("pauseAndResume ERR : " + ex.getMessage(), ex);
        }
    }

    public void start() {
        try {
            getMp3Helper().start();
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("start ERR : " + ex.getMessage(), ex);
        }
    }

    public void pause() {
        try {
            getMp3Helper().pause();
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("pause ERR : " + ex.getMessage(), ex);
        }
    }

    public void backwardOrBackward(int second) {
        try {
            getMp3Helper().backwardOrBackward(second);
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("backwardOrBackward ERR : " + ex.getMessage(), ex);
        }
    }

    public boolean isInitDone() {
        try {
            getMp3Helper();
            return true;
        } catch (NullPointerException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return false;
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("isInitDone ERR : " + ex.getMessage(), ex);
        }
    }

    public void setReplayMode(String currentName, int currentPosition, List<String> nameLst, List<String> pathLst, boolean isRandom) {
        try {
            List<Mp3Bean> lst = new ArrayList<Mp3Bean>();
            int firstIndex = 0;
            if (totalLst != null) {
                Log.v(TAG, "TotalLst size : " + totalLst.size());
                for (int ii = 0; ii < nameLst.size(); ii++) {
                    String name = (String) nameLst.get(ii);
                    String url = (String) pathLst.get(ii);
                    Mp3Bean b = new Mp3Bean();
                    b.setName(name);
                    b.setUrl(url);
                    Log.v(TAG, "Add TotalLst : " + ReflectionToStringBuilder.toString(b));
                    lst.add(b);

                    if (StringUtils.isNotBlank(currentName) && StringUtils.equals(currentName, name)) {
                        firstIndex = ii;
                    }
                }
            }

            if (isRandom) {
                lst = RandomUtil.randomList(lst);
            }

            this.totalLst = lst;

            if (!this.totalLst.isEmpty()) {
                String firstName = this.totalLst.get(firstIndex).getName();
                getMp3Helper().setReplayMode(this.getApplicationContext(), firstName, currentPosition, this.totalLst);
            }
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("setReplayMode ERR : " + ex.getMessage(), ex);
        }
    }

    private Map getCurrentBean() {
        try {
            Map<String, String> map = new HashMap<>();
            if (getMp3Helper().getCurrentBean() == null) {
                return map;
            }
            map.put("name", getMp3Helper().getCurrentBean().getCurrentName());
            map.put("path", getMp3Helper().getCurrentBean().getCurrentPath());
            return map;
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("getCurrentBean ERR : " + ex.getMessage(), ex);
        }
    }

    public void onProgressChange(int percent) {
        try {
            getMp3Helper().onProgressChange(percent);
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("onProgressChange ERR : " + ex.getMessage(), ex);
        }
    }

    public int getProgressPercent() {
        try {
            return getMp3Helper().getProgressPercent();
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("getProgressPercent ERR : " + ex.getMessage(), ex);
        }
    }

    public String getProgressTime() {
        try {
            return getMp3Helper().getProgressTime();
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("getProgressTime ERR : " + ex.getMessage(), ex);
        }
    }

    private IUrlPlayerService.Stub mBinderNew = new IUrlPlayerService.Stub() {
        @Override
        public boolean isPlaying() throws RemoteException {
            return UrlPlayerService.this.isPlaying();
        }

        @Override
        public void pauseAndResume() throws RemoteException {
            UrlPlayerService.this.pauseAndResume();
        }

        @Override
        public void backwardOrBackward(int second) throws RemoteException {
            UrlPlayerService.this.backwardOrBackward(second);
        }

        @Override
        public String startPlay(String name, String url, int currentPosition) throws RemoteException {
            return UrlPlayerService.this.startPlay(name, url, currentPosition);
        }

        @Override
        public String stopPlay() throws RemoteException {
            return UrlPlayerService.this.stopPlay();
        }

        @Override
        public boolean isInitDone() throws RemoteException {
            return UrlPlayerService.this.isInitDone();
        }

        @Override
        public Map getCurrentBean() throws RemoteException {
            return UrlPlayerService.this.getCurrentBean();
        }

        @Override
        public void setReplayMode(String currentName, int currentPosition, List<String> nameLst, List<String> pathLst, boolean isRandom) throws RemoteException {
            UrlPlayerService.this.setReplayMode(currentName, currentPosition, nameLst, pathLst, isRandom);
        }

        @Override
        public void onProgressChange(int percent) throws RemoteException {
            UrlPlayerService.this.onProgressChange(percent);
        }

        @Override
        public int getProgressPercent() throws RemoteException {
            return UrlPlayerService.this.getProgressPercent();
        }

        @Override
        public String getProgressTime() throws RemoteException {
            return UrlPlayerService.this.getProgressTime();
        }

        public void stopSelf() throws RemoteException {
            UrlPlayerService.this.onDestroy();
        }

        @Override
        public void start() throws RemoteException {
            UrlPlayerService.this.start();
        }

        @Override
        public void pause() throws RemoteException {
            UrlPlayerService.this.pause();
        }

        @Override
        public boolean isAlive() throws RemoteException {
            return true;
        }

        UrlPlayerService getService() {
            return UrlPlayerService.this;
        }
    };

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }

    private class MyMp3BroadcastReceiver extends Mp3BroadcastReceiver {

        UrlPlayerService self;

        private MyMp3BroadcastReceiver(UrlPlayerService self) {
            this.self = self;
        }

        public void doMusicPause(Context context, String fromMsg) {
            Log.v(TAG, fromMsg + "_____________Broadcast_Pause");
            if (!self.isInitDone()) {
                return;
            }
            pause();

        }

        public void doMusicContinue(Context context, String fromMsg) {
            Log.v(TAG, fromMsg + "_____________Broadcast_Continue");
            if (!self.isInitDone()) {
                return;
            }
            if (!self.isPlaying()) {
                start();
            }
        }

        public boolean isPlaying(Context context, String fromMsg) {
            Log.v(TAG, fromMsg + "_____________Broadcast_isPlaying");
            if (!self.isInitDone()) {
                return false;
            }
            return self.isPlaying();
        }
    }
}
