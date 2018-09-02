package com.example.gtuandroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class CameraReceiverActivity extends Activity {

    private TakeNewPictureReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout contentView = createContentView();
        
        Button button1 = new Button(this);
        contentView.addView(button1);
        button1.setText("NEW_PICTURE");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.android.camera.NEW_PICTURE");
                sendBroadcast(intent);
            }
        });
    }

    public static class TakeNewPictureReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //abortBroadcast();
            Log.d("New Photo Clicked", ">");
            Cursor cursor = context.getContentResolver().query(intent.getData(), null, null, null, null);
            cursor.moveToFirst();
            String image_path = cursor.getString(cursor.getColumnIndex("_data"));
            Toast.makeText(context, "New Photo is Saved as : " + image_path, 1000).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        myReceiver = new TakeNewPictureReceiver();
        IntentFilter i = new IntentFilter("android.intent.action.CAMERA_BUTTON");
        registerReceiver(myReceiver, i);
    }

    @Override
    public void onResume() {
        IntentFilter i = new IntentFilter("android.intent.action.CAMERA_BUTTON");
        registerReceiver(myReceiver, i);
        super.onResume();
    }

    @Override
    public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
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