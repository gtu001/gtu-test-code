package com.example.englishtester;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

public class ListenModeService {
    
    private static final String TAG = ListenModeService.class.getSimpleName();

    MainActivityDTO dto;
    TextView englishLabel;
    TextView englishPronounceLabel;
    
    int englishLabelTextColor;
    int englishPronounceLabelTextColor;
    
    public ListenModeService(TextView englishLabel, TextView englishPronounceLabel, MainActivityDTO dto){
        this.englishLabel = englishLabel;
        this.englishPronounceLabel = englishPronounceLabel;
        this.dto = dto;
    }
    
    public void hide(){
        Log.v(TAG, "#. hide ");
        if(!dto.isListenTestMode){
            return;
        }
        
        englishLabelTextColor = englishLabel.getCurrentTextColor();
        englishPronounceLabelTextColor = englishPronounceLabel.getCurrentTextColor();
        
        englishLabel.setTextColor(Color.WHITE);
        englishPronounceLabel.setTextColor(Color.WHITE);
    }
    
    public void show(){
        Log.v(TAG, "#. show ");
        if(!dto.isListenTestMode){
            return;
        }
        
        englishLabel.setTextColor(englishLabelTextColor);
        englishPronounceLabel.setTextColor(englishPronounceLabelTextColor);
    }
}
