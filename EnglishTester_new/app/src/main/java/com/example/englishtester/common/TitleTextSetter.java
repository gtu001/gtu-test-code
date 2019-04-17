package com.example.englishtester.common;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by gtu001 on 2018/7/8.
 */

public class TitleTextSetter {

    public static void setText(Activity activity, String text) {
        activity.getActionBar().setTitle(text);
        activity.setTitle(text);
    }

    public static void setText(AppCompatActivity activity, String text) {
        activity.setTitle(text);

        ActionBar actionBar = activity.getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_bar_gray)));
        actionBar.setTitle(text);  // provide compatibility to all the versions
        actionBar.show();
    }
}
