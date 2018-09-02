package com.example.gtuandroid;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.example.gtuandroid.sub.FragmentV4_1;
import com.example.gtuandroid.sub.FragmentV4_2;

public class FragmentPagerV4TestActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_pager_v4);
        
        //Page切換 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.setting);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
            }

            @Override
            public void onPageSelected(int paramInt) {
                getActionBar().setSelectedNavigationItem(paramInt);
            }

            @Override
            public void onPageScrollStateChanged(int paramInt) {
            }
        });
        
        // mViewPager.setCurrentItem(0);//設定選擇page
        // mViewPager.getAdapter().notifyDataSetChanged();//聲明變更
        // mViewPager.removeAllViews();//移除所有
        
        
        //加上action bar ↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        // Specify that tabs should be displayed in the action bar.
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                mViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 2; i++) {
            getActionBar().addTab(
                    getActionBar().newTab()
                            .setText("Tab " + (i + 1))
                            .setTabListener(tabListener));
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
            case 0:
                fragment = new FragmentV4_1();
            case 1:
                fragment = new FragmentV4_2();
            }
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return FragmentV4_1.TAG;
            case 1:
                return FragmentV4_2.TAG;
            default:
                return "Test";
            }
        }
    }
}
