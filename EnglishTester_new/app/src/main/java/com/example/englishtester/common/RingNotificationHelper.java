package com.example.englishtester.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

public class RingNotificationHelper {

    private static final RingNotificationHelper INST = new RingNotificationHelper();

    public static RingNotificationHelper getInstance() {
        return INST;
    }

    public Ringtone ring(Context context, Float percent, Long during) {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if (alert == null) {
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        final Ringtone r = RingtoneManager.getRingtone(context, alert);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (percent != null) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                float volume = (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) * percent);
//                r.setVolume(volume);//目前無效
                audioManager.setStreamVolume(AudioManager.STREAM_RING, (int) volume, AudioManager.FLAG_SHOW_UI);
            }
        }
        r.play();
        if (during != null && during > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    r.stop();
                }
            }, during);
        }
        return r;
    }
}
