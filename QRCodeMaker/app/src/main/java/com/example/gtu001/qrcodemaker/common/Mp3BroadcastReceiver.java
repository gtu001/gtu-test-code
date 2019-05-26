package com.example.gtu001.qrcodemaker.common;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

import org.apache.commons.lang3.StringUtils;

public abstract class Mp3BroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = Mp3BroadcastReceiver.class.getSimpleName();

    public abstract void doMusicPause(Context context, String fromMsg);

    public abstract void doMusicContinue(Context context, String fromMsg);

    public abstract boolean isPlaying(Context context, String fromMsg);


    private static final String CUSTOM_KEYCODE_HEADSETHOOK = "custom.gtu001.KEYCODE_HEADSETHOOK";
    private static final String CUSTOM_KEY = "custom.gtu001.KEY";
    private static final String CUSTOM_MSG_KEY = "custom.gtu001.MSG_KEY";

    private ComponentName mReceiverComponent;

    private PhoneCallBroadcastReceviver mPhoneCallBroadcastReceviver = new PhoneCallBroadcastReceviver();

    //--------------------------------------------------------------------------------------------------
    //MediaKeyBroadcastReceiver
    public void register1(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mReceiverComponent = new ComponentName(context, MediaKeyBroadcastReceiver.class);
        mAudioManager.registerMediaButtonEventReceiver(mReceiverComponent);
    }

    //MediaKeyBroadcastReceiver
    public void unregister1(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.unregisterMediaButtonEventReceiver(mReceiverComponent);
    }
    //--------------------------------------------------------------------------------------------------


    /**
     * 1.boardcast 必須定義於 AndroidManifest.xml
     * 2.必須為 public static , 且必須有 default constructor
     * 3.One activity need to register an MediaButtonEventReceiver to listen for the button presses
     */
    public static class MediaKeyBroadcastReceiver extends BroadcastReceiver {

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

//                MediaKeyBroadcastReceiver.onReceive:75: Action ---->android.intent.action.MEDIA_BUTTON  KeyEvent----->KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MEDIA_PLAY, scanCode=200, metaState=0, flags=0x8, repeatCount=0, eventTime=234273618, downTime=234273587, deviceId=15, source=0x101 }
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
                if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode) {
                    sb.append("KEYCODE_MEDIA_PLAY, ");
                    Mp3BroadcastReceiver.sendBroadcast(context, "continue", "KEYCODE_MEDIA_PLAY");//for藍芽
                }
                if (KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode) {
                    sb.append("KEYCODE_MEDIA_PLAY, ");
                    Mp3BroadcastReceiver.sendBroadcast(context, "pause", "KEYCODE_MEDIA_PAUSE");//for藍芽
                }
                if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
                    sb.append("KEYCODE_HEADSETHOOK, ");
                    Mp3BroadcastReceiver.sendBroadcast(context, "toggle", "KEYCODE_HEADSETHOOK");//for一般耳機
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

    public static class PhoneCallBroadcastReceviver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            // TODO Auto-generated method stub
            System.out.println("action" + intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                //如果是去电（拨出）
                System.out.println("拨出");
            } else {
                //查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电
                System.out.println("来电");
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                tm.listen(new PhoneStateListener() {

                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        // TODO Auto-generated method stub
                        //state 当前状态 incomingNumber,貌似没有去电的API
                        super.onCallStateChanged(state, incomingNumber);
                        System.out.println("来电 : " + state);
                        switch (state) {
                            case TelephonyManager.CALL_STATE_IDLE:
                                System.out.println("挂断");

                                try {
                                    Thread.sleep(1000L);
                                } catch (InterruptedException e) {
                                }
                                Mp3BroadcastReceiver.sendBroadcast(context, "continue", "来电挂断");

                                break;
                            case TelephonyManager.CALL_STATE_OFFHOOK:
                                System.out.println("接听");

                                Mp3BroadcastReceiver.sendBroadcast(context, "pause", "来电接听");
                                break;
                            case TelephonyManager.CALL_STATE_RINGING:
                                System.out.println("响铃:来电号码" + incomingNumber);

                                Mp3BroadcastReceiver.sendBroadcast(context, "pause", "响铃:来电号码" + incomingNumber);
                                //输出来电号码
                                break;
                        }
                    }

                }, PhoneStateListener.LISTEN_CALL_STATE);
                //设置一个监听器
            }
        }
    }

    public static void sendBroadcast(Context context, String command, String message) {
        Intent in = new Intent(CUSTOM_KEYCODE_HEADSETHOOK);
        if (StringUtils.isNotBlank(command)) {
            in.putExtra(CUSTOM_KEY, StringUtils.trimToEmpty(command));
        }
        in.putExtra(CUSTOM_MSG_KEY, StringUtils.trimToEmpty(message));
        context.sendBroadcast(in);
    }

    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "# " + intent.getAction(), 30);
        this.onReceive_Bluetooth(context, intent);
        this.onReceive_plug(context, intent);
        this.onReceive_forward(context, intent);
    }

    public IntentFilter getFilter() {
        IntentFilter intent = new IntentFilter();
        this.appendBluetoothIntent(intent);
        this.appendPlugIntent(intent);
        this.appendPauseIntent(intent);
        return intent;
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


    private void onReceive_Bluetooth(Context context, Intent intent) {
        String action = intent.getAction(); //获取蓝牙设备实例【如果无设备链接会返回null，如果在无实例的状态下调用了实例的方法，会报空指针异常】 //主要与蓝牙设备有关系
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.v(TAG, "监听蓝牙变化");
        switch (action) {
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                Log.v(TAG, "蓝牙设备:" + device.getName() + "已链接");
                this.doMusicContinue(context, "蓝牙设备:" + device.getName() + "已链接");
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                Log.v(TAG, "蓝牙设备:" + device.getName() + "断开链接");
                this.doMusicPause(context, "蓝牙设备:" + device.getName() + "断开链接");
                break; //上面的两个链接监听，其实也可以BluetoothAdapter实现，修改状态码即可
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.v(TAG, "蓝牙关闭");
                        this.doMusicPause(context, "蓝牙关闭");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.v(TAG, "蓝牙开启");
                        this.doMusicContinue(context, "蓝牙开启");
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
                    this.doMusicPause(context, "Headset unplugged");
                    break;
                case 1:
                    Log.d(TAG, "Headset plugged");
                    this.doMusicContinue(context, "Headset plugged");
                    break;
            }
        }
    }

    //轉發 broadcast
    private void onReceive_forward(Context context, Intent intent) {
        if (intent.getAction().equals(CUSTOM_KEYCODE_HEADSETHOOK)) {
            String command = intent.getStringExtra(CUSTOM_KEY);
            String message = StringUtils.trimToEmpty(intent.getStringExtra(CUSTOM_MSG_KEY));

            if ("toggle".equalsIgnoreCase(command)) {
                Log.v(TAG, "[onReceive_pause] : isPlaying : " + this.isPlaying(context, message));
                if (this.isPlaying(context, message)) {
                    this.doMusicPause(context, message);
                    Log.v(TAG, "[onReceive_pause] : doMusicPause ");
                } else {
                    this.doMusicContinue(context, message);
                    Log.v(TAG, "[onReceive_pause] : doMusicContinue ");
                }
            } else if ("pause".equalsIgnoreCase(command)) {
                this.doMusicPause(context, message);
                Log.v(TAG, "[onReceive_pause] : doMusicPause ");
            } else if ("continue".equalsIgnoreCase(command)) {
                this.doMusicContinue(context, message);
                Log.v(TAG, "[onReceive_pause] : doMusicContinue ");
            }
        }
    }
}