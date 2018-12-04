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
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.Mp3PlayerHandler;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by wistronits on 2018/6/27.
 */

public class UrlPlayerService extends Service {

    private static final String TAG = UrlPlayerService.class.getSimpleName();

    private Context context;
    private Handler handler = new Handler();
    ;

    //↓↓↓↓↓↓↓↓ service logical ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "oncreat");
        context = this.getApplicationContext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderNew;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    //↓↓↓↓↓↓↓↓ business logical ------------------------------------------------------------------------------------------------------------------------------------
    private Mp3PlayerHandler mp3Helper;

    private String startPlay(String url) {
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
        return mp3Helper.isPlaying();
    }

    public void pauseAndResume() {
        mp3Helper.pauseAndResume();
    }

    public void backwardOrBackward(int second){
        mp3Helper.backwardOrBackward(second);
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
        public String startPlay(String url) throws RemoteException {
            return UrlPlayerService.this.startPlay(url);
        }

        @Override
        public String stopPlay() throws RemoteException {
            return UrlPlayerService.this.stopPlay();
        }
    };

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
}
