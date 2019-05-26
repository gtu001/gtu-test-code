package com.example.gtu001.qrcodemaker.common;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class LayoutViewHelper {

    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public static LinearLayout createContentView_simple(Activity activity) {
        LinearLayout layout = new LinearLayout(activity);
        activity.setContentView(layout);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    public static LinearLayout createContentView(Activity activity) {
        LinearLayout layout = new LinearLayout(activity);
        ScrollView scroll = new ScrollView(activity);
        activity.setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.MATCH_PARENT));// 高度隨內容而定
        return layout;
    }
}
