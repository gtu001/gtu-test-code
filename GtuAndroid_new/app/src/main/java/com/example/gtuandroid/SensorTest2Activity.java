package com.example.gtuandroid;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SensorTest2Activity extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */

    private TextView lab_X;
    private TextView lab_Y;
    private TextView lab_Z;

    private SensorManager sensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayout layout = createContentView();

        TextView text1 = new TextView(this);
        text1.setText("方向 - 資料擷取");
        layout.addView(text1);
        
        lab_X = new TextView(this);
        lab_Y = new TextView(this);
        lab_Z = new TextView(this);
        layout.addView(lab_X);
        layout.addView(lab_Y);
        layout.addView(lab_Z);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetSensor();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 解除感應器註冊
        sensorManager.unregisterListener(this);
    }

    protected void SetSensor() {
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        // 如果有取到該手機的方位感測器，就註冊他。
        if (sensors.size() > 0) {
            // registerListener必須要implements SensorEventListener，
            // 而SensorEventListener必須實作onAccuracyChanged與onSensorChanged
            // 感應器註冊
            sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        float[] values = event.values;
        lab_X.setText("X：" + String.valueOf(values[0]));
        lab_Y.setText("Y：" + String.valueOf(values[1]));
        lab_Z.setText("Z：" + String.valueOf(values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
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