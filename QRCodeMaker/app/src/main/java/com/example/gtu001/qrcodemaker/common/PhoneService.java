package com.example.gtu001.qrcodemaker.common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.config.Constant;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;

public class PhoneService extends Service {

    private static final String TAG = PhoneService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneListener(),
                PhoneStateListener.LISTEN_CALL_STATE); // 注册监听器 监听电话状态
        //telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_NONE);//取消註冊
    }

    private void toastShot(String message) {
        try {
            Log.v(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }


    private final class PhoneListener extends PhoneStateListener {
        private String incomeNumber; // 来电号码
        private File file;
        private MediaRecorderAudioHelper audioHelper;
        private AudioRecordHelper audioHelper2;

        private File getRecordFile() {
            String name = this.incomeNumber + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + MediaRecorderAudioHelper.RECORDFILE_EXTENSTION;
            return FileConstantAccessUtil.getFile(PhoneService.this.getApplicationContext(), new File(Constant.RECORD_DIR, name));
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                Log.v(TAG, ">>>>>>CallState>>>>>>>>" + state);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: // 来电
                        Log.v(TAG, ">>>>>>来电>>>>>>>>" + state);
                        toastShot("撥號中~~準備監聽!");
                        this.incomeNumber = incomingNumber;
                        Log.v(TAG, "來電 TEL : " + incomingNumber);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: // 接通电话
                        Log.v(TAG, "接通 TEL : " + incomingNumber);
                        file = getRecordFile();
                        toastShot("開始錄音 : " + file.getAbsolutePath());
                        Log.v(TAG, ">>>>>>接通>>>>>>>>" + state);

                        try {
                            audioHelper = MediaRecorderAudioHelper.newInstance().isVoiceCall().file(file).buildAndStart();
                        } catch (Exception ex) {
                            toastShot("您的設備不支援雙向錄音!");
                            audioHelper = MediaRecorderAudioHelper.newInstance().isVoiceCommunication().file(file).buildAndStart();
                        }

//                        audioHelper2 = new AudioRecordHelper(file.getAbsolutePath());
//                        try {
//                            audioHelper2.startRecording();
//                        } catch (Exception ex) {
//                            Log.e(TAG, ex.getMessage() ,ex);
//                            toastShot("不可預期的失敗 : " + ex.getMessage());
//                        }

                        break;
                    case TelephonyManager.CALL_STATE_IDLE: // 挂掉电话
                        Log.v(TAG, ">>>>>>挂电话>>>>>>>>" + state);
                        Log.v(TAG, "掛斷 TEL : " + incomingNumber);
                        if (audioHelper != null) {
                            audioHelper.stop();
                        }

//                        if(audioHelper2 != null){
//                            audioHelper2.stopRecording();
//                        }
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                toastShot("不可預期的失敗 : " + e.getMessage());
            }
        }
    }
}
