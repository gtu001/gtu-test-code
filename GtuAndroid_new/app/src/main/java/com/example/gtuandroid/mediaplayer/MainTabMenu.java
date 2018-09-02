package com.example.gtuandroid.mediaplayer;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.example.gtuandroid.R;

public class MainTabMenu extends TabActivity {
	
	public final static int IMAGE = 0;
	public final static int VIDEO = 1;
	public final static int AUDIO = 2;
	
	public final static int RESOURCE_TYPE = 0;
	public final static int LOCAL_TYPE = 1;
	public final static int STREAM_TYPE = 2;	
	
	private int iCurrentTab = LOCAL_TYPE;	
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // ʽơ
	    
	    setContentView(R.layout.activity_mediaplayer_main);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ResMediaPlayer.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("tab0").setIndicator("Resources",
	                      res.getDrawable(R.drawable.ic_tab1))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the tab2
	    intent = new Intent().setClass(this, LocalMediaPlayer.class);
	    
	    spec = tabHost.newTabSpec("tab1").setIndicator("Locals",
	                      res.getDrawable(R.drawable.ic_tab2)) 
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the tab3
	    intent = new Intent().setClass(this, StreamMediaPlayer.class);
	    
	    spec = tabHost.newTabSpec("tab2").setIndicator("Streams",
	                      res.getDrawable(R.drawable.ic_tab3))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(iCurrentTab);
	    
	    tabHost.setOnTabChangedListener(new OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabId) {
				Log.i("MainTabMenu", tabId);
				switch(iCurrentTab) {
				case RESOURCE_TYPE:
					((ResMediaPlayer) ResMediaPlayer.context).stopMedia();
					break;
				case LOCAL_TYPE:
					((LocalMediaPlayer) LocalMediaPlayer.context).stopMedia();
					break;
				case STREAM_TYPE:
					((StreamMediaPlayer) StreamMediaPlayer.context).stopMedia();
					break;
				}
				Log.v("mainTab", "tabId = " + tabId);
				iCurrentTab = Integer.parseInt(tabId.substring(3, 4));
			}});
	}
}