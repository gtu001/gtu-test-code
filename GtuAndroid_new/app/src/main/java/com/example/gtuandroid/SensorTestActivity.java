package com.example.gtuandroid;

import gtu.reflect.ToStringUtil;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SensorTestActivity extends Activity implements SensorEventListener {

    private static final String TAG = SensorTestActivity.class.getSimpleName();

    private TextView text_x;
    private TextView text_y;
    private TextView text_z;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float gravity[] = new float[3];

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayout layout = createContentView();

        TextView text1 = new TextView(this);
        text1.setText("三軸加速度 - 資料擷取");
        layout.addView(text1);
        
        text_x = new TextView(this);
        text_y = new TextView(this);
        text_z = new TextView(this);
        layout.addView(text_x);
        layout.addView(text_y);
        layout.addView(text_z);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // 加速度：Sensor.TYPE_ACCELEROMETER
        // 重力：Sensor.TYPE_GRAVITY
        // 磁場：Sensor.TYPE_MAGNETIC_FIELD
        // 方向：Sensor.TYPE_ORIENTATION
        // 陀螺儀：Sensor.TYPE_GYROSCOPE
        // 亮度：Sensor.TYPE_LIGHT
        // 壓力：Sensor.TYPE_PRESSURE
        // 溫度：Sensor.TYPE_TEMPERATURE
        // 接近：Sensor.TYPE_PROXIMITY
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Sensor.manager.SENSOR_DELAY_FASTEST 0ms
        // Sensor.manager.SENSOR_DELAY_GAME 20ms
        // Sensor.manager.SENSOR_DELAY_UI 60ms
        // Sensor.manager.SENSOR_DELAY_NORMAL 200ms
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // 當感測器的值有所改變
    @Override
    public void onSensorChanged(SensorEvent event) {
        gravity[0] = event.values[0];
        gravity[1] = event.values[1];
        gravity[2] = event.values[2];
        text_x.setText("X = " + gravity[0]);
        text_y.setText("Y = " + gravity[1]);
        text_z.setText("Z = " + gravity[2]);
    }

    // 感測器精準度改變時
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v(TAG, ToStringUtil.toString(sensor));
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        /* 取消註冊SensorEventListener */
        sensorManager.unregisterListener(this);
        Toast.makeText(this, "Unregister accelerometerListener", Toast.LENGTH_LONG).show();
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