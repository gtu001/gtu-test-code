package com.example.englishtester.common;

import android.content.Intent;
import android.os.Bundle;

public class IntentShower {

    private static final String TAG = IntentShower.class.getSimpleName();

    public static void show(Intent intent, String label) {
        if (intent != null) {
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    sb.append(key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL") + "\n");
                }
            }
            Log.line(TAG, "# " + label + " , Intent = " + sb);
        }
    }
}
