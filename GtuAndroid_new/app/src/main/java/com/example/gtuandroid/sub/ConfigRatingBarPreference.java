package com.example.gtuandroid.sub;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.TimeFormatException;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TimePicker;

import com.example.gtuandroid.R;

import java.util.TimeZone;

public class ConfigRatingBarPreference extends Preference implements RatingBar.OnRatingBarChangeListener {

    private float mCurrentRating;
    private float mOldRating;

    public ConfigRatingBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.subview_pref_config2);
    }

    /**
     * defaultValue屬性設定為可以使用
     */
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getFloat(index, 0);
    }

    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mCurrentRating = getPersistedFloat(mCurrentRating);
        } else {
            mCurrentRating = (Float) defaultValue;
            persistFloat(mCurrentRating);
        }
        mOldRating = mCurrentRating;
    }

    protected void onBindView(View view) {
        super.onBindView(view);

        final RatingBar rating = (RatingBar)view.findViewById(R.id.ratingBar1);
        if(rating != null){
            rating.setRating(mCurrentRating);
            rating.setOnRatingBarChangeListener(this);
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        mCurrentRating = (callChangeListener(rating)) ? rating : mOldRating;
        persistFloat(mCurrentRating);
        mOldRating = mCurrentRating;
    }
}