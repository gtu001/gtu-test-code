package com.example.englishtester.common;

import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 設定放放大鏡坐標
 */
public enum MagnifierPosEnum {
    LEFT_TOP() {
        @Override
        public void apply(WindowManager mWindowManager, LayoutParams wmParams) {
            DisplayMetrics dm = getDisplayMetrics(mWindowManager);
            wmParams.x = 0;
            wmParams.y = 0;
        }
    },
    RIGHT_TOP() {
        @Override
        public void apply(WindowManager mWindowManager, LayoutParams wmParams) {
            DisplayMetrics dm = getDisplayMetrics(mWindowManager);
            wmParams.x = dm.widthPixels;
            wmParams.y = 0;
        }
    },
    LEFT_BUTTOM() {
        @Override
        public void apply(WindowManager mWindowManager, LayoutParams wmParams) {
            DisplayMetrics dm = getDisplayMetrics(mWindowManager);
            wmParams.x = 0;
            wmParams.y = dm.heightPixels;
        }
    },
    RIGHT_BUTTOM() {
        @Override
        public void apply(WindowManager mWindowManager, LayoutParams wmParams) {
            DisplayMetrics dm = getDisplayMetrics(mWindowManager);
            wmParams.x = dm.widthPixels;
            wmParams.y = dm.heightPixels;
        }
    },
    ;
    private static DisplayMetrics getDisplayMetrics(WindowManager mWindowManager) {
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public abstract void apply(WindowManager mWindowManager, WindowManager.LayoutParams wmParams);
}