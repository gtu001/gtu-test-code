package com.example.gtu001.qrcodemaker.common;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import static android.view.MotionEvent.*;

/**
 * Created by gtu001 on 2017/11/6.
 */

public class ImageButtonImageHelper {

    private static final String TAG = ImageButtonImageHelper.class.getSimpleName();

    int normal;
    int pressed;
    ImageView imageView;
    boolean manualRelease;

    public ImageButtonImageHelper(int normal, int pressed, ImageView imageView) {
        this(normal, pressed, imageView, false);
    }

    public ImageButtonImageHelper(int normal, int pressed, ImageView imageView, boolean manualRelease) {
        this.normal = normal;
        this.pressed = pressed;
        this.imageView = imageView;
        this.manualRelease = manualRelease;
        this.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & ACTION_MASK) {
                    case ACTION_DOWN:
                        Log.v(TAG, "action down");
                        pressedButton();
                        break;
                    case ACTION_UP:
                    case ACTION_CANCEL:
                        Log.v(TAG, "action up");
                        if(ImageButtonImageHelper.this.manualRelease == false){
                            releaseButton();
                        }
                        break;
                    case ACTION_MOVE:
                        Log.v(TAG, "action move");
                        break;
                }
                return false;
            }
        });

        //設定預設圖
        releaseButton();
    }

    public void pressedButton(){
        imageView.setBackgroundResource(pressed);
    }

    public void releaseButton(){
        imageView.setBackgroundResource(normal);
    }
}
