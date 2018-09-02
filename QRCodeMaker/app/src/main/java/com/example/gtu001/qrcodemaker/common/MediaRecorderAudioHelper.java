package com.example.gtu001.qrcodemaker.common;

import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gtu001 on 2017/12/12.
 */

public class MediaRecorderAudioHelper {

    private static final String TAG = MediaRecorderAudioHelper.class.getSimpleName();

    private MediaRecordInfo mediaRecordInfo = new MediaRecordInfo();

    public static MediaRecorderAudioHelper newInstance(){
        return new MediaRecorderAudioHelper();
    }

    private static class MediaRecordInfo {
        private Map<Integer, String> infoMap = new HashMap<Integer, String>();

        private MediaRecordInfo() {
            for (Field f : MediaRecorder.class.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.getName().startsWith("MEDIA_RECORDER")) {
                    try {
                        Integer val = (Integer) FieldUtils.readDeclaredStaticField(MediaRecorder.class, f.getName(), true);
                        infoMap.put(val, f.getName());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public String getInfo(int val) {
            if (infoMap.containsKey(val)) {
                return infoMap.get(val);
            }
            return "Unknown Code : " + val;
        }
    }

    private void MediaRecorderAudioHelper() {
    }

    private MediaRecorder mediaRecorder;
    private File file;
    private Integer audioSource;
    public static final String RECORDFILE_EXTENSTION = ".3gp";

    /**
     * //这个设置就是获取双向声音
     */
    public MediaRecorderAudioHelper isVoiceCall(){
        this.audioSource = MediaRecorder.AudioSource.VOICE_CALL;
        return this;
    }
    public MediaRecorderAudioHelper isMic(){
        this.audioSource = MediaRecorder.AudioSource.MIC;
        return this;
    }
    public MediaRecorderAudioHelper isVoiceCommunication(){
        this.audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
        return this;
    }
    public MediaRecorderAudioHelper file(File file){
        this.file = file;
        return this;
    }

    public MediaRecorderAudioHelper buildAndStart() {
        try {
            Validate.notNull(this.audioSource, "audioSource不可為空", new Object[0]);
            Validate.notNull(this.file, "file不可為空", new Object[0]);

            mediaRecorder = new MediaRecorder();

            mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    Log.v(TAG, "#onInfo - What:" + mediaRecordInfo.getInfo(what) + " , Extra:" + mediaRecordInfo.getInfo(extra));
                }
            });
            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    Log.v(TAG, "#onError - What:" + mediaRecordInfo.getInfo(what) + " , Extra:" + mediaRecordInfo.getInfo(extra));
                }
            });


            mediaRecorder.setAudioSource(this.audioSource);

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // 按3gp格式输出
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.setOutputFile(file.getAbsolutePath()); // 输出文件

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                mediaRecorder.setAudioSamplingRate(11025);
            }

//                        mediaRecorder.setAudioEncodingBitRate(42 * 1024);
//                        mediaRecorder.setAudioChannels(2);
//                        mediaRecorder.setAudioSamplingRate(44100);

            mediaRecorder.prepare(); // 准备
            mediaRecorder.start();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        return this;
    }

    public void stop(){
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
