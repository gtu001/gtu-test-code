package com.example.gtuandroid.sub;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MoveImageView extends ImageView {

    private WindowManager mWindowManager = (WindowManager) getContext().getApplicationContext().getSystemService("window");
    private int titleHeight = 0;
    
    public MoveImageView(Context context) {
        super(context);
    }

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTitleHeight(Window window) {
        // 获取状态栏高度。不能在onCreate回调方法中获取
        Rect frame = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int contentTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight;

        titleHeight = titleBarHeight;
    }
    
    private boolean pressOK = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams)getLayoutParams();
        int beginX = layoutParams.x;
        int endX = layoutParams.x + getMeasuredWidth();
        int beginY = layoutParams.y;
        int endY = layoutParams.y + getMeasuredHeight();
        float x = event.getRawX();
        float y = event.getRawY() - titleHeight;
        
        System.out.println(String.format("x : %s , %s, %s", x, beginX, endX));
        System.out.println(String.format("y : %s , %s, %s", y, beginY, endY));
        System.out.println(pressOK);
        
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            pressOK = x >= beginX && x <= endX && y >= beginY && y <= endY;
            break;
        case MotionEvent.ACTION_MOVE:
            if(pressOK){
                layoutParams.x = (int)(x - getMeasuredWidth() / 2);
                layoutParams.y = (int)(y - getMeasuredHeight() / 2);
                mWindowManager.updateViewLayout(this, layoutParams);
            }
            break;
        case MotionEvent.ACTION_UP:
            pressOK = false;
            break;
        }
        return false;
    }
}
