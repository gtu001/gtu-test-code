package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class TapGestureDetectActivity extends Activity  {

    private static final String TAG = TapGestureDetectActivity.class.getSimpleName();

    GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blank);

        mGestureDetector = new GestureDetector(this, mOnGestureListener);
        mGestureDetector.setOnDoubleTapListener(mOnGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //使用gestureDetector#onTouchEvent方法來判別,區分點擊事件
        mGestureDetector.onTouchEvent(event);
        return false;
    }

    private final GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                final int SWIPE_MIN_DISTANCE = 120;
                final int SWIPE_THRESHOLD_VELOCITY = 200;

                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(TapGestureDetectActivity.this, "Right to left", Toast.LENGTH_SHORT).show();
                    return false; // Right to left
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(TapGestureDetectActivity.this, "Left to right", Toast.LENGTH_SHORT).show();
                    return false; // Left to right
                }

                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(TapGestureDetectActivity.this, "Bottom to top", Toast.LENGTH_SHORT).show();
                    return false; // Bottom to top
                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(TapGestureDetectActivity.this, "Top to bottom", Toast.LENGTH_SHORT).show();
                    return false; // Top to bottom
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Toast.makeText(TapGestureDetectActivity.this, "執行了單點擊", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(TapGestureDetectActivity.this, "執行了雙點擊", Toast.LENGTH_SHORT).show();
            return false;
        }

        public void onLongPress(MotionEvent e){
            Toast.makeText(TapGestureDetectActivity.this, "被長時間按住了", Toast.LENGTH_SHORT).show();
        }
    };
}
