package com.example.gtuandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;

public class ImageMoveV2Activity extends Activity {

    private MySurfaceView mView;
    private Bitmap mBitmap;
    private Matrix mtx = new Matrix();

    // For 位置與方向相關屬性
    private float iWidth, iHeight;
    private float imageX = -1.0f, imageY = -1.0f;
    private float fAngle;
    private float speedY = -10, speedX = -10;
    // For 執行緒
    private Thread mainLoop;
    private Handler mHandler = new Handler();
    private Runnable runDancing = new Runnable() {
        public void run() {
            moving();
        }
    };

    private synchronized void moving() {
        boolean aChanged = false;

        if (imageY < 0 || imageY + mBitmap.getHeight() > iHeight) {
            this.speedY = (-1) * this.speedY; // 碰到上下邊界時，y反向
            aChanged = true;
        }
        imageY += this.speedY;

        if (imageX < 0 || imageX + mBitmap.getWidth() > this.iWidth) {
            this.speedX = (-1) * this.speedX; // 碰到左右邊界時，x反向
            aChanged = true;
        }
        imageX += this.speedX;

        if (aChanged) {
            this.genAngle();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainLoop != null)
            mainLoop.interrupt();
        System.exit(0);
    }

    public void initAngle() {
        fAngle = Math.round(360 * Math.random());
        // fAngle = 45;
        setTitle(fAngle + "度");
    }

    public void genSpeed() {
        this.speedX = (float) Math.cos(this.fAngle / 180 * Math.PI) * 10;
        this.speedY = (float) Math.sin(this.fAngle / 180 * Math.PI) * 10 * (-1);
    }

    public void genAngle() {
        this.fAngle = (float) ((Math.atan2(this.speedY, (-1) * this.speedX) + Math.PI) / Math.PI * 180);
        setTitle(fAngle + "度");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mView = new MySurfaceView(this);
        setContentView(mView);

        this.initAngle();
        this.genSpeed();
    }

    // MySurfaceView.doDraw()方法
    private class MySurfaceView extends SurfaceView implements Runnable {
        private Paint mPaint;

        //
        public MySurfaceView(Context context) {
            super(context);
            mPaint = new Paint();
            // 以執行緒處理座標與繪圖
            mainLoop = new Thread(this);
            mainLoop.start();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            iWidth = w;
            iHeight = h;

            imageX = Math.round((w - mBitmap.getWidth()) * Math.random()) + 0.5f * mBitmap.getWidth();
            imageY = Math.round((h - mBitmap.getHeight()) * Math.random()) + 0.5f * mBitmap.getHeight();

            super.onSizeChanged(w, h, oldw, oldh);
        }

        void doDraw() {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);
                mPaint.setColor(Color.argb(128, 0, 0, 255));
                canvas.drawCircle(iWidth / 3, iHeight / 3, 20, mPaint);
                if (imageX == -1.0f) {
                    imageX = iWidth * 2 / 3;
                    imageY = iHeight * 2 / 3;
                }
                mtx.reset();
                float dg = fAngle;
                dg *= (-1);
                mtx.postRotate(dg + 90); // 再轉至目的方向
                Bitmap rotatedBMP = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), mtx, true);
                canvas.drawBitmap(rotatedBMP, imageX, imageY, null);
                getHolder().unlockCanvasAndPost(canvas);
            }
        }

        public void run() {
            while (true) {
                mHandler.removeCallbacks(runDancing);
                mHandler.post(runDancing); // 處理座標
                doDraw(); // 處理繪圖
            }
        }
    }
}