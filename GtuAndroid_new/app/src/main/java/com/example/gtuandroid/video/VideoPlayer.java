package com.example.gtuandroid.video;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.gtuandroid.R;

public class VideoPlayer extends Activity {
    VideoView videoView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Create a VideoView widget in the layout file
        // use setContentView method to set content of the activity to the
        // layout file which contains videoView
        this.setContentView(R.layout.activity_videoplayer);

        videoView = (VideoView) this.findViewById(R.id.videoView);

        // add controls to a MediaPlayer like play, pause.
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);

        // Set the path of Video or URI
        videoView.setVideoURI(Uri.parse("/sdcard/recordvideooutput.3gpp"));

        // Set the focus
        videoView.requestFocus();
    }
}
