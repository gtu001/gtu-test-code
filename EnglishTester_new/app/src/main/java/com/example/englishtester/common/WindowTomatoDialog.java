package com.example.englishtester.common;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.englishtester.R;

import org.apache.commons.lang3.StringUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

public class WindowTomatoDialog {

    private static final String TAG = WindowSingleInputDialog.class.getSimpleName();
    private WindowManager mWindowManager;
    private Context context;
    private RelativeLayout outterLayout;
    private View.OnClickListener closeTomatoBtnListener;


    public void setCloseTomatoBtnListener(final View.OnClickListener closeTomatoBtnListener) {
        this.closeTomatoBtnListener = closeTomatoBtnListener;
    }

    public WindowTomatoDialog(Context context) {
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.context = context;
    }

    public void showDialog(String buttonText) {
        LayoutInflater inflater = LayoutInflater.from(context);

        outterLayout = (RelativeLayout) inflater.inflate(R.layout.dialog_tomato, null);
        outterLayout.setBackgroundColor(context.getResources().getColor(R.color.translucent_black));

        Button closeTomatoBtn = outterLayout.findViewById(R.id.close_tomato_btn);
        if (StringUtils.isNotBlank(buttonText)) {
            closeTomatoBtn.setText(StringUtils.trimToEmpty(buttonText));
        }

        closeTomatoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closeTomatoBtnListener != null) {
                    closeTomatoBtnListener.onClick(view);
                }
            }
        });

        outterLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    Log.v(TAG, "touch outside");
                    mWindowManager.removeView(outterLayout);
                }

                // 如果沒有得到focus則關閉編輯視窗
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    mWindowManager.removeView(outterLayout);
                }
                return false;
            }
        });

        mWindowManager.addView(outterLayout, getInitLayoutParams(context));
    }

    public WindowManager.LayoutParams getInitLayoutParams(Context context) {
        int wh[] = ViewLocationCoordinatesHelper.getScreenSize(context);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG, //WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

//        params.windowAnimations = android.R.style.Animation_Dialog;

        params.gravity = Gravity.CENTER;
//        params.width = wh[0];
//        params.height = wh[1] - parent_height;
//        params.x = 0;
//        params.y = y;
        return params;
    }

    public interface WindowSingleInputDialog_DlgConfirm {
        void onConfirm(String inputStr, View v, WindowSingleInputDialog dlg);
    }

    public void dismiss() {
        if (outterLayout != null) {
            mWindowManager.removeViewImmediate(outterLayout);
        }
        outterLayout = null;
    }
}
