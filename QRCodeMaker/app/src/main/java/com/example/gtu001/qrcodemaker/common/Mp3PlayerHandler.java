package com.example.gtu001.qrcodemaker.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.Mp3Bean;
import com.example.gtu001.qrcodemaker.Mp3PlayerActivity;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gtu001 on 2017/10/28.
 */

public class Mp3PlayerHandler {

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
    private static final String TAG = Mp3PlayerHandler.class.getSimpleName();

    private MediaPlayer mediaplayer = new MediaPlayer();
    private static MyReplayListObj mMyReplayListObj;
    private AtomicBoolean isMediaInitDone = new AtomicBoolean(true);
    private StoreStatusThread mStoreStatusThread;

    public Mp3PlayerHandler() {
    }

    public void applyOf(String url, Context context) {
        try {
            isMediaInitDone.set(false);
            if (mediaplayer.isPlaying()) {
                mediaplayer.stop();
                mediaplayer.reset();
            }

            if (StringUtils.isNotBlank(url)) {
                mediaplayer = new MediaPlayer();
                Uri uri = Uri.parse(url);

                Log.v(TAG, "url = " + url);
                Log.v(TAG, "uri = " + uri);

                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("User-Agent", DEFAULT_USER_AGENT);

                Log.v(TAG, "context = " + context);
                Log.v(TAG, "headerMap = " + headerMap);

                mediaplayer.setDataSource(context, uri, headerMap);
                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    mediaplayer.prepare();
                } else {
                    mediaplayer.prepareAsync();
                }
            } else {
                Log.v(TAG, "重播ing...");
                mediaplayer.seekTo(0);
            }

            if (mMyReplayListObj == null) {
                mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.v(TAG, "Orign setOnCompletionListener ----");
                        mediaplayer.stop();
                        mediaplayer.reset();
                    }
                });
            } else {
                mediaplayer.setOnCompletionListener(mMyReplayListObj);
            }

            //固定儲存進度
            if (mStoreStatusThread != null) {
                mStoreStatusThread.close();
            }
            mStoreStatusThread = new StoreStatusThread(mMyReplayListObj, context);
            new Timer().schedule(mStoreStatusThread, 0, 10000);

            mediaplayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "onError : " + what + " / " + extra);
                    return false;
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, "mp3讀取錯誤 ex : " + ex.getMessage(), ex);
            throw new RuntimeException("mp3讀取錯誤 ex : " + ex.getMessage(), ex);
        } finally {
            isMediaInitDone.set(true);
        }
    }

    public boolean isPlaying() {
        return mediaplayer.isPlaying();
    }

    public void start() {
        int length = mediaplayer.getCurrentPosition();
        mediaplayer.seekTo(length);
        mediaplayer.start();
    }

    public void pause() {
        mediaplayer.pause();
    }

    public void pauseAndResume() {
        if (mediaplayer.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    public void play() {
        mediaplayer.start();
    }

    public void release() {
        mediaplayer.stop();
        mediaplayer.release();
    }

    public int getCurrentPosition() {
        return mediaplayer.getCurrentPosition();
    }

    public void backwardOrBackward(int second) {
        int length = mediaplayer.getCurrentPosition();
        mediaplayer.seekTo(length + (second * 1000));
        if (mediaplayer.isPlaying()) {
            mediaplayer.start();
        } else {
            mediaplayer.pause();
        }
    }

    public void setReplayMode(Context context, final String currentName, final int currentPosition, final List<Mp3Bean> lst) {
        Log.v(TAG, "setReplayMode size = " + lst.size());
        Log.v(TAG, "# ReplayList ----------- start");
        for (Mp3Bean b : lst) {
            Log.v(TAG, "\t " + ReflectionToStringBuilder.toString(b));
        }
        Log.v(TAG, "# ReplayList ----------- end");

        mMyReplayListObj = new MyReplayListObj(context, this, currentName, currentPosition, lst, true);
        mMyReplayListObj.onCompletion(this.mediaplayer);
    }

    public static class MyReplayListObj implements MediaPlayer.OnCompletionListener {
        String currentName = "";
        String currentPath = "";
        int currentPosition = -1;
        List<Mp3Bean> lst;
        Context context;
        Mp3PlayerHandler mp3PlayerHandler;
        boolean isFirst = false;

        MyReplayListObj(Context context, Mp3PlayerHandler mMp3PlayerHandler, final String currentName, final int currentPosition, final List<Mp3Bean> lst, boolean isFirst) {
            this.context = context;
            this.currentName = currentName;
            this.lst = lst;
            this.mp3PlayerHandler = mMp3PlayerHandler;
            this.isFirst = isFirst;
            this.currentPosition = currentPosition;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.v(TAG, "onCompletion start ...");
            try {
                Log.v(TAG, "# Replaying ...");
                if (lst.isEmpty()) {
                    Log.v(TAG, "# Replaying ... ERROR");
                    return;
                }

                int findIndex = 0;

                if (isFirst || lst.size() == 1) {
                    isFirst = false;

                } else if (lst.size() > 1) {
                    Log.v(TAG, "# Replaying ... ALL");

                    boolean findOk = false;
                    for (int ii = 0; ii < lst.size(); ii++) {
                        Mp3Bean b = lst.get(ii);
                        if (StringUtils.equals(currentName, b.getName()) && (ii + 1 < lst.size())) {
                            findIndex = ii + 1;
                            findOk = true;
                            break;
                        }
                    }

                    if (!findOk) {
                        List<String> notFoundLst = new ArrayList<>();
                        for (int ii = 0; ii < lst.size(); ii++) {
                            Mp3Bean b = lst.get(ii);
                            notFoundLst.add(b.getName());
                        }
                    }
                }

                mp3PlayerHandler.applyOf(lst.get(findIndex).getUrl(), context);
                mp3PlayerHandler.mediaplayer.start();

                if (currentPosition > 0) {
                    mp3PlayerHandler.mediaplayer.seekTo(currentPosition);
                }

                Log.v(TAG, "onCompletion : " + findIndex + "/" + lst.size());

                //設定當前首
                this.currentName = lst.get(findIndex).getName();
                this.currentPath = lst.get(findIndex).getUrl();

            } catch (Exception ex) {
                Log.e(TAG, "onCompletion ERR : " + ex.getMessage(), ex);
            } finally {
                Log.v(TAG, "onCompletion end ...");
            }
        }

        public String getCurrentName() {
            return currentName;
        }

        public String getCurrentPath() {
            return currentPath;
        }
    }

    public void onProgressChange(int percent) {
        Log.v(TAG, "onProgressChange / percent : " + percent);
        if (percent == 0) {
            return;
        }
        double currentPos = this.mediaplayer.getDuration() / 100 * percent;
        this.mediaplayer.seekTo((int) currentPos);
        if (mediaplayer.isPlaying()) {
            mediaplayer.start();
        } else {
            mediaplayer.pause();
        }
    }

    public int getProgressPercent() {
        try {
            if (!isMediaInitDone.get()) {
                return 0;
            }
            double current = (double) this.mediaplayer.getCurrentPosition();
            double duriation = (double) this.mediaplayer.getDuration();
            int percent = (int) (current / duriation * 100);
            return percent;
        } catch (Exception ex) {
            Log.e(TAG, "getProgressPercent ERR " + ex.getMessage(), ex);
            return 0;
        }
    }

    public String getProgressTime() {
        if (!isMediaInitDone.get()) {
            return "";
        }
        String currentTimeStr = DateUtil.wasteTotalTime_HHmmss(this.mediaplayer.getCurrentPosition());
        String allTimeStr = DateUtil.wasteTotalTime_HHmmss(this.mediaplayer.getDuration());
        return currentTimeStr + " / " + allTimeStr;
    }

    public MyReplayListObj getCurrentBean() {
        return mMyReplayListObj;
    }

    private class StoreStatusThread extends TimerTask {
        MyReplayListObj mMyReplayListObj;
        Context context;
        private Timer timer;

        private void close() {
            if (timer != null) {
                timer.cancel();
            }
        }

        public StoreStatusThread(MyReplayListObj mMyReplayListObj, Context context) {
            this.mMyReplayListObj = mMyReplayListObj;
            this.context = context;
            this.timer = new Timer();
        }

        public void run() {
            if (mMyReplayListObj != null) {
                Mp3PlayerActivity.InitListViewHandler.saveCurrentMp3(mMyReplayListObj.currentName, mMyReplayListObj.currentPath, mMyReplayListObj.mp3PlayerHandler.getCurrentPosition(), context);
                Toast.makeText(context, "儲存:" + mMyReplayListObj.currentName + " - " + mMyReplayListObj.mp3PlayerHandler.getCurrentPosition(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
