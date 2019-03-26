package com.example.gtu001.qrcodemaker.common;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

public abstract class Mp3BroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = Mp3BroadcastReceiver.class.getSimpleName();

    public abstract void doMusicPause(Context context);

    public abstract void doMusicContinue(Context context);

    public abstract boolean isPlaying(Context context);

    private MediaKeyBroadcastReceiver mMediaKeyBroadcastReceiver;

    private static final String CUSTOM_KEYCODE_HEADSETHOOK = "custom.gtu001.KEYCODE_HEADSETHOOK";

    public void registerMediaBroadcast(Context context) {
        MediaKeyBroadcastReceiver mMediaKeyBroadcastReceiver = new MediaKeyBroadcastReceiver();
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMediaKeyBroadcastReceiver.mReceiverComponent = new ComponentName(context, MediaKeyBroadcastReceiver.class);
        mAudioManager.registerMediaButtonEventReceiver(mMediaKeyBroadcastReceiver.mReceiverComponent);

    }

    public void unregisterMediaBroadcast(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.unregisterMediaButtonEventReceiver(mMediaKeyBroadcastReceiver.mReceiverComponent);
    }

    /**
     * 1.boardcast 必須定義於 AndroidManifest.xml
     * 2.必須為 public static , 且必須有 default constructor
     * 3.One activity need to register an MediaButtonEventReceiver to listen for the button presses
     */
    public static class MediaKeyBroadcastReceiver extends BroadcastReceiver {

        ComponentName mReceiverComponent;

        public MediaKeyBroadcastReceiver() {
        }

        //媒體鍵
        private void appendMediaKeyIntent() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
        }

        public void onReceive(Context context, Intent intent) {
            // 获得Action
            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                // 获得KeyEvent对象
                KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

                Log.i(TAG, "Action ---->" + intent.getAction() + "  KeyEvent----->" + keyEvent.toString());
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    return;//忽略 ACTION_DOWN , 只處理 ACTION_UP
                }

                // 获得按键字节码
                int keyCode = keyEvent.getKeyCode();
                // 按下 / 松开 按钮
                int keyAction = keyEvent.getAction();
                // 获得事件的时间
                long downtime = keyEvent.getEventTime();

                // 获取按键码 keyCode
                StringBuilder sb = new StringBuilder();
                // 这些都是可能的按键码 ， 打印出来用户按下的键
                if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
                    sb.append("KEYCODE_MEDIA_NEXT, ");
                }
                // 说明：当我们按下MEDIA_BUTTON中间按钮时，实际出发的是 KEYCODE_HEADSETHOOK 而不是
                // KEYCODE_MEDIA_PLAY_PAUSE
                if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode) {
                    sb.append("KEYCODE_MEDIA_PLAY_PAUSE, ");
                }
                if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
                    sb.append("KEYCODE_HEADSETHOOK, ");
                    context.sendBroadcast(new Intent(CUSTOM_KEYCODE_HEADSETHOOK));
                }
                if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
                    sb.append("KEYCODE_MEDIA_PREVIOUS, ");
                }
                if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode) {
                    sb.append("KEYCODE_MEDIA_STOP, ");
                }
                // 输出点击的按键码
                Log.i(TAG, sb.toString());
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "# " + intent.getAction(), 30);
        this.onReceive_PhoneCall(context, intent);
        this.onReceive_Bluetooth(context, intent);
        this.onReceive_plug(context, intent);
        this.onReceive_pause(context, intent);
    }

    public IntentFilter getFilter() {
        IntentFilter intent = new IntentFilter();
        this.appendPhonecall(intent);
        this.appendBluetoothIntent(intent);
        this.appendPlugIntent(intent);
        this.appendPauseIntent(intent);
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

    //插拔耳機
    private void appendPlugIntent(IntentFilter intentFilter) {
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
    }

    //暫停鍵
    private void appendPauseIntent(IntentFilter intentFilter) {
        intentFilter.addAction(CUSTOM_KEYCODE_HEADSETHOOK);
    }

    private void onReceive_PhoneCall(Context context, Intent intent) {
        String savedNumber = "";
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            doMusicPause(context);
        } else if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String stateStr = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
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

    private void onReceive_plug(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Log.d(TAG, "Headset unplugged");
                    this.doMusicPause(context);
                    break;
                case 1:
                    Log.d(TAG, "Headset plugged");
                    this.doMusicContinue(context);
                    break;
            }
        }
    }

    private void onReceive_pause(Context context, Intent intent) {
        if (intent.getAction().equals(CUSTOM_KEYCODE_HEADSETHOOK)) {
            Log.v(TAG, "[onReceive_pause] : isPlaying : " + this.isPlaying(context));
            if (this.isPlaying(context)) {
                this.doMusicPause(context);
                Log.v(TAG, "[onReceive_pause] : doMusicPause ");
            } else {
                this.doMusicContinue(context);
                Log.v(TAG, "[onReceive_pause] : doMusicContinue ");
            }
        }
    }
}