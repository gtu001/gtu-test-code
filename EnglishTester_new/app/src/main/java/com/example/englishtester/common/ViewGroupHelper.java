package com.example.englishtester.common;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import com.example.englishtester.common.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by wistronits on 2018/8/9.
 */

public class ViewGroupHelper {

    private static ViewGroupHelper _INST = new ViewGroupHelper();

    public static ViewGroupHelper getInstance() {
        return _INST;
    }

    private static final String TAG = ViewGroupHelper.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void whenViewRenderCompleted(final ViewGroup layout, final Handler handler, final Runnable runnable, final long delayTime) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.w(TAG, "[whenRenderCompleted] unsuppeorted !!! (不支援!)");
            return;
        }
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                handler.postDelayed(runnable, delayTime);
            }
        });
    }
}
