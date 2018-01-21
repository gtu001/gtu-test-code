package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import com.example.gtu001.qrcodemaker.custom_dialog.BluetoothConnectDeviceDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by gtu001 on 2017/12/13.
 */

public class TvConnActivity extends Activity {

    private static final String TAG = TvConnActivity.class.getSimpleName();

    public static final int BLUETOOTH_REQUEST_CODE = 99889;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // 一定要是這組

    private ListView listView;
    private BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = createContentView();

        doStartBroadcastReceiver();

        //初始Btn狀態紐
        Button btn1 = new Button(this);
        btn1.setText("藍芽狀態");
        layout.addView(btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBluetoothDevices();
            }
        });

        //初始listView
        listView = new ListView(this);
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(listView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(scrollView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                BluetoothDevice device = (BluetoothDevice) item.get("device");
                BluetoothConnectDeviceDialog dialog = new BluetoothConnectDeviceDialog(TvConnActivity.this);
                Dialog _dialog = dialog.setDevice(device).build();
                _dialog.show();
            }
        });
    }

    private void tryConnectBluetoothDevice(final BluetoothDevice device) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int tryTimes = 0;
                BluetoothSocket socket = null;
                do {
                    try {
                        tryTimes++;
                        if(socket == null){
                            ParcelUuid[] uuids = device.getUuids();
                            socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                        }
                        Log.v(TAG, String.format("嘗試連線 %s : %d 次", device.getName(), tryTimes));
                        socket.connect();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                        try {
                            Thread.sleep(300L);
                        } catch (InterruptedException e1) {
                        }
                    }
                    if (tryTimes >= 3) {
                        Log.v(TAG, String.format("放棄連線 %s : %d 次", device.getName(), tryTimes));
                        break;
                    }
                } while (!socket.isConnected());
            }
        });
        thread.start();
    }

    private SimpleAdapter createSimpleAdapter(Set<BluetoothDevice> devices) {
        List<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (BluetoothDevice device : devices) {
            Log.v(TAG, "device : " + device.getName() + "->" + device.getAddress());
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", null);// 圖像資源的ID
            map.put("item_title", device.getName());
            map.put("item_text", device.getAddress());
            map.put("item_image_check", null);
            map.put("device", device);
            listItem.add(map);
        }
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 資料來源
                R.layout.subview_listview, //
                new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
        );
        return listItemAdapter;
    }

    private void showBluetoothDevices() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        } else {
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

            //嘗試連線
            for(BluetoothDevice device : devices){
                tryConnectBluetoothDevice(device);
            }

            baseAdapter = createSimpleAdapter(devices);
            listView.setAdapter(baseAdapter);
            baseAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置广播信息过滤
     */
    private void doStartBroadcastReceiver() {
        Log.v(TAG, "# doStartBroadcastReceiver");
        try {
            this.unregisterReceiver(mReceiver);
        } catch (Exception ex) {
        }
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        this.registerReceiver(mReceiver, intentFilter);
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        Set<String> mArrayAdapter = new LinkedHashSet<>();

        public void onReceive(Context context, Intent intent) {
            // 當收尋到裝置時
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                // 取得藍芽裝置這個物件
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            }
        }
    };


    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
