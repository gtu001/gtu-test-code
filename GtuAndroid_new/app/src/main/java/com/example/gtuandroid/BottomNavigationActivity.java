package com.example.gtuandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gtu001 on 2017/10/22.
 */

public class BottomNavigationActivity extends AppCompatActivity {

    private static final String TAG = BottomNavigationActivity.class.getSimpleName();

    private ViewPager viewpager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigation;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        this.setContentView(R.layout.activity_bottom_navigation);

        viewpager = (ViewPager) findViewById(R.id.viewpager);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        this.resetCancelNavigationViewAnimation(bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, item.getItemId() + " item was selected-------------------");
                switch (item.getItemId()) {
                    case R.id.menu_id1:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.menu_id2:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.menu_id3:
                        viewpager.setCurrentItem(2);
                        break;
                    case R.id.menu_id4:
                        viewpager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //禁止ViewPager滑動
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        this.setupViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(MyFragment.newInstance(this, "test1"));
        list.add(MyFragment.newInstance(this, "test2"));
        list.add(MyFragment.newInstance(this, "test3"));
        list.add(MyFragment.newInstance(this, "test4"));
        MyPageAdapter adapter = new MyPageAdapter(this.getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
    }

    /**
     * Fragment
     */
    public static class MyFragment extends Fragment {

        public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

        Context context;

        public static final MyFragment newInstance(Context context, String message) {
            MyFragment f = new MyFragment();
            Bundle bdl = new Bundle(1);
            bdl.putString(EXTRA_MESSAGE, message);
            f.setArguments(bdl);
            f.context = context;
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            String message = getArguments().getString(EXTRA_MESSAGE);
            View v = inflater.inflate(R.layout.activity_test, container, false);
            TextView textView = (TextView)v.findViewById(R.id.text);
            textView.setText(message);
            return v;
        }
    }

    /**
     * PageAdapter
     */
    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    /**
     * 取消Navigation按鈕特效
     */
    private void resetCancelNavigationViewAnimation(BottomNavigationView bnve) {
        try {
            BottomNavigationMenuView menu = (BottomNavigationMenuView) FieldUtils.readDeclaredField(bnve, "mMenuView", true);
            FieldUtils.writeDeclaredField(menu, "mShiftingMode", false, true);
            BottomNavigationItemView[] mButtons = (BottomNavigationItemView[]) FieldUtils.readDeclaredField(menu, "mButtons", true);
            for (BottomNavigationItemView button : mButtons) {
                FieldUtils.writeDeclaredField(button, "mShiftingMode", false, true);
            }
            menu.updateMenuView();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }
}
