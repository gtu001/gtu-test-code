package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class CommonKeyListenerActivity extends Activity {

    private static final String TAG = CommonKeyListenerActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Toast.makeText(this, "返回按鈕被按下了!", Toast.LENGTH_SHORT).show();
        }

        if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(this, "音量調高了", Toast.LENGTH_SHORT).show();
        }else if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN){
            Toast.makeText(this, "音量調低了", Toast.LENGTH_SHORT).show();
        }else if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_MUTE){
            Toast.makeText(this, "設為靜音了", Toast.LENGTH_SHORT).show();
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onUserLeaveHint(){
        Toast.makeText(this, "Home按鈕被按下了!", Toast.LENGTH_SHORT).show();
    }
}
