package com.example.englishtester.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by gtu001 on 2017/10/28.
 */

public class KeyboardHelper {

    /**
     * 取消focus狀態
     */
    public static void clearFoucs(View view, Context context) {
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = null;
        if (context instanceof Activity) {
            v = ((Activity) context).getCurrentFocus();
        } else {
            v = view;
        }
        if (v == null)
            return;
        v.clearFocus();
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 顯示鍵盤
     */
    public static void requestFocus(View view, Context context) {
        view.requestFocus();
        InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        int softInputAnchor = view.getId();
        imm.toggleSoftInput(softInputAnchor, 0);
        //下方為顯示虛擬鍵盤
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
