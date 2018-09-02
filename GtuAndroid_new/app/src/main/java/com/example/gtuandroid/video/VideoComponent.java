package com.example.gtuandroid.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gtuandroid.R;

/*To allow recording video, add permission into the application's manifest file. i.e. 
 Add <uses-permission android:name="android.permission.RECORD_VIDEO"/>*/

//class which gives choice to start video player or  video recorder
public class VideoComponent extends Activity {
    private Button playerbtn;// button to start player
    private Button recorderbtn;// button for recorder

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_main);
        playerbtn = (Button) findViewById(R.id.player);
        recorderbtn = (Button) findViewById(R.id.recorder);

        // start the VideoPlayer on the click of the button
        playerbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(VideoComponent.this, VideoPlayer.class);
                startActivity(intent);

            }
        });

        // start the VideoRecorder on the click of the button
        recorderbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(VideoComponent.this, VideoRecorder.class);
                startActivity(intent);
            }
        });
    }
}