package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class RatingSeekBarActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_seek_bar);
        back();

        final RatingBar ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        final SeekBar seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        final TextView textViewR = (TextView) findViewById(R.id.textView1);
        final TextView textViewS = (TextView) findViewById(R.id.textView2);

        Log.v("tag", "ratingBar1 = " + ratingBar1);
        Log.v("tag", "seekBar1 = " + seekBar1);
        Log.v("tag", "textViewR = " + textViewR);
        Log.v("tag", "textViewS = " + textViewS);

        ratingBar1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar paramRatingBar, float paramFloat, boolean paramBoolean) {
                textViewR.setText("用Rating Bar表示:\n亮度是=" + ratingBar1.getProgress());
            }
        });

        seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean) {
                textViewS.setText("用Seek Bar表示:\n音量是=" + seekBar1.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar paramSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar paramSeekBar) {
            }
        });
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                RatingSeekBarActivity.this.setResult(RESULT_CANCELED, RatingSeekBarActivity.this.getIntent());
                RatingSeekBarActivity.this.finish();
            }
        });
    }
}
