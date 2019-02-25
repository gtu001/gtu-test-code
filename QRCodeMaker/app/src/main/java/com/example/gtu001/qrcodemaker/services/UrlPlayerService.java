package com.example.gtu001.qrcodemaker.services;

import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.IUrlPlayerService;
import com.example.gtu001.qrcodemaker.Mp3Bean;
import com.example.gtu001.qrcodemaker.Mp3PlayerActivity;
import com.example.gtu001.qrcodemaker.R;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.Mp3PlayerHandler;
import com.example.gtu001.qrcodemaker.common.ServiceKeepAliveHelper;
import com.example.gtu001.qrcodemaker.common.SharedPreferencesUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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

    private Context context;
    private Handler handler = new Handler();
    private Mp3Bean currentBean;
    private CurrentBeanHandler currentBeanHandler;
    private List<Mp3Bean> totalLst = new ArrayList<>();
    private MyBoardcastClass myBoardcastClass;

    //↓↓↓↓↓↓↓↓ service logical ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();

        //-----------------------------------------------------------------
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        ServiceKeepAliveHelper.showForegroundNotification(this, Mp3PlayerActivity.class, R.drawable.qr_code_icon, NOTIFICATION, DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss") + "播放開始!!", "Mp3播放");

        //-----------------------------------------------------------------

        Log.i(TAG, "oncreat");
        context = this.getApplicationContext();
        currentBeanHandler = new CurrentBeanHandler();

        myBoardcastClass = new MyBoardcastClass(this);
        myBoardcastClass.register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderNew;
    }

    @Override
    public void onDestroy() {
        this.myBoardcastClass.unregister(this);

        this.stopPlay();
        //-----------------------------------------------------------------
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, "local_service_stopped", Toast.LENGTH_SHORT).show();

        //-----------------------------------------------------------------
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    //↓↓↓↓↓↓↓↓ business logical ------------------------------------------------------------------------------------------------------------------------------------
    private Mp3PlayerHandler mp3Helper;

    private String startPlay(String name, String url) {
        currentBean = new Mp3Bean();
        currentBean.setName(name);
        currentBean.setUrl(url);

        Log.v(TAG, "#---startPlay : " + url);
        if (StringUtils.isBlank(url)) {
            return "檔案錯誤!";
        }
        if (mp3Helper != null) {
            mp3Helper.release();
        }
        mp3Helper = Mp3PlayerHandler.create(context);
        mp3Helper.of(url);
        mp3Helper.play();
        return "";
    }

    private String stopPlay() {
        Log.v(TAG, "#---stopPlay");
        if (mp3Helper == null) {
            return "尚未撥放!";
        }
        mp3Helper.release();
        mp3Helper = null;
        return "";
    }

    public boolean isPlaying() {
        try {
            return mp3Helper.isPlaying();
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("isPlaying ERR : " + ex.getMessage(), ex);
        }
    }

    public void pauseAndResume() {
        try {
            mp3Helper.pauseAndResume();
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("pauseAndResume ERR : " + ex.getMessage(), ex);
        }
    }

    public void backwardOrBackward(int second) {
        try {
            mp3Helper.backwardOrBackward(second);
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("backwardOrBackward ERR : " + ex.getMessage(), ex);
        }
    }

    public boolean isInitDone() {
        try {
            return mp3Helper != null;
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("isInitDone ERR : " + ex.getMessage(), ex);
        }
    }

    public void setReplayMode(Map totalLst) {
        try {
            List<Mp3Bean> lst = new ArrayList<Mp3Bean>();
            if (totalLst != null) {
                Log.v(TAG, "TotalLst size : " + totalLst.size());
                for (Object k : totalLst.keySet()) {
                    String name = (String) k;
                    String url = (String) totalLst.get(k);
                    Mp3Bean b = new Mp3Bean();
                    b.setName(name);
                    b.setUrl(url);
                    Log.v(TAG, "Add TotalLst : " + ReflectionToStringBuilder.toString(b));
                    lst.add(b);
                }
            }
            this.totalLst = lst;
            if (!this.totalLst.isEmpty()) {
                mp3Helper.setReplayMode(this.currentBean.getName(), this.totalLst);
            }
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("setReplayMode ERR : " + ex.getMessage(), ex);
        }
    }

    private static class CurrentBeanHandler {
        public void putBean(Context context, Mp3Bean currentBean) {
            String refKey = UrlPlayerService.class.getName() + "_currentBean";
            SharedPreferencesUtil.putData(context, refKey, "currentBean_name", currentBean.getName());
            SharedPreferencesUtil.putData(context, refKey, "currentBean_url", currentBean.getUrl());
            SharedPreferencesUtil.putData(context, refKey, "currentBean_lastPosition", currentBean.getLastPosition());
        }

        public Mp3Bean getBean(Context context) {
            String refKey = UrlPlayerService.class.getName() + "_currentBean";
            String name = SharedPreferencesUtil.getData(context, refKey, "currentBean_name");
            String url = SharedPreferencesUtil.getData(context, refKey, "currentBean_url");
            String lastPosition = SharedPreferencesUtil.getData(context, refKey, "currentBean_lastPosition");
            Mp3Bean b = new Mp3Bean();
            b.setUrl(url);
            b.setName(name);
            b.setLastPosition(lastPosition);
            return b;
        }
    }

    private Map getCurrentBean() {
        try {
            if (currentBean == null) {
                return currentBeanHandler.getBean(context).toMap();
            } else {
                return currentBean.toMap();
            }
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("getCurrentBean ERR : " + ex.getMessage(), ex);
        }
    }

    public void onProgressChange(int percent) {
        try {
            mp3Helper.onProgressChange(percent);
        } catch (Exception ex) {
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("onProgressChange ERR : " + ex.getMessage(), ex);
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
        public String startPlay(String name, String url) throws RemoteException {
            return UrlPlayerService.this.startPlay(name, url);
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
        public void setReplayMode(Map totalLst) throws RemoteException {
            UrlPlayerService.this.setReplayMode(totalLst);
        }

        @Override
        public void onProgressChange(int percent) throws RemoteException {
            UrlPlayerService.this.onProgressChange(percent);
        }

        public void stopSelf() throws RemoteException {
            UrlPlayerService.this.onDestroy();
        }

        UrlPlayerService getService() {
            return UrlPlayerService.this;
        }
    };

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }

    private static class MyBoardcastClass extends BroadcastReceiver {
        private boolean isResume = false;

        UrlPlayerService urlPlayerService;

        private MyBoardcastClass(UrlPlayerService urlPlayerService) {
            this.urlPlayerService = urlPlayerService;
        }

        private void doMusicPause(Context context) {
            Log.line(TAG, "_____________Broadcast_Pause");
            if (isResume == true) {
                urlPlayerService.pauseAndResume();
                isResume = false;
            }
        }

        private void doMusicContinue(Context context) {
            Log.line(TAG, "_____________Broadcast_Continue");
            if (urlPlayerService.isPlaying()) {
                urlPlayerService.pauseAndResume();
                isResume = true;
            }
        }

        public void onReceive(Context context, Intent intent) {
            //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
            String savedNumber = "";
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = 0;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                    doMusicPause(context);
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                    doMusicContinue(context);
                }
            }

            //TEST
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Log.v(TAG, "Paired");
                    doMusicContinue(context);
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    Log.v(TAG, "Unpaired");
                    doMusicPause(context);
                }
            }
        }

        public void register(Context context) {
            IntentFilter intent = new IntentFilter();
            //手機來電
            intent.addAction("android.intent.action.PHONE_STATE");
            intent.addAction("android.intent.action.NEW_OUTGOING_CALL");

            //藍芽1
            intent.addAction("android.bluetooth.device.action.ACL_CONNECTED");
            intent.addAction("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED");
            intent.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");

            //藍芽2
            intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            context.registerReceiver(this, intent);
        }

        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }
    }
}
