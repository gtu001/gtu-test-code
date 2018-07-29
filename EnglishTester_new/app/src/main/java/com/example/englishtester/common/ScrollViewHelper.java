package com.example.englishtester.common;

import android.widget.ScrollView;

/**
 * Created by gtu001 on 2018/7/30.
 */

public class ScrollViewHelper {

    public static int getMaxHeight(ScrollView scrollView) {
        return scrollView.getChildAt(0).getHeight();
    }
}
