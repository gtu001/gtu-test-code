package com.example.englishtester.common;

import android.app.Service;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class, that can be used as a TouchListener on any view (e.g. a Button). It
 * cyclically runs a clickListener, emulating keyboard-like behaviour. First
 * click is fired immediately, next one after the initialInterval, and
 * subsequent ones after the normalInterval.
 *
 * <p>
 * Interval is scheduled after the onClick completes, so it has to run fast. If
 * it runs slow, it does not generate skipped onClicks. Can be rewritten to
 * achieve this.
 */
public class RepeatMoveListener implements OnTouchListener {

    private static final String TAG = RepeatMoveListener.class.getSimpleName();

    private Handler handler = new Handler();

    private int initialInterval = 500;
    private final int normalInterval = 10;
    public static AtomicReference<float[]> POSITION = new AtomicReference<float[]>();

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, normalInterval);
            if(!canMove.get()){
                setCanMove(true);
                myVibrator.vibrate(50);
            }
        }
    };

    private AtomicBoolean canMove = new AtomicBoolean(false);
    private Context context;
    private View downView;
    private OnTouchListener onTouchListener;
    private Rect rect; // Variable rect to hold the bounds of the view
    private Vibrator myVibrator;

    public RepeatMoveListener(Context context, OnTouchListener onTouchListener) {
        this.context = context;
        this.onTouchListener = onTouchListener;
        this.myVibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            Log.v(TAG, "action down");
            handler.removeCallbacks(handlerRunnable);
            handler.postDelayed(handlerRunnable, initialInterval);
            downView = view;
            //downView.setPressed(true);
            //紀錄按下位置
            rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            Log.v(TAG, "action up");
            handler.removeCallbacks(handlerRunnable);
            //downView.setPressed(false);
            downView = null;
            setPosition(motionEvent);
            setCanMove(false);
            break;
        case MotionEvent.ACTION_MOVE:
            Log.v(TAG, "action move");
            if (canMove.get()) {
                onTouchListener.onTouch(view, motionEvent);
            } else {
                if (!rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())) {
                    // User moved outside bounds
                    handler.removeCallbacks(handlerRunnable);
                    //downView = null;
                    Log.d(TAG, "ACTION_MOVE...OUTSIDE");
                    setCanMove(false);
                }
            }
            break;
        }
        return false;
    }

    private void setPosition(MotionEvent motionEvent){
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        float isLandscape = 0;
        if (d.widthPixels > d.heightPixels){
            isLandscape = 1;
        }
        if(POSITION == null){
            POSITION = new AtomicReference<float[]>();
        }
        POSITION.set(new float[]{motionEvent.getRawX(), motionEvent.getRawY(), isLandscape});
    }

    private void setCanMove(boolean isCanMove){
        if(isCanMove){
            canMove.set(true);
        }else{
            canMove.set(false);
        }
    }
}