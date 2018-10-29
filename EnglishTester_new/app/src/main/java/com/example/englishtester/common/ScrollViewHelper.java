package com.example.englishtester.common;

import android.view.View;
import android.widget.ScrollView;

/**
 * Created by gtu001 on 2018/7/30.
 */

public class ScrollViewHelper {

    @Deprecated
    public static int getMaxHeight2(ScrollView scrollView) {
        return scrollView.getChildAt(0).getHeight();
    }

    public static int getMaxHeight(ScrollView scrollView) {
        int scrollRange = 0;
        if (scrollView.getChildCount() > 0) {
            View child = scrollView.getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (scrollView.getHeight() - scrollView.getPaddingBottom() - scrollView.getPaddingTop()));
        }
        return scrollRange;
    }
}
