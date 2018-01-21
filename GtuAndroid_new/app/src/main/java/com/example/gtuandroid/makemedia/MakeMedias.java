package com.example.gtuandroid.makemedia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gtuandroid.R;


enum Options {
    TakePicture, RecordVideo, RecordAudio	
}

public class MakeMedias extends Activity {
    
    private static final String TAG = MakeMedias.class.getSimpleName();
    
	private SlidingPanel panel=null;
    private Button [] mButton = new Button[3];
    private Drawable [][] drawable = new Drawable [mButton.length][2];
    private int [][] resDrawId = {  { R.drawable.camera0 ,  R.drawable.camera1 }, 
	                                { R.drawable.video0  ,  R.drawable.video1 }, 
	                                { R.drawable.audio0  ,  R.drawable.audio1 } }; 
    private int [] resButtonId = {  R.id.button1, R.id.button2, R.id.button3 };
    private Options eSwitch = Options.TakePicture, ePreSwitch = Options.TakePicture;
    // 拍照/錄影/錄音共用
    private TextView tv;
    // For 拍照/錄影共用
    private SurfaceView svPreview;
	// For 錄影/錄音共用
    private ImageView mIV; // 閃爍圖案訊號
	private RelativeLayout rl; // 重疊圖案版面
	// For 拍照
    private ImageView iv; // 展示照片用
    private TakePicture tp; //拍照處理之View類別
	// For 錄影
    private VideoRecorder vr; //錄影處理之View類別
    // For 錄音
    private AudioRecorder ar; //錄音處理之View類別
    // For 錄影/錄音中的閃爍圖案訊號顯示
	private Handler mHandler  = new Handler();
	private Runnable runUnHint = new Runnable() {
		public void run() {
			if(tv!=null)
				tv.setVisibility(View.INVISIBLE);
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全螢幕
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 無標題
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 橫式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		// 初始為拍照模式
		initCamera();
    }
        
    private void initSlidingPanel() {
    	panel=(SlidingPanel)findViewById(R.id.panel);
        
        for(int i=0; i<drawable.length; i++) { // 以Drawable形式預備功能鈕上的圖案
		    drawable[i][0] = getResources().getDrawable(resDrawId[i][0]);
		    drawable[i][1] = getResources().getDrawable(resDrawId[i][1]);
	    }
        for(int i=0; i<mButton.length; i++) {
        	mButton[i]=(Button)findViewById(resButtonId[i]); // 功能鈕物件初始化
    	    mButton[i].setOnClickListener(listener); // 設定點擊事件傾聽
    	    if( i == eSwitch.ordinal()) // 根據目前的選擇，設定功能鈕上的圖案
    	    	mButton[i].setCompoundDrawablesWithIntrinsicBounds(drawable[i][1], null, null, null);
    	    else
    	    	mButton[i].setCompoundDrawablesWithIntrinsicBounds(drawable[i][0], null, null, null);
        }
    }
    
    // 相機物件初始化
    private void initCamera() {
    	setContentView(R.layout.activity_makemedia_main);
		initSlidingPanel();
		svPreview = (SurfaceView) findViewById(R.id.svPreview); //SurfaceView物件主要當作預覽畫面
		iv = (ImageView) findViewById(R.id.iv);
		iv.setVisibility(View.INVISIBLE);
		tp = new TakePicture(this, svPreview, iv);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // 點擊照片，可重回相機預覽
				svPreview.setVisibility(View.VISIBLE);
				iv.setVisibility(View.INVISIBLE);
			}});
		tv = (TextView) findViewById(R.id.tv);
		tv.setTextColor(Color.WHITE);
		tv.setText("按下DPAD或Camera拍照，再按一下重拍！");
		tv.setVisibility(View.VISIBLE);
		mHandler.postDelayed(runUnHint, 3000);
    }
    
    // 錄影或錄音物件初始化
    public void initRecorderViews(Options opt) {
		// ImageView for blinking
		rl = new RelativeLayout(this);
		rl.setPadding(20, 20, 0, 0);
		mIV = new ImageView(this);
		mIV.setBackgroundResource(R.drawable.symrec);
		mIV.setVisibility(View.INVISIBLE);
		rl.addView(mIV);
		// Reset content view
		setContentView(R.layout.activity_makemedia_main);
		initSlidingPanel();
		svPreview = (SurfaceView) findViewById(R.id.svPreview); //SurfaceView物件主要當作預覽畫面
		iv = (ImageView) findViewById(R.id.iv);
		tv = (TextView) findViewById(R.id.tv);
		iv.setVisibility(View.INVISIBLE);
		switch(opt) {
		case RecordVideo:
			vr = new VideoRecorder(MakeMedias.this, svPreview, mIV);
			tv.setText("按下DPAD或Camera錄影，再按一下重播！");
			break;
		case RecordAudio:
			svPreview.setVisibility(View.INVISIBLE); // 錄音不需要預覽
			ar = new AudioRecorder(MakeMedias.this, mIV);
			tv.setText("按下DPAD或Camera錄音，再按一下重播！");
			break;
		}
		addContentView(rl, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		tv.setTextColor(Color.WHITE);
		tv.setVisibility(View.VISIBLE);
		mHandler.postDelayed(runUnHint, 3000);
	}
  
    OnClickListener listener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			for(int i=0; i<mButton.length; i++) {
				if(v==mButton[i]) {
					mButton[i].setCompoundDrawablesWithIntrinsicBounds(drawable[i][1], null, null, null);
					eSwitch = Options.values()[i];
					if(ePreSwitch!=eSwitch) {
						if(ePreSwitch == Options.RecordAudio) {
							ar.stopPlayer();
							ar.stopRecorder();
						}
						ePreSwitch = eSwitch;
					}
					switch(eSwitch) {
					case TakePicture:
						initCamera();
						break;
					case RecordVideo:
						initRecorderViews(Options.RecordVideo);
						break;
					case RecordAudio:
						initRecorderViews(Options.RecordAudio);
						break;
					}
				}
				else {
					mButton[i].setCompoundDrawablesWithIntrinsicBounds(drawable[i][0], null, null, null);
				}
				System.out.println("MakeMedias:onClick: "+ eSwitch.ordinal());
			}
		}};
	
	@Override
  	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    Log.v(TAG, "onKeyDown : " + eSwitch);
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
		    Log.v(TAG, "function!");
			switch(eSwitch) {
			case TakePicture: // 拍照
				if(iv.getVisibility() == View.INVISIBLE)
				    tp.camera.autoFocus(tp); //拍照前先自動定焦
				else { // 恢復相機 Preview
					svPreview.setVisibility(View.VISIBLE);
					iv.setVisibility(View.INVISIBLE);
				}
				break;
			case RecordVideo: // 錄影
				vr.onKeyDown(keyCode, event);
				break;
			case RecordAudio: // 錄音
				ar.onKeyDown(keyCode, event);
				break;
			}
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_MENU) {
			System.out.println("MakeMedias:KEYCODE_MENU: "+ eSwitch.ordinal());
			for(int i=0; i<mButton.length; i++) {
				if(i==eSwitch.ordinal()) {
					mButton[i].setCompoundDrawablesWithIntrinsicBounds(drawable[i][1], null, null, null);
				}
				else {
					mButton[i].setCompoundDrawablesWithIntrinsicBounds(drawable[i][0], null, null, null);
				}
			}
			panel.toggle();
		    return true;
	    }
		else if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
            .setTitle("MakeMedias")
            .setMessage("確定結束AP?")
            .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	System.exit(0);
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .show();
		}
	    return super.onKeyDown(keyCode, event);
    }
}