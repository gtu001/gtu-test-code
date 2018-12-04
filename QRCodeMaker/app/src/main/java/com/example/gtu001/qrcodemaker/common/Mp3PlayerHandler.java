package com.example.gtu001.qrcodemaker.common;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import com.example.gtu001.qrcodemaker.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gtu001 on 2017/10/28.
 */

public class Mp3PlayerHandler {

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
    private static final String TAG = Mp3PlayerHandler.class.getSimpleName();

    MediaPlayer mediaplayer = new MediaPlayer();
    Context context;

    private Mp3PlayerHandler(Context context) {
        this.context = context;
    }

    public static Mp3PlayerHandler create(Context context) {
        return new Mp3PlayerHandler(context);
    }

    public Mp3PlayerHandler of(String url) {
        try {
            if (mediaplayer.isPlaying()) {
                mediaplayer.stop();
                mediaplayer.reset();
            }

            Uri uri = Uri.parse(url);

            Log.v(TAG, "url = " + url);
            Log.v(TAG, "uri = " + uri);

            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("User-Agent", DEFAULT_USER_AGENT);

            mediaplayer.setDataSource(context, uri, headerMap);
            mediaplayer.prepare();
            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaplayer.stop();
                    mediaplayer.reset();
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException("mp3讀取錯誤 ex : " + ex.getMessage(), ex);
        }
        return this;
    }

    public boolean isPlaying() {
        return mediaplayer.isPlaying();
    }

    public void pauseAndResume() {
        if (mediaplayer.isPlaying()) {
            mediaplayer.pause();
        } else {
            int length = mediaplayer.getCurrentPosition();
            mediaplayer.seekTo(length);
            mediaplayer.start();
        }
    }

    public void play() {
        mediaplayer.start();
    }

    public void release() {
        mediaplayer.stop();
        mediaplayer.release();
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
}
