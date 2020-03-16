package com.example.gtu001.qrcodemaker.common.mp3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.IUrlPlayerService;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.ServiceUtil;
import com.example.gtu001.qrcodemaker.services.UrlPlayerService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UrlPlayerServiceHander {

    private static final String TAG = UrlPlayerServiceHander.class.getSimpleName();

    private ServiceConnection mConnection;
    private IUrlPlayerService mService;

    public UrlPlayerServiceHander() {
    }

    public boolean isInitOk() {
        if (mService != null && mConnection != null) {
            return true;
        }
        return false;
    }

    public void init(Context context) {
        if (mConnection == null) {
            mConnection = getMConnection(context);
        }
        if (!ServiceUtil.isServiceRunning(context, UrlPlayerService.class)) {
            ServiceUtil.startStopService(true, context, UrlPlayerService.class);
        }
        Intent intent = new Intent(context, UrlPlayerService.class);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Toast.makeText(context, "---init ok 1", Toast.LENGTH_SHORT).show();
    }

    private void close(Context context) {
        context.unbindService(mConnection);
    }

    private ServiceConnection getMConnection(Context context) {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                Log.v(TAG, "[onServiceConnected] called");
                mService = IUrlPlayerService.Stub.asInterface(service);
                Log.v(TAG, "[mService] init " + mService);
                Toast.makeText(context, "---init ok 2", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                Log.v(TAG, "[onServiceDisconnected] called");
                mService = null;
                Log.v(TAG, "[mService] setNull ");
                Toast.makeText(context, "---shutdown ok 2", Toast.LENGTH_SHORT).show();
            }
        };
    }

    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    private boolean tryOk(Context context) throws RemoteException {
        if (mService == null || mConnection == null) {
            Toast.makeText(context, "未初始化1", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            return mService.isAlive();
        } catch (android.os.DeadObjectException ex) {
            mService = null;
            mConnection = null;
            Toast.makeText(context, "未初始化2", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void setReplayMode(final String currentName, final int currentPosition, List<String> nameLst, List<String> pathLst, boolean isRandom, Context context) throws RemoteException {
        if (!tryOk(context)) {
            return;
        }
        mService.setReplayMode(currentName, currentPosition, nameLst, pathLst, isRandom);
    }

    public void pauseAndResume(Context context) throws RemoteException {
        if (!tryOk(context)) {
            return;
        }
        mService.pauseAndResume();
    }

    public boolean isPlaying(Context context) throws RemoteException {
        if (!tryOk(context)) {
            return false;
        }
        return mService.isPlaying();
    }

    public String stopPlay(Context context) throws RemoteException {
        if (!tryOk(context)) {
            return "";
        }
        return mService.stopPlay();
    }

    public void backwardOrBackward(int time, Context context) throws RemoteException {
        if (!tryOk(context)) {
            return;
        }
        mService.backwardOrBackward(time);
    }

    public String startPlay(String name, String url, int currentPosition, Context context) throws RemoteException {
        if (!tryOk(context)) {
            return "";
        }
        return mService.startPlay(name, url, currentPosition);
    }

    public void onProgressChange(int percent, Context context) throws RemoteException {
        if (!tryOk(context)) {
            return;
        }
        mService.onProgressChange(percent);
    }

    public int getProgressPercent(Context context) throws RemoteException {
        if (!tryOk(context)) {
            return -1;
        }
        return mService.getProgressPercent();
    }

    public String getProgressTime(Context context) throws RemoteException {
        if (!tryOk(context)) {
            return "";
        }
        return mService.getProgressTime();
    }

    public Map getCurrentBean(Context context) throws RemoteException {
        if (!tryOk(context)) {
            return Collections.EMPTY_MAP;
        }
        return mService.getCurrentBean();
    }
}