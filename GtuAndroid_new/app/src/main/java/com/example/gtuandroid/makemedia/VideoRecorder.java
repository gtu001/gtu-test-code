package com.example.gtuandroid.makemedia;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gtuandroid.R;

public class VideoRecorder extends View implements SurfaceHolder.Callback {

    private SurfaceView tempSV;
    private SurfaceHolder mHolder;
    private MediaRecorder mRecoder;
    private ImageView tempIV;
    private boolean bRecording = false;
    
    private static final String TAG = VideoRecorder.class.getSimpleName();

    // For 重播 Video
    private VideoView videoView;
    private Dialog dialog;

    // For 錄影中的紅圈顯示
    private boolean bShown = false;
    private Handler mHandler = new Handler();
    private Runnable runBlinking = new Runnable() {
        public void run() {
            if (tempIV != null) {
                if (!bShown)
                    tempIV.setVisibility(View.VISIBLE);
                else
                    tempIV.setVisibility(View.INVISIBLE);
                bShown = !bShown;
            }
        }
    };
    private Timer timer;

    private class Task extends TimerTask {
        public void run() {
            mHandler.removeCallbacks(runBlinking);
            mHandler.post(runBlinking);
        }
    }// Task

    public Task task = new Task();

    // Create constructor of VideoRecorder Class. In this, get an object of
    // surfaceHolder class by calling getHolder() method. After that add
    // callback to the surfaceHolder. The callback will inform when surface is
    // created/changed/destroyed. Also set surface not to have its own buffers.
    public VideoRecorder(Context context, SurfaceView sv, ImageView iv) {
        super(context);
        this.tempSV = sv;
        this.tempIV = iv;
        initVideo();
        initViews();
    }

    private void initViews() {
        mHolder = tempSV.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initVideo() {
        mRecoder = new MediaRecorder();
        mRecoder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mRecoder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecoder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mRecoder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mRecoder.setOutputFile("/sdcard/myvideo2.3gpp");
    }

    // 按下實體的手機照相按鈕, 非覆寫, 而是讓 MakeMedias 傳過來
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v(TAG, "onKeyDown : " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_CAMERA) {
            if (!bRecording) {
                Log.v(TAG, "onKeyDown start!");
                // initVideo();
                mRecoder.start();
                bRecording = true;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                task = new Task();
                timer.scheduleAtFixedRate(task, 500, 500); // 每半秒閃一次
            } else {
                Log.v(TAG, "onKeyDown end!");
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                tempIV.setVisibility(View.INVISIBLE);
                mRecoder.stop();
                mRecoder.release();
                mRecoder = null;
                bRecording = false;
                //
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_videoplayer);
                dialog.setTitle("Video");
                dialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        ((MakeMedias) getContext()).initRecorderViews(Options.RecordVideo);
                    }
                });
                videoView = (VideoView) dialog.findViewById(R.id.videoView);

                // Set the path of Video or URI
                videoView.setVideoURI(Uri.parse("/sdcard/myvideo2.3gpp"));

                //
                videoView.setOnCompletionListener(new OnCompletionListener() { // 播放完畢恢復視圖元件之顯示
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                videoView.start();
                            }
                        });
                videoView.setOnErrorListener(new OnErrorListener() { // 播放錯誤恢復視圖元件之顯示並訊息告知
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                Log.e("VideoPlayer", "Some Errors Happens!");
                                Toast.makeText(getContext(), "Some Errors Happens!", Toast.LENGTH_LONG).show();
                                videoView.start();
                                return true;
                            }
                        });
                dialog.show();
                videoView.start();
            }
            return true;
        }
        return false;
    }

    // Implement the methods of SurfaceHolder.Callback interface
    public void surfaceCreated(SurfaceHolder holder) {
        mRecoder.setPreviewDisplay(mHolder.getSurface());
        try {
            mRecoder.prepare();
        } catch (Exception e) {
            mRecoder.release();
            mRecoder = null;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mRecoder != null) {
            mRecoder.release();
            mRecoder = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    }
}
