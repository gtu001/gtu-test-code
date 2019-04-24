package com.example.englishtester.common;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by wistronits on 2018/8/9.
 */

public class FragmentUtil {

    public static Fragment getCurrentViewPagerFragment(int pagerId, ViewPager viewPager, FragmentManager fragmentManager) {
        Fragment page = fragmentManager.findFragmentByTag("android:switcher:" + pagerId + ":" + viewPager.getCurrentItem());
        // based on the current position you can then cast the page to the correct
        // class and call the method:
        if (page != null) {
            return page;
        }
        return null;
    }
}
