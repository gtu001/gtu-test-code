package com.example.gtuandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.example.gtuandroid.component.TabManager;
import com.example.gtuandroid.sub.FragmentTab1;
import com.example.gtuandroid.sub.FragmentTab2;
import com.example.gtuandroid.sub.FragmentTab3;
import com.example.gtuandroid.sub.FragmentTab4;

public class FragmentTabsActivity extends FragmentActivity {
    private TabHost mTabHost;
    private TabManager mTabManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tabs);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
        mTabHost.setCurrentTab(0);
        mTabManager.addTab(mTabHost.newTabSpec("Tab1")//
                .setIndicator("Tab1", this.getResources().getDrawable(android.R.drawable.ic_dialog_alert)), //
                FragmentTab1.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("Tab2")//
                .setIndicator("Tab2", this.getResources().getDrawable(android.R.drawable.ic_lock_lock)), //
                FragmentTab2.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("Tab3")//
                .setIndicator("Tab3", this.getResources().getDrawable(android.R.drawable.ic_input_add)), //
                FragmentTab3.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("Tab4")//
                .setIndicator("Tab4", this.getResources().getDrawable(android.R.drawable.ic_delete)), //
                FragmentTab4.class, null);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 先取得螢幕解析度
        int screenWidth = dm.widthPixels; // 取得螢幕的寬

        TabWidget tabWidget = mTabHost.getTabWidget(); // 取得tab的物件
        int count = tabWidget.getChildCount(); // 取得tab的分頁有幾個
        if (count > 3) {
            for (int i = 0; i < count; i++) {
                tabWidget.getChildTabViewAt(i).setMinimumWidth((screenWidth) / 3);// 設定每一個分頁最小的寬度
            }
        }
    }

    // 回上頁時取消fragment替換的動作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("tabActivity", "onKeyDown count : " + getFragmentManager().getBackStackEntryCount());
        //若堆疊內沒有fragmgnet則回系統預設上一頁
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // getSupportFragmentManager().popBackStack();//不work
            getFragmentManager().popBackStack();
        }
        return true;
    }
}