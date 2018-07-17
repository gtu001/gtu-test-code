package com.example.englishtester.common;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by wistronits on 2018/7/17.
 */

public class FastActivityLayoutCreater {

    private static LinearLayout createSimpleScrollLinearLayout(Activity activity) {
        LinearLayout layout = new LinearLayout(activity);
        ScrollView scroll = new ScrollView(activity);
        activity.setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
