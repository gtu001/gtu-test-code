package com.example.gtuandroid.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gtuandroid.R;

public class ResMediaPlayer extends Activity {

    public static Context context; // 讓主程式作切換控制之用
    private VideoView mVideoView; // 影片播放器
    private MediaPlayer mMediaPlayer; // 媒體播放器，在此負責聲音媒體
    private Button mButtonImage, mButtonVideo, mButtonAudio; // 三個播放鈕
    private ImageView mImageView; // 呈現影像圖片
    private Uri uri; // Uniform resource locator，用於標識某一網際網路資源名稱的字元串
    public static int[] iResId = { R.drawable.myimage, R.raw.myvideo, R.raw.myaudio };
    //
    private int iDuration; // 用來記錄 媒體長度
    private SeekBar mTimebar; // 用來顯示聲音媒體之播放進度
    private Handler mHandler = new Handler();
    private Runnable run = new Runnable() {
        public void run() { // 以另一執行緒更新播放進度
            if (mTimebar != null && mMediaPlayer != null)
                mTimebar.setProgress(mMediaPlayer.getCurrentPosition());
            mHandler.postDelayed(this, 100); // 以遞迴方式更新播放進度
        }
    };

    public void stopMedia() { // 停止播放並恢復視圖元件之顯示
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mVideoView != null)
            mVideoView.setVisibility(View.INVISIBLE);
        if (mTimebar != null) {
            mHandler.removeCallbacks(run);
            mTimebar.setVisibility(View.GONE);
        }
        if (mImageView != null)
            mImageView.setVisibility(View.GONE);
        showButtons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMedia();
        System.exit(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResMediaPlayer.context = this;
        setContentView(R.layout.activity_mediaplayer_sub);
        initViews(); // 視圖元件初始化

        mVideoView.setMediaController(new MediaController(this)); // 啟動內建之播放面版
        mVideoView.setOnCompletionListener(new OnCompletionListener() { // 播放完畢恢復視圖元件之顯示
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mVideoView.setVisibility(View.INVISIBLE);
                        showButtons();
                    }
                });
        mVideoView.setOnErrorListener(new OnErrorListener() { // 播放錯誤恢復視圖元件之顯示並訊息告知
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.e("ResMediaPlayer", "Some Errors Happens!");
                        Toast.makeText(ResMediaPlayer.this, "Some Errors Happens!", Toast.LENGTH_LONG).show();
                        mVideoView.setVisibility(View.INVISIBLE);
                        showButtons();
                        return true;
                    }
                });

        // Play Image ...
        mButtonImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideButtons();
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setBackgroundResource(iResId[MainTabMenu.IMAGE]);
            }
        });
        mImageView.setOnClickListener(new OnClickListener() { // 點擊圖片恢復視圖元件之顯示
                    @Override
                    public void onClick(View v) {
                        mImageView.setVisibility(View.GONE);
                        showButtons();
                    }
                });

        // Play Video ...
        mButtonVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + iResId[MainTabMenu.VIDEO]);
                mVideoView.setVideoURI(uri);
                mVideoView.setVisibility(View.VISIBLE);
                hideButtons();
                mVideoView.start();
            }
        });

        // Play Audio ...
        mButtonAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + iResId[MainTabMenu.AUDIO]);
                mMediaPlayer = MediaPlayer.create(ResMediaPlayer.this, uri);
                mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        iDuration = mMediaPlayer.getDuration();
                        Toast.makeText(ResMediaPlayer.this, iDuration + " 毫秒", Toast.LENGTH_LONG).show();
                        mTimebar.setMax(iDuration);
                        mHandler.postDelayed(run, 100);
                    }
                });

                mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mHandler.removeCallbacks(run);
                        mTimebar.setVisibility(View.GONE);
                        showButtons();
                    }
                });
                mMediaPlayer.start();
                hideButtons();
                mTimebar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initViews() {
        mVideoView = (VideoView) findViewById(R.id.videoView1);
        mButtonImage = (Button) findViewById(R.id.button1);
        mButtonVideo = (Button) findViewById(R.id.button2);
        mButtonAudio = (Button) findViewById(R.id.button3);
        mImageView = (ImageView) findViewById(R.id.imageview1);
        mTimebar = (SeekBar) findViewById(R.id.timebar);
    }

    private void showButtons() { // 顯示三個播放鈕
        if (mButtonImage != null)
            mButtonImage.setVisibility(View.VISIBLE);
        if (mButtonVideo != null)
            mButtonVideo.setVisibility(View.VISIBLE);
        if (mButtonAudio != null)
            mButtonAudio.setVisibility(View.VISIBLE);
    }

    private void hideButtons() { // 隱藏三個播放鈕
        if (mButtonImage != null)
            mButtonImage.setVisibility(View.INVISIBLE);
        if (mButtonVideo != null)
            mButtonVideo.setVisibility(View.INVISIBLE);
        if (mButtonAudio != null)
            mButtonAudio.setVisibility(View.INVISIBLE);
    }
}