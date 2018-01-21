package com.example.gtuandroid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class TabFragmentActivity extends Activity implements ActionBar.TabListener {

    private static final String TAG = TabFragmentActivity.class.getSimpleName();
    private static final int MAX_PAGES = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for(int ii = 0 ; ii < MAX_PAGES ; ii ++){
            String tabName = String.format("Tab(%1$s)", ii);
            actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(android.R.id.content, PageFragment.newInstance(tab.getPosition()));
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public static class PageFragment extends Fragment {
        public static final String TAB_NAME = "Page";
        public static final String EXTRA_PAGE_NUM = "extra.pageNum";

        public static final PageFragment newInstance(int pageNum){
            PageFragment f = new PageFragment();
            Bundle args = new Bundle();
            args.putInt(EXTRA_PAGE_NUM, pageNum);
            f.setArguments(args);
            return f;
        }
    }
}
