package com.example.gtuandroid.makemedia;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.gtuandroid.R;

public class AudioRecorder extends View {

	private MediaRecorder mRecoder;
	private ImageView tempIV;
	private boolean bRecording=false;
	private boolean bFirst=true;
	
	// For 錄音中的紅圈顯示
	private boolean bShown = false;
	private Handler mHandler  = new Handler();
	private Runnable runBlinking = new Runnable() {
		public void run() {
			if(tempIV!=null) {
				if(!bShown)
					tempIV.setVisibility(View.VISIBLE);
				else
					tempIV.setVisibility(View.INVISIBLE);
				bShown = !bShown;
			}
		}
	};
	private Timer timer;
	private class Task extends TimerTask{
		public void run(){
			mHandler.removeCallbacks(runBlinking);
			mHandler.post(runBlinking);
		}
	}// Task
	public Task task = new Task();
	
	// For 播音
	private MediaPlayer mMediaPlayer;
	private AudioManager am;
	private int curVol, maxVol;
	
	public AudioRecorder(Context context, ImageView iv) {
		super(context);
		this.tempIV = iv;
		
		initAudio();
		// 取得  AudioManager
        am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        curVol = maxVol;
	}
	
	private void initAudio() {
		mRecoder = new MediaRecorder();
		mRecoder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecoder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mRecoder.setOutputFile("/sdcard/myaudio2.3gp");
        try {
        	mRecoder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        bFirst = false; 
	}
	
	// 按下實體的手機照相按鈕, 非覆寫, 而是讓 MakeMedias 傳過來
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			if(!bRecording) {
				if(!bFirst) {
				    initAudio();
				    stopPlayer();
				    // 更換閃爍符號
					tempIV.setBackgroundResource(R.drawable.symrec);
				}
				mRecoder.start();
				bRecording=true;
				if(timer!=null) {
					timer.cancel();
					timer = null;
				}
				timer = new Timer();
				task = new Task();
				timer.scheduleAtFixedRate(task, 500 , 500); // 每半秒閃一次
			}
			else {
				if(timer!=null) {
					timer.cancel();
					timer = null;
				}
				tempIV.setVisibility(View.INVISIBLE);
				stopRecorder();
				bRecording=false;
				// 更換閃爍符號
				tempIV.setBackgroundResource(R.drawable.symplay);
				playAudio(curVol, curVol);
				timer = new Timer();
				task = new Task();
				timer.scheduleAtFixedRate(task, 500 , 500); // 每半秒閃一次
			}
			return true;
		}
		return false;
	}	
	private void playAudio(int curVol, int maxVol) {
		mMediaPlayer = new MediaPlayer();
        try {
			mMediaPlayer.setDataSource(getContext(), Uri.parse("/sdcard/myaudio2.3gp"));
			mMediaPlayer.prepare();
			mMediaPlayer.start();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setVolume(curVol*1.0f, curVol*1.0f);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	public void stopPlayer() {
		if(mMediaPlayer!=null) {
			if(mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		}
	}
	public void stopRecorder() {
		if(mRecoder!=null) {
			if(bRecording)
			    mRecoder.stop();
			mRecoder.release();
			mRecoder = null;
		}
	}
}
