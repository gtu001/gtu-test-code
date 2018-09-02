package com.example.gtuandroid.mediaplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.gtuandroid.R;

public class ExpandImage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getIntent().getExtras();
		int iType = b.getInt("TYPE");
		
		setContentView(R.layout.activity_mediaplayer_expandimage);
		TouchImageView tiv = (TouchImageView) findViewById(R.id.touchimageview);

		Bitmap img = null;
		if(iType==MainTabMenu.LOCAL_TYPE)
			img = LocalMediaPlayer.myBitmap;
		else if(iType==MainTabMenu.STREAM_TYPE)
			img = StreamMediaPlayer.myBitmap; 
		
		if (img == null){
			ExpandImage.printDebug("	ExpandImage: img is null");
		}else{
		    tiv.setImage(img, tiv.getWidth(), tiv.getHeight());
	        tiv.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                ExpandImage.this.finish();
	            }
	        });
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		ExpandImage.printDebug("onConfigurationChanged()");
	}
	
	public static final boolean isDebug = true;
	public synchronized static void printDebug(String str) {
		if (isDebug) {
			System.out.println(str);
		}
	}
}
