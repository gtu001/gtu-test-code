package com.example.englishtester.common.interf;

import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.common.IFloatServiceAidlInterface;

public interface TxtBufferActivityInterface extends ITxtReaderActivity {
    void setTitle(String titleVal);

    IFloatServiceAidlInterface getFloatService();

    RecentTxtMarkService getRecentTxtMarkService();

    int getFixScreenWidth();

    void gotoViewPagerPosition(int position);

    int getCurrentPageIndex();
}