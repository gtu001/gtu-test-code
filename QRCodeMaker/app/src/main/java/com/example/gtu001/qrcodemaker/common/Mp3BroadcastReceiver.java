package com.example.gtu001.qrcodemaker.common;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

public abstract class Mp3BroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = Mp3BroadcastReceiver.class.getSimpleName();

    public abstract void doMusicPause(Context context);

    public abstract void doMusicContinue(Context context);

    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "# " + intent.getAction(), 30);
        this.onReceive_PhoneCall(context, intent);
        this.onReceive_Bluetooth(context, intent);
    }

    public IntentFilter getFilter() {
        IntentFilter intent = new IntentFilter();
        this.appendPhonecall(intent);
        this.appendBluetoothIntent(intent);
        return intent;
    }

    //手機來電
    private void appendPhonecall(IntentFilter intent) {
        intent.addAction("android.intent.action.PHONE_STATE");
        intent.addAction("android.intent.action.NEW_OUTGOING_CALL");
    }

    //藍芽
    private void appendBluetoothIntent(IntentFilter intentFilter) {
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
    }

    private void onReceive_PhoneCall(Context context, Intent intent) {
        String savedNumber = "";
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            doMusicPause(context);
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if (TelephonyManager.EXTRA_STATE_IDLE.equals(stateStr)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(stateStr)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
                doMusicContinue(context);
            } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(stateStr)) {
                state = TelephonyManager.CALL_STATE_RINGING;
                doMusicPause(context);
            }
        }
    }

    private void onReceive_Bluetooth(Context context, Intent intent) {
        String action = intent.getAction(); //获取蓝牙设备实例【如果无设备链接会返回null，如果在无实例的状态下调用了实例的方法，会报空指针异常】 //主要与蓝牙设备有关系
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.v(TAG, "监听蓝牙变化");
        switch (action) {
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                Log.v(TAG, "蓝牙设备:" + device.getName() + "已链接");
                this.doMusicContinue(context);
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                Log.v(TAG, "蓝牙设备:" + device.getName() + "断开链接");
                this.doMusicPause(context);
                break; //上面的两个链接监听，其实也可以BluetoothAdapter实现，修改状态码即可
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.v(TAG, "蓝牙关闭");
                        this.doMusicPause(context);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.v(TAG, "蓝牙开启");
                        this.doMusicContinue(context);
                        break;
                }
                break;
            default:
                break;
        }
    }
}