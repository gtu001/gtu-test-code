package com.example.gtu001.qrcodemaker.common;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by gtu001 on 2017/12/12.
 */

public class AudioRecordHelper {

//    http://blog.csdn.net/liuhaitao_share/article/details/41249175

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder;
    private boolean isRecording;
    private Thread recordingThread;

    private String filePath;

    public AudioRecordHelper(String filePath) {
        setForceUseOn();
        this.filePath = filePath;
    }

    public void startRecording() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_CALL,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }

    private void writeAudioDataToFile() {
        FileOutputStream os = null;
        // Write the output audio in byte
        try {
            short sData[] = new short[BufferElements2Rec];

            os = new FileOutputStream(filePath);

            while (isRecording) {
                // gets the voice output from microphone to byte format

                recorder.read(sData, 0, BufferElements2Rec);
                System.out.println("Short wirting to file" + sData.toString());
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
            } catch (IOException e) {
            }
            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }

    public void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }
    }

    private static void setForceUseOn() {
/*
setForceUse(int usage, int config);

----usage for setForceUse, must match AudioSystem::force_use
public static final int FOR_COMMUNICATION = 0;
public static final int FOR_MEDIA = 1;
public static final int FOR_RECORD = 2;
public static final int FOR_DOCK = 3;
public static final int FOR_SYSTEM = 4;
public static final int FOR_HDMI_SYSTEM_AUDIO = 5;

----device categories config for setForceUse, must match AudioSystem::forced_config
public static final int FORCE_NONE = 0;
public static final int FORCE_SPEAKER = 1;
public static final int FORCE_HEADPHONES = 2;
public static final int FORCE_BT_SCO = 3;
public static final int FORCE_BT_A2DP = 4;
public static final int FORCE_WIRED_ACCESSORY = 5;
public static final int FORCE_BT_CAR_DOCK = 6;
public static final int FORCE_BT_DESK_DOCK = 7;
public static final int FORCE_ANALOG_DOCK = 8;
public static final int FORCE_DIGITAL_DOCK = 9;
public static final int FORCE_NO_BT_A2DP = 10;
public static final int FORCE_SYSTEM_ENFORCED = 11;
public static final int FORCE_HDMI_SYSTEM_AUDIO_ENFORCED = 12;
public static final int FORCE_DEFAULT = FORCE_NONE;
 */
        try {
            Class audioSystemClass = Class.forName("android.media.AudioSystem");
            Method setForceUse = audioSystemClass.getMethod("setForceUse", int.class, int.class);
            setForceUse.invoke(null, 0, 0);      // setForceUse(FOR_RECORD, FORCE_NONE)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
