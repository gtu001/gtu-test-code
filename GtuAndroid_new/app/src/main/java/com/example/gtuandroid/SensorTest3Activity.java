package com.example.gtuandroid;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SensorTest3Activity extends Activity {

    private SensorManager sm;
    // 需要两个Sensor
    private Sensor aSensor;
    private Sensor mSensor;

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];

    private static final String TAG = "sensor";
    
    private TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        text = (TextView)findViewById(R.id.text);
        back();

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        // 更新显示数据的方法
        calculateOrientation();
    }

    // 再次强调：注意activity暂停的时候释放
    public void onPause() {
        sm.unregisterListener(myListener);
        super.onPause();
    }

    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;
            calculateOrientation();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        // 要经过一次数据格式的转换，转换为度
        values[0] = (float) Math.toDegrees(values[0]);
        Log.i(TAG, values[0] + "");
        // values[1] = (float) Math.toDegrees(values[1]);
        // values[2] = (float) Math.toDegrees(values[2]);

        if (values[0] >= -5 && values[0] < 5) {
            text.setText("正北");
        } else if (values[0] >= 5 && values[0] < 85) {
            text.setText("东北");
        } else if (values[0] >= 85 && values[0] <= 95) {
            text.setText("正东");
        } else if (values[0] >= 95 && values[0] < 175) {
            text.setText("东南");
        } else if ((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175) {
            text.setText("正南");
        } else if (values[0] >= -175 && values[0] < -95) {
            text.setText("西南");
        } else if (values[0] >= -95 && values[0] < -85) {
            text.setText("正西");
        } else if (values[0] >= -85 && values[0] < -5) {
            text.setText("西北");
        }
    }
    
    private void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SensorTest3Activity instance = SensorTest3Activity.this;
                instance.setResult(RESULT_CANCELED, instance.getIntent());
                instance.finish();
            }
        });
    }
}