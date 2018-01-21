package com.example.gtuandroid.component;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class TextToSpeechComponent implements TextToSpeech.OnInitListener {

    private static final String TAG = TextToSpeechComponent.class.getSimpleName();

    private Context context;
    private TextToSpeech talker = null;
    
    private TextToSpeechComponent(Context context){
        this.context = context;
        this.talker = new TextToSpeech(context, this);
    }
    
    private static TextToSpeechComponent _INSTANCE;
    public static TextToSpeechComponent getInstance(Context context){
        if(_INSTANCE == null || _INSTANCE.talker == null){
            _INSTANCE = new TextToSpeechComponent(context);
        }
        return _INSTANCE;
    }
    
    public void speak(String word){
        talker.speak(word, TextToSpeech.QUEUE_FLUSH, null);
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
