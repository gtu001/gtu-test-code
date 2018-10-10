package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.IOException;

public class Mp3PlayerActivity extends Activity {

    private static final String TAG = Mp3PlayerActivity.class.getSimpleName();

    private Button btn;
    /**
     * help to toggle between play and pause.
     */
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    /**
     * remain false till media is not completed, inside OnCompletionListener make it true.
     */
    private boolean intialStage = true;
    private BtnConfig btnConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LinearLayout layout = createContentView();

//        btn = (Button) findViewById(R.id.button1);
        Button btn = new Button(this);
        btn.setText("play");
        layout.addView(btn, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        btn.setOnClickListener(pausePlay);

        btnConfig = new BtnConfig(btn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private class BtnConfig {
        Button btn;
        private BtnConfig(Button btn){
            this.btn = btn;
        }

        private void btn_setPause() {
//            btn.setBackgroundResource(R.drawable.button_pause);
            btn.setText("pause");
        }

        private void btn_setPlay() {
//            btn.setBackgroundResource(R.drawable.button_play);
            btn.setText("play");
        }
    }

    private OnClickListener pausePlay = new OnClickListener() {
        public void onClick(View v) {
            if (!playPause) {
                btnConfig.btn_setPause();
                Toast.makeText(Mp3PlayerActivity.this, "START!!!", Toast.LENGTH_SHORT).show();
                if (intialStage)
                    new Player()
                            .execute("https://r1---sn-ipoxu-un5r.googlevideo.com/videoplayback?dur=538.424&id=o-AJQ9ZVckcZ9Dk6fBOr4wdED6DQd9IJXQW-BFQZ2EDby9&pl=21&ms=au%2Crdu&fvip=1&sparams=dur%2Cei%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&source=youtube&initcwndbps=997500&mv=m&mm=31%2C29&ip=114.45.89.19&mt=1539151373&mn=sn-ipoxu-un5r%2Csn-un57sn7s&expire=1539173060&ipbits=0&mime=video%2Fmp4&ratebypass=yes&itag=22&signature=165F7E3E1C498A2E63FABB25D5A165A701490E05.1AC838F521F5F323E100586250B40AB194257484&lmt=1536682821655962&requiressl=yes&ei=ZJa9W6ecJc-TgQOEv6zgBg&key=yt6&c=WEB");
                else {
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                }
                playPause = true;
            } else {
                Toast.makeText(Mp3PlayerActivity.this, "PAUSE!!!", Toast.LENGTH_SHORT).show();
                btnConfig.btn_setPlay();
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                playPause = false;
            }
        }
    };

    /**
     * preparing mediaplayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
     *
     * @author piyush
     */

    private class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mediaPlayer.setDataSource(params[0]);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        intialStage = true;
                        playPause = false;
                        btnConfig.btn_setPlay();
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            mediaPlayer.start();

            intialStage = false;
        }

        public Player() {
            progress = new ProgressDialog(Mp3PlayerActivity.this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
            this.progress.show();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}