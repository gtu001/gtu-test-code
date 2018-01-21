package com.example.gtuandroid.sub;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.TimeFormatException;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.gtuandroid.R;

import java.util.TimeZone;

public class ConfigDialogPreference extends DialogPreference {

    private DatePicker datePicker;
    private TimePicker timePicker;

    private String mPreferenceValue = null;

    public ConfigDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.subview_pref_config);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mPreferenceValue = getPersistedString(mPreferenceValue);
        } else {
            mPreferenceValue = (String) defaultValue;
            persistString(mPreferenceValue);
        }
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker1);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker1);

        setTimeToView(mPreferenceValue);
    }

    public void setTimeToView(String preferenceValue) {
        Time time = new Time(TimeZone.getDefault().getID());
        try {
            time.parse(preferenceValue);
        } catch (TimeFormatException ex) {
            time.setToNow();
        }
        datePicker.updateDate(time.year, time.month, time.monthDay);
        timePicker.setCurrentHour(time.hour);
        timePicker.setCurrentMinute(time.minute);
    }

    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            Time time = new Time(TimeZone.getDefault().getID());
            time.year = datePicker.getYear();
            time.month = datePicker.getMonth();
            time.monthDay = datePicker.getDayOfMonth();
            time.hour = timePicker.getCurrentHour();
            time.minute = timePicker.getCurrentMinute();
            String newValue = time.format2445();
            if (callChangeListener(newValue)) {
                mPreferenceValue = newValue;
                persistString(mPreferenceValue);
            }
        }
    }

    /**
     * defaultValue屬性設定為可以使用
     */
    protected String onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    //↓↓↓↓↓↓↓ 螢幕翻轉必要code

    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        final PreferenceSavedState myState = new PreferenceSavedState(superState);
        myState.value = mPreferenceValue;
        return myState;
    }

    private static class PreferenceSavedState extends BaseSavedState {
        String value;

        public PreferenceSavedState(Parcelable superState) {
            super(superState);
        }

        public PreferenceSavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public static final Parcelable.Creator<PreferenceSavedState> CREATOR = new Parcelable.Creator<PreferenceSavedState>() {
            @Override
            public PreferenceSavedState createFromParcel(Parcel source) {
                return new PreferenceSavedState(source);
            }

            @Override
            public PreferenceSavedState[] newArray(int size) {
                return new PreferenceSavedState[size];
            }
        };
    }

    //↑↑↑↑↑↑↑ 螢幕翻轉必要code
}