package com.example.englishtester.common.interf;

import android.content.Context;

import com.example.englishtester.common.mobi.base.MobiViewerMainHandler;

/**
 * Created by wistronits on 2018/8/7.
 */

public interface ITxtReaderActivity {
    void doOnoffService(boolean b);

    Context getApplicationContext();

    void onWordClickBefore_TxtReaderAppender(String word);

    void gotoViewPagerPosition(int page_index);

    String getCurrentTitle(int page_index);

    void setTitle(String currentTitle);

    String getFileName();

    String getDtoFileName();
}
