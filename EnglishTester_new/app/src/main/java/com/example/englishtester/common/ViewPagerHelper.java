package com.example.englishtester.common;

import android.support.v4.view.ViewPager;
import com.example.englishtester.common.Log;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by gtu001 on 2018/8/9.
 */

public class ViewPagerHelper {

    private static final String TAG = ViewPagerHelper.class.getSimpleName();

    public static void triggerPageSelected(final ViewPager viewPager, Integer pageIndex) {
        final AtomicReference<Integer> pageIdx = new AtomicReference<>(pageIndex);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (pageIdx.get() == null) {
                        pageIdx.set(viewPager.getCurrentItem());
                    }

                    Method mth = ViewPager.class.getDeclaredMethod("dispatchOnPageSelected", int.class);
                    mth.setAccessible(true);
                    mth.invoke(viewPager, pageIdx.get());
                } catch (Exception e) {
                    Log.e(TAG, "triggerFirstPage ERR : " + e.getMessage(), e);
                }
            }
        });
    }
}
