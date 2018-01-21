package com.example.gtuandroid.sub;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class HorizontalBarViewGroup extends ViewGroup {

    private static final String TAG = HorizontalBarViewGroup.class.getSimpleName();

    public HorizontalBarViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalBarViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalBarViewGroup(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalBarViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*
     * 我们还需要提供onLayout
     * 方法的实现。这个方法设置它每个子view的位置和大小。下面我提供了一个非常基础的实现，遍历所有子view然后逐个横向依次布局。
     * 
     * Left —  view的x轴起始位置 ,Top —  view的y轴起始位置 ,Right —  view的x轴结束位置 ,Bottom — 
     * view的y轴结束位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int prevChildRight = 0;
        int prevChildBottom = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            child.layout(//
                    prevChildRight, //
                    prevChildBottom, //
                    prevChildRight + child.getMeasuredWidth(), //
                    prevChildBottom + child.getMeasuredHeight()//
            );
            prevChildRight += child.getMeasuredWidth();
        }
    }

    private void testData() {
        // 而当设置为 wrap_content时，容器传进去的是AT_MOST,
        // 表示子view的大小最多是多少，这样子view会根据这个上限来设置自己的尺寸。当子view的大小设置为精确值时，容器传入的是EXACTLY,
        // 而MeasureSpec的UNSPECIFIED模式目前还没有发现在什么情况下使用
        int specMode1 = MeasureSpec.getMode(MeasureSpec.EXACTLY);
        int specSize1 = MeasureSpec.getSize(MeasureSpec.EXACTLY);
        int specMode2 = MeasureSpec.getMode(MeasureSpec.AT_MOST);
        int specSize2 = MeasureSpec.getSize(MeasureSpec.AT_MOST);
        int specMode3 = MeasureSpec.getMode(MeasureSpec.UNSPECIFIED);
        int specSize3 = MeasureSpec.getSize(MeasureSpec.UNSPECIFIED);
        Log.v(TAG, "EXACTLY specMode = " + specMode1);
        Log.v(TAG, "EXACTLY specSize = " + specSize1);
        Log.v(TAG, "AT_MOST specMode = " + specMode2);
        Log.v(TAG, "AT_MOST specSize = " + specSize2);
        Log.v(TAG, "UNSPECIFIED specMode = " + specMode3);
        Log.v(TAG, "UNSPECIFIED specSize = " + specSize3);
    }

    private int measureLength(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Default size if no limits are specified.
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            // Calculate the ideal size of your
            // control within this maximum size.
            // If your control fills the available
            // space return the outer bound.
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // If your control can fit within these bounds return that value.
            result = specSize;
        }
        return result;
    }

    private void onMeasure_test1(int widthMeasureSpec, int heightMeasureSpec) {
        // 1.會佔據整個畫面
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void onMeasure_test2(int widthMeasureSpec, int heightMeasureSpec) {
        // 2.會計算子的高度(以最高的為高度), 不需要super.onMeasure
        int totalWidth = 0;
        int totalHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            totalWidth += child.getMeasuredWidth();
            if (child.getMeasuredHeight() > totalHeight) {
                // height of the container, will be the largest height.
                totalHeight = child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private void onMeasure_test3(int widthMeasureSpec, int heightMeasureSpec) {
        // 3.寫死固定寬高為300, 不需要super.onMeasure
        int totalWidth = 0;
        int totalHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            int width = MeasureSpec.makeMeasureSpec(300, MeasureSpec.EXACTLY);
            int height = MeasureSpec.makeMeasureSpec(300, MeasureSpec.EXACTLY);
            child.measure(width, height);
            totalWidth += width;
            if (height > totalHeight) {
                // height of the container, will be the largest height.
                totalHeight = child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private void onMeasure_test4(int widthMeasureSpec, int heightMeasureSpec) {
        // 4.經典範例
        int measuredHeight = measureLength(heightMeasureSpec);
        int measuredWidth = measureLength(widthMeasureSpec);
        setMeasuredDimension(measuredHeight, measuredWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 1.會佔據整個畫面
        // onMeasure_test1(widthMeasureSpec, heightMeasureSpec);
        // // 2.會計算子的高度(以最高的為高度)
        // onMeasure_test2(widthMeasureSpec, heightMeasureSpec);
        // 3.寫死固定寬高為300
        onMeasure_test3(widthMeasureSpec, heightMeasureSpec);
        // 4.經典範例
        // onMeasure_test4(widthMeasureSpec, heightMeasureSpec);
    }
}