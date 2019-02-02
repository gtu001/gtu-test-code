package com.example.gtu001.qrcodemaker.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.gtu001.qrcodemaker.Mp3Bean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.HashMap;
import java.util.List;
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

            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.v(TAG, "Orign setOnCompletionListener ----");
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

    public void setReplayMode(final String currentName, final List<Mp3Bean> lst) {
        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.v(TAG, "# Replaying ...");
                if (lst.isEmpty()) {
                    Log.v(TAG, "# Replaying ... ERROR");
                    return;
                }
                if (lst.size() == 1) {
                    Log.v(TAG, "# Replaying ... ONE");
                    of("");//播放同一首
                    mediaplayer.start();
                } else {
                    Log.v(TAG, "# Replaying ... ALL");
                    int findIndex = 0;
                    for (int ii = 0; ii < lst.size(); ii++) {
                        Mp3Bean b = lst.get(ii);
                        if (StringUtils.equals(currentName, b.getName()) && (ii + 1 < lst.size())) {
                            findIndex = ii + 1;
                            break;
                        }
                    }
                    of(lst.get(findIndex).getUrl());
                    mediaplayer.start();
                }
            }
        });
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
}
