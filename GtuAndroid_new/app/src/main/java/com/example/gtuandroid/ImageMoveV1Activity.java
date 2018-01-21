package com.example.gtuandroid;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class ImageMoveV1Activity extends Activity {

    private MyView mView;
    private Bitmap mBitmap;
    private int iWidth;
    private int iHeight;
    // For 位置與方向相關屬性
    private float imageX = -1.0f, imageY = -1.0f;
    private boolean bNorth = true;
    // For 計時器
    private Handler mHandler = new Handler();
    private Runnable runDancing = new Runnable() {
        public void run() {
            moving();
        }
    };
    private Timer timer;

    class Task extends TimerTask {
        public void run() {
            execute();
        }

        public synchronized void execute() {
            mHandler.removeCallbacks(runDancing);
            mHandler.post(runDancing);
        }
    }// Task

    private Task task = new Task();

    private synchronized void moving() {
        if (bNorth && imageY - mBitmap.getHeight() / 2 < 0)
            bNorth = false;
        else if (!bNorth && imageY + mBitmap.getHeight() / 2 > iHeight)
            bNorth = true;

        if (bNorth)
            imageY -= 10;
        else
            imageY += 10;
        mView.invalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task = null;
        if (timer != null)
            timer = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mView = new MyView(this);
        setContentView(mView);
        // 初始化計時器
        timer = new Timer();
        task = new Task();
        timer.schedule(task, 1000, 500);
    }

    // View.onDraw()方法
    private class MyView extends View {
        private Paint mPaint;
        private float deltaX = 0f, deltaY = 0f;
        private boolean bImageTouched = false;

        //
        public MyView(Context context) {
            super(context);
            mPaint = new Paint();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            iWidth = w; // 螢幕寬初始化
            iHeight = h; // 螢幕高初始化
        }

        // onDraw() callback方法
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            mPaint.setColor(Color.argb(128, 0, 0, 255));
            canvas.drawCircle(iWidth / 3, iHeight / 3, 20, mPaint);
            if (imageX == -1.0f) {
                imageX = iWidth * 2 / 3;
                imageY = iHeight * 2 / 3;
            }
            canvas.drawBitmap(mBitmap, //
                    imageX - mBitmap.getWidth() / 2, //
                    imageY - mBitmap.getHeight() / 2, //
                    null);
        }

        // 手勢觸控的監聽功能
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (x >= imageX - mBitmap.getWidth() / 2 && //
                        x <= imageX + mBitmap.getWidth() / 2 && //
                        y >= imageY - mBitmap.getWidth() / 2 && //
                        y <= imageY + mBitmap.getHeight() / 2) {
                    deltaX = x - imageX;
                    deltaY = y - imageY;
                    bImageTouched = true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                System.out.println("bImageTouched= " + bImageTouched);
                if (bImageTouched) {
                    imageX = x - deltaX;
                    imageY = y - deltaY;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (bImageTouched)
                    bImageTouched = false;
            }
            // 再描繪的指示
            invalidate();
            return true;
        }
    }
}