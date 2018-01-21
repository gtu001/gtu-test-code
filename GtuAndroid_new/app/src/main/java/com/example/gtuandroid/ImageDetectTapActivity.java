package com.example.gtuandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class ImageDetectTapActivity extends Activity {
    private static final String TAG = ImageDetectTapActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    private class MyView extends View {
        private Bitmap mBitmap;
        private Region mRegion = new Region();
        private Path mPath = new Path();
        private Paint mPaint = new Paint();

        public MyView(Context context) {
            super(context);
            try {
                InputStream is = getResources().getAssets().open("sirokuma.jpg");
                mBitmap = BitmapFactory.decodeStream(is);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }

            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(3);
            mPaint.setStyle(Paint.Style.STROKE);

            Rect rect = new Rect(100, 100, 300, 300);

            mPath.addRect(new RectF(rect), Path.Direction.CW);
            mRegion.set(rect);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.save();
            canvas.drawBitmap(mBitmap, 0, 0, null);
            canvas.drawPath(mPath, mPaint);
            canvas.restore();
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                if(mRegion.contains((int)ev.getX(), (int)ev.getY())){
                    Toast.makeText(getContext(), "觸碰了", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    }
}
