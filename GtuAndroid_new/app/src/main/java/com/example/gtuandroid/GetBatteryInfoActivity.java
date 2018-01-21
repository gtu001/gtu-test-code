package com.example.gtuandroid;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GetBatteryInfoActivity extends Activity {
    /** Called when the activity is first created. */
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);

        Button back = (Button) findViewById(R.id.back);
        back.setText("註冊廣播/取消廣播");
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                // 首先我們在onCreate註冊一個廣播，然後馬上解除註冊廣播，
                // 原因是因為當我們註冊一個廣播以後，抓取的是電池的電量變化，
                // 而電池的電量隨時在變動，因此只要一註冊，就可以馬上抓到電池的訊息，
                // 所以我們就可以馬上解除註冊了。
                registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            }
        });
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // Level: 電池目前的電量 以Scale為準
            // Scale: 電池最大的電量 通常最大為100
            // Health: 電池的健康狀況 可是使用BatteryManager看常數，Battery_Health_XX就是了
            // Status: 電池的狀態 可以使用BatteryManager看常數，Battery_Status_XX就是了
            // Temperature: 電池的溫度 以0.1為單位，因此要乘上0.1
            // Technology: 電池的類型 例如Li-ion
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int status = intent.getIntExtra("status", 0);
                int health = intent.getIntExtra("health", 0);
                boolean present = intent.getBooleanExtra("present", false);
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 0);
                int icon_small = intent.getIntExtra("icon-small", 0);
                int plugged = intent.getIntExtra("plugged", 0);
                int voltage = intent.getIntExtra("voltage", 0);
                int temperature = intent.getIntExtra("temperature", 0);
                String technology = intent.getStringExtra("technology");

                String statusString = "";
                switch (status) {
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    statusString = "unknown";
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    statusString = "charging";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    statusString = "discharging";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    statusString = "not charging";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    statusString = "full";
                    break;
                }

                String healthString = "";
                switch (health) {
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    healthString = "unknown";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthString = "good";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthString = "overheat";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthString = "dead";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthString = "voltage";
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthString = "unspecified failure";
                    break;
                }

                String acString = "";
                switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_AC:
                    acString = "plugged ac";
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    acString = "plugged usb";
                    break;
                }

                Log.v("status", statusString);
                Log.v("health", healthString);
                Log.v("present", String.valueOf(present));
                Log.v("level", String.valueOf(level));
                Log.v("scale", String.valueOf(scale));
                Log.v("icon_small", String.valueOf(icon_small));
                Log.v("plugged", acString);
                Log.v("voltage", String.valueOf(voltage));
                Log.v("temperature", String.valueOf(temperature));
                Log.v("technology", technology);

                StringBuilder sb = new StringBuilder();
                sb.append("status : " + statusString + "\n");
                sb.append("health : " + healthString + "\n");
                sb.append("present : " + present + "\n");
                sb.append("level : " + level + "\n");
                sb.append("scale : " + scale + "\n");
                sb.append("icon_small : " + icon_small + "\n");
                sb.append("plugged : " + acString + "\n");
                sb.append("voltage : " + voltage + "\n");
                sb.append("temperature : " + temperature + "\n");
                sb.append("technology : " + technology + "\n");

                textView.setText(sb.toString());
                
                unregisterReceiver(this);
            }
        }
    };
}