package com.example.englishtester.common;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class TextToSpeechComponent implements TextToSpeech.OnInitListener {

    private static final String TAG = TextToSpeechComponent.class.getSimpleName();

    private Context context;
    private TextToSpeech talker = null;

    public TextToSpeechComponent(Context context) {
        this.context = context;
        this.talker = new TextToSpeech(context, this);
    }

    public void speak(String word) {
        HashMap myHashAlarm = new HashMap();
//        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
//                String.valueOf(AudioManager.STREAM_ALARM));
//        talker.speak(myText1, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
//        talker.speak(myText2, TextToSpeech.QUEUE_ADD, myHashAlarm);
        talker.speak(word, TextToSpeech.QUEUE_FLUSH, myHashAlarm);//原為null
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.v(TAG, "onInit success!");
            Log.v(TAG, "language = " + talker.getLanguage());
            if (talker.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                Log.v(TAG, "set US");
                talker.setLanguage(Locale.US);
            }

            //不設定不行
            talker.setLanguage(Locale.US);
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (talker != null) {
            talker.stop();
            talker.shutdown();
            talker = null;
        }
    }
}
