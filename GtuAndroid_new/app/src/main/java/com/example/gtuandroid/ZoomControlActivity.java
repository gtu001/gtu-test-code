package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ZoomControls;

/**
 * 放大縮小
 */
public class ZoomControlActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_test);

        final TextView contents = (TextView) findViewById(R.id.contents);
        final ZoomControls zoomControl = (ZoomControls) findViewById(R.id.zoomControl);

        zoomControl.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomControl.setZoomSpeed(1000L);
                float scaleX = contents.getScaleX();
                scaleX += 0.1;
                if(scaleX > 4){
                    scaleX = 4;
                }
                contents.setScaleX(scaleX);
                contents.setScaleY(scaleX);
                contents.setText(String.valueOf(scaleX));
            }
        });

        zoomControl.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomControl.setZoomSpeed(1000L);
                float scaleX = contents.getScaleX();
                scaleX -= 0.1;
                if(scaleX <= 1){
                    scaleX = 1;
                }
                contents.setScaleX(scaleX);
                contents.setScaleY(scaleX);
                contents.setText(String.valueOf(scaleX));
            }
        });
    }
}
