package com.example.gtuandroid;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.VideoView;

import com.example.gtuandroid.component.TextToSpeechComponent;

public class MediaTestActivity extends Activity {

    private static final String TAG = MediaTestActivity.class.getSimpleName();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout layout = createContentView();

        Button button = new Button(this);
        button.setText("play video");
        layout.addView(button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.myvideo);
                if (!mp.isPlaying()) {
                    mp.start();
                } else {
                    mp.stop();
                }
            }
        });

        Button button1 = new Button(this);
        button1.setText("play video online");
        layout.addView(button1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MediaPlayer mp = new MediaPlayer();
                    String uri = "android.resource://" + getPackageName() + "/" + R.raw.myaudio;
                    mp.setDataSource(uri);
                    mp.prepare();
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button button2 = new Button(this);
        button2.setText("play video intent");
        layout.addView(button2);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String uri = "android.resource://" + getPackageName() + "/" + R.raw.myvideo;
                intent.setDataAndType(Uri.parse(uri), "video/mp4");
                startActivity(intent);
                if (isCallable(intent)) {
                    // call the intent as you intended.
                    startActivity(intent);
                } else {
                    // make alternative arrangements.
                    Log.v(TAG, "not intent..");
                }
            }
        });

        Button button3 = new Button(this);
        button3.setText("play video videoView");
        layout.addView(button3);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFormat(PixelFormat.TRANSLUCENT);

                final VideoView videoHolder = new VideoView(MediaTestActivity.this);
                videoHolder.setMediaController(new android.widget.MediaController(MediaTestActivity.this));
                String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
                videoHolder.setVideoURI(Uri.parse(vidAddress));
                videoHolder.requestFocus();
                videoHolder.start();

                AlertDialog.Builder builder = new AlertDialog.Builder(MediaTestActivity.this);
                builder.setView(videoHolder);
                builder.setPositiveButton("STOP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        videoHolder.stopPlayback();
                    }
                }).show();
            }
        });

        Button button4 = new Button(this);
        button4.setText("Text to speech");
        layout.addView(button4);
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextToSpeechComponent talker = TextToSpeechComponent.getInstance(getApplicationContext());
                talker.speak("Hello world");
            }
        });
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
