package com.example.gtuandroid.component;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by gtu001 on 2017/12/14.
 */

public class SystemUtil {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void finishTheAppCompleted(Activity activity){
        activity.finishAffinity();
        activity.finishAndRemoveTask();

        //for all api
        activity.finish();
        System.exit(0);
    }
}
