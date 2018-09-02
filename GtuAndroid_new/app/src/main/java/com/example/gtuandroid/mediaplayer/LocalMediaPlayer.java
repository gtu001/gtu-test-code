package com.example.gtuandroid.mediaplayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gtuandroid.R;

public class LocalMediaPlayer extends Activity {

    public static Context context; // 讓主程式作切換控制之用
    private VideoView mVideoView; // 影片播放器
    private MediaPlayer mMediaPlayer; // 媒體播放器，在此負責聲音媒體
    private Button mButtonImage, mButtonVideo, mButtonAudio; // 三個播放鈕
    private Uri uri; // Uniform resource locator，用於標識某一網際網路資源名稱的字元串
    String[] uris = new String[3];
    public static Bitmap myBitmap; // 提供給另一整合套件4chan-image-browser作圖片展示
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

        LocalMediaPlayer.context = this;
        setContentView(R.layout.activity_mediaplayer_sub);
        initViews(); // 視圖元件初始化
        initUris(); // 檔案Uri初始化

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
                        Log.e("LocalMediaPlayer", "Some Errors Happens!");
                        Toast.makeText(LocalMediaPlayer.this, "Some Errors Happens!", Toast.LENGTH_LONG).show();
                        mVideoView.setVisibility(View.INVISIBLE);
                        showButtons();
                        return true;
                    }
                });

        // Play Image ...
        mButtonImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                File imgFile = new File(uris[MainTabMenu.IMAGE]);
                if (imgFile.exists()) {
                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Log.i("LocalMediaPlayer", imgFile.getAbsolutePath());
                    // 呼叫4chan-image-browser開源套件作圖片展示
                    Intent intent = new Intent(LocalMediaPlayer.this, ExpandImage.class);
                    intent.putExtra("TYPE", MainTabMenu.LOCAL_TYPE);
                    startActivity(intent);
                }
            }
        });

        // Play Video ...
        mButtonVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = Uri.parse(uris[MainTabMenu.VIDEO]);
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
                mMediaPlayer = new MediaPlayer();
                try {
                    mMediaPlayer.setDataSource(uris[MainTabMenu.AUDIO]);
                    mMediaPlayer.prepare();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        iDuration = mMediaPlayer.getDuration();
                        Toast.makeText(LocalMediaPlayer.this, iDuration + " 毫秒", Toast.LENGTH_LONG).show();
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
        mTimebar = (SeekBar) findViewById(R.id.timebar);
    }

    private void showButtons() {
        mButtonImage.setVisibility(View.VISIBLE);
        mButtonVideo.setVisibility(View.VISIBLE);
        mButtonAudio.setVisibility(View.VISIBLE);
    }

    private void hideButtons() {
        mButtonImage.setVisibility(View.INVISIBLE);
        mButtonVideo.setVisibility(View.INVISIBLE);
        mButtonAudio.setVisibility(View.INVISIBLE);
    }

    private void initUris() {
        for (int i = 0; i < uris.length; i++)
            try {
                uris[i] = copyFile(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private String copyFile(int index) throws IOException {
        String[] fileName = { "myimage.jpg", "myvideo.mp4", "myaudio.mp3" };

        File dest = Environment.getExternalStorageDirectory();
        InputStream in = getResources().openRawResource(ResMediaPlayer.iResId[index]);
        OutputStream out = new FileOutputStream(dest + "/" + fileName[index]);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return dest + "/" + fileName[index];
    }
}