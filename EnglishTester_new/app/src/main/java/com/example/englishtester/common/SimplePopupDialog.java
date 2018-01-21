package com.example.englishtester.common;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.englishtester.FloatViewActivity;

/**
 * Created by gtu001 on 2017/5/29.
 */

public class SimplePopupDialog {

    public static void show(Context context, View parent, String text, int width, int height, final View.OnClickListener onClickListener) {
        final Button button = new Button(context);
        button.setText(text);
        final PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setContentView(button);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(300);
        popupWindow.setHeight(200);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(button);
                }
                popupWindow.dismiss();
            }
        });
    }
}
