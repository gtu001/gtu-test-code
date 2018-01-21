package com.example.gtuandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class ImageMyDrawViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyView mView = new MyView(this);
        setContentView(mView);
    }

    private class MyView extends View {
        private Context context;
        private Bitmap mBitmap;
        private Paint mPaint;
        private int iWidth;
        private int iHeight;
        private float imageX = -1.0f, imageY = -1.0f;
        private float deltaX = 0f, deltaY = 0f;
        private boolean bImageTouched = false;

        public MyView(Context context) {
            super(context);
            this.context = context;
            mPaint = new Paint();
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.iWidth = w;
            this.iHeight = h;

            imageX = w * 2 / 3;
            imageY = h * 2 / 3;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            // 畫幾何
            mPaint.setColor(Color.BLUE);
            canvas.drawCircle(iWidth / 3, iHeight / 3, 20, mPaint);
            // 畫文字
            mPaint.setColor(Color.RED);
            canvas.drawText(context.getString(R.string.hello_world), //
                    iWidth / 3, //
                    iHeight / 2, mPaint);
            // 畫圖片
            canvas.drawBitmap(mBitmap, //
                    imageX - mBitmap.getWidth() / 2, //
                    imageY - mBitmap.getHeight() / 2, null);
        }

        // 手勢觸控的監聽功能
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (x >= imageX && //
                        x <= imageX + mBitmap.getWidth() && //
                        y >= imageY && //
                        y <= imageY + mBitmap.getHeight()) {
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
