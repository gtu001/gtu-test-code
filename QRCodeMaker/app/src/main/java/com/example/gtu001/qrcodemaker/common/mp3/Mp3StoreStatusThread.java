package com.example.gtu001.qrcodemaker.common.mp3;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.Mp3PlayerActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Mp3StoreStatusThread extends TimerTask {
    Mp3PlayerHandler.MyReplayListObj mMyReplayListObj;
    Context context;
    private Timer timer;
    private boolean isClose = false;

    public void close() {
        isClose = true;
    }

    public Mp3StoreStatusThread(Mp3PlayerHandler.MyReplayListObj mMyReplayListObj, Context context) {
        this.mMyReplayListObj = mMyReplayListObj;
        this.context = context;
        this.timer = new Timer();
    }

    public void run() {
        if (isClose && timer != null) {
            this.cancel();
        }
        if (mMyReplayListObj != null) {

            boolean isNameChange = Mp3InitListViewHandler.saveCurrentMp3(//
                    mMyReplayListObj.getCurrentName(),//
                    mMyReplayListObj.getCurrentPath(), //
                    mMyReplayListObj.getMp3PlayerHandlerCurrentPosition(), //
                    context);//

            if (isNameChange) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "儲存狀態:" + mMyReplayListObj.getCurrentName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}