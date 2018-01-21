package com.example.gtuandroid;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class SensorBudaowActivity extends Activity implements SensorEventListener {

    private GSensitiveView gsView;
    private SensorManager sm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);

        gsView = new GSensitiveView(this);
        setContentView(gsView);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        sm.unregisterListener(this);
        super.onDestroy();
    }

    public void onSensorChanged(SensorEvent event) {
        if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
            return;
        }

        float[] values = event.values;
        float ax = values[0];
        float ay = values[1];

        double g = Math.sqrt(ax * ax + ay * ay);
        double cos = ay / g;
        if (cos > 1) {
            cos = 1;
        } else if (cos < -1) {
            cos = -1;
        }
        double rad = Math.acos(cos);
        if (ax < 0) {
            rad = 2 * Math.PI - rad;
        }

        int uiRot = getWindowManager().getDefaultDisplay().getRotation();
        double uiRad = Math.PI / 2 * uiRot;
        rad -= uiRad;

        gsView.setRotation(rad);
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    private static class GSensitiveView extends ImageView {
        private Bitmap image;
        private double rotation;
        private Paint paint;

        public GSensitiveView(Context context) {
            super(context);
            BitmapDrawable drawble = (BitmapDrawable) context.getResources().getDrawable(R.drawable.budaow);
            image = drawble.getBitmap();

            paint = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // super.onDraw(canvas);

            double w = image.getWidth();
            double h = image.getHeight();

            Rect rect = new Rect();
            getDrawingRect(rect);

            int degrees = (int) (180 * rotation / Math.PI);
            canvas.rotate(degrees, rect.width() / 2, rect.height() / 2);
            canvas.drawBitmap(image, //
                    (float) ((rect.width() - w) / 2),//
                    (float) ((rect.height() - h) / 2),//
                    paint);
        }

        public void setRotation(double rad) {
            rotation = rad;
            invalidate();
        }
    }
}