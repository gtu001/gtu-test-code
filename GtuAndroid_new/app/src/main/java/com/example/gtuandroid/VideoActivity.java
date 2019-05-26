package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        back();

        VideoView videoView1 = (VideoView) findViewById(R.id.videoView1);

        videoView1.setVideoPath("");
        videoView1.setMediaController(new MediaController(this));
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                VideoActivity.this.setResult(RESULT_CANCELED, VideoActivity.this.getIntent());
                VideoActivity.this.finish();
            }
        });
    }
}
