package com.example.gtuandroid.component;

import java.util.HashMap;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

public class TabManager implements TabHost.OnTabChangeListener {
    private final FragmentActivity mActivity;
    private final TabHost mTabHost;
    private final int mContainerId;
    private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    TabInfo mLastTab;

    // TabInfo就是每一個分頁當中存在的資訊, 例如該分頁的名稱、傳到哪一個類別以及傳過去的時候, 所帶的Bundle。
    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    // DummyTabFactory目的是用來創造一個分頁所形成的View, 當你按下任一分頁, 它就會根據對應的View來進行切換。
    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    /*
     * 從建構子傳入的就是該FragmentActivity, 再來就是TabHost物件, 最後是一個容器的id,
     * 這個容器就是用來裝我們切換分頁的view, 還記得fragment_tabs.xml當中, 我們用了兩個FrameLayout嗎?
     * 這個就是放在上面的那個, 你可以對照一下後面我們會傳入的id, 就可以清楚的明白這個layout的功用。
     */
    public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
        mActivity = activity;
        mTabHost = tabHost;
        mContainerId = containerId;
        mTabHost.setOnTabChangedListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mActivity));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);

        info.fragment = mActivity.getFragmentManager().findFragmentByTag(tag);
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
            ft.detach(info.fragment);
            ft.commit();
        }

        mTabs.put(tag, info);
        mTabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tabId) {
        TabInfo newTab = mTabs.get(tabId);

        if (mLastTab != newTab) {
            FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }

            if (newTab != null) {
                newTab.fragment = Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
                ft.add(mContainerId, newTab.fragment, newTab.tag);
                if (newTab.fragment == null) {
                    ft.detach(mLastTab.fragment);
                } else {
                    mActivity.getSupportFragmentManager().popBackStack();
                    ft.replace(mContainerId, newTab.fragment);
                    ft.attach(newTab.fragment);
                }
            }else{
                Log.e("MainTabActivity","newTab == null");
            }

            mLastTab = newTab;
            ft.commit();
            mActivity.getSupportFragmentManager().executePendingTransactions();
        }
    }
}