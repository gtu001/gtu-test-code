package com.example.englishtester.common;

import android.os.Handler;

import com.example.englishtester.common.Log;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by gtu001 on 2018/8/9.
 */

public class ViewPagerHelper {

    private static final String TAG = ViewPagerHelper.class.getSimpleName();

    public static void triggerPageSelected(final ViewPager viewPager, Integer pageIndex) {
        final AtomicReference<Integer> pageIdx = new AtomicReference<>(pageIndex);

        if (pageIdx.get() == null) {
            pageIdx.set(viewPager.getCurrentItem());
        } else if (pageIdx.get() != viewPager.getCurrentItem()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(pageIdx.get(), false);
                }
            });
        }

        /*
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Method mth = ViewPager.class.getDeclaredMethod("dispatchOnPageSelected", int.class);
                    mth.setAccessible(true);
                    mth.invoke(viewPager, pageIdx.get());
                } catch (Exception e) {
                    Log.e(TAG, "triggerFirstPage ERR : " + e.getMessage(), e);
                }
            }
        });
        */
    }
}
