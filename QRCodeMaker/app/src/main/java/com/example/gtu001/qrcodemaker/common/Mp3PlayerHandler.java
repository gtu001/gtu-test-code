package com.example.gtu001.qrcodemaker.common;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import com.example.gtu001.qrcodemaker.R;

import java.io.File;

/**
 * Created by gtu001 on 2017/10/28.
 */

public class Mp3PlayerHandler {

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

            Uri uri = null;
            if(new File(url).exists()){
                uri = Uri.fromFile(new File(url));
            }else{
                uri = Uri.parse(url);
            }

            mediaplayer.setDataSource(context, uri);
            mediaplayer.prepare();
            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaplayer.stop();
                    mediaplayer.reset();
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            Toast.makeText(context, "mp3讀取錯誤", Toast.LENGTH_SHORT).show();
        }
        return this;
    }

    public void play() {
        mediaplayer.start();
    }

    public void release() {
        mediaplayer.stop();
        mediaplayer.release();
    }
}
