package com.example.gtu001.qrcodemaker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.IUrlPlayerService;
import com.example.gtu001.qrcodemaker.Mp3Bean;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.Mp3PlayerHandler;
import com.example.gtu001.qrcodemaker.common.SharedPreferencesUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wistronits on 2018/6/27.
 */

public class UrlPlayerService extends Service {

    private static final String TAG = UrlPlayerService.class.getSimpleName();

    private Context context;
    private Handler handler = new Handler();
    private Mp3Bean currentBean;
    private CurrentBeanHandler currentBeanHandler;
    private List<Mp3Bean> totalLst = new ArrayList<>();

    //↓↓↓↓↓↓↓↓ service logical ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "oncreat");
        context = this.getApplicationContext();
        currentBeanHandler = new CurrentBeanHandler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderNew;
    }

    @Override
    public void onDestroy() {
        onMyServiceDestory();
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
            Log.line(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("isPlaying ERR : " + ex.getMessage(), ex);
        }
    }

    public void pauseAndResume() {
        try {
            mp3Helper.pauseAndResume();
        } catch (Exception ex) {
            Log.line(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("pauseAndResume ERR : " + ex.getMessage(), ex);
        }
    }

    public void backwardOrBackward(int second) {
        try {
            mp3Helper.backwardOrBackward(second);
        } catch (Exception ex) {
            Log.line(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("backwardOrBackward ERR : " + ex.getMessage(), ex);
        }
    }

    public boolean isInitDone() {
        try {
            return mp3Helper != null;
        } catch (Exception ex) {
            Log.line(TAG, "ERR : " + ex.getMessage(), ex);
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
            Log.line(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("setReplayMode ERR : " + ex.getMessage(), ex);
        }
    }

    public void onMyServiceDestory() {
        try {
            if (currentBean == null) {
                return;
            }
            currentBean.setLastPosition(String.valueOf(mp3Helper.getCurrentPosition()));
            currentBeanHandler.putBean(context, currentBean);
        } catch (Exception ex) {
            Log.line(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("onMyServiceDestory ERR : " + ex.getMessage(), ex);
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
            Log.line(TAG, "ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("getCurrentBean ERR : " + ex.getMessage(), ex);
        }
    }

    public void onProgressChange(int percent) {
        try {
            mp3Helper.onProgressChange(percent);
        } catch (Exception ex) {
            Log.line(TAG, "ERR : " + ex.getMessage(), ex);
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
        public void onMyServiceDestory() throws RemoteException {
            UrlPlayerService.this.onMyServiceDestory();
        }

        @Override
        public void setReplayMode(Map totalLst) throws RemoteException {
            UrlPlayerService.this.setReplayMode(totalLst);
        }

        @Override
        public void onProgressChange(int percent) throws RemoteException {
            UrlPlayerService.this.onProgressChange(percent);
        }
    };

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
}
