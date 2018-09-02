package com.example.gtuandroid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * 建立設定畫面
 */
public class PrefereceCategoryTestActivity extends Activity {

    private static final String TAG = PrefereceCategoryTestActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferencesFragment()).commit();

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
    }

    public static class MyPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle saveInstanceState) {
            super.onCreate(saveInstanceState);
            addPreferencesFromResource(R.xml.mypreference);
        }

        @Override
        public void onActivityCreated(Bundle saveinstanceState) {
            super.onActivityCreated(saveinstanceState);
            Preference preference = getPreferenceManager().findPreference("test3");
            if (preference != null) {
                preference.setTitle("標題變更 : onActivityCreated");
                preference.setSummary("摘要變更 : onActivityCreated");
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            EditTextPreference edittext_preference = (EditTextPreference) getPreferenceScreen().findPreference("edittext_key");
            edittext_preference.setSummary(edittext_preference.getText());
        }

        @Override
        public void onResume(){
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause(){
            super.onPause();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }
    }
}
