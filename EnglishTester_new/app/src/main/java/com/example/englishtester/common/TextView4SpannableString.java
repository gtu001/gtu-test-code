package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wistronits on 2018/8/9.
 */

public class TextView4SpannableString extends android.support.v7.widget.AppCompatTextView {

    private Runnable onRenderCompleteCallback;
    private final Handler handler = new Handler();

    public TextView4SpannableString(Context context) {
        super(context);
    }

    public TextView4SpannableString(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView4SpannableString(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Finished drawing. Do other stuff.
        // However you must check if this is the first or subsequent call.
        // Each call to "invalidate()" will trigger re-drawing.

        if (onRenderCompleteCallback != null) {
            handler.post(onRenderCompleteCallback);
        }
    }

    public void setOnRenderCompleteCallback(Runnable onRenderCompleteCallback) {
        this.onRenderCompleteCallback = onRenderCompleteCallback;
    }
}
