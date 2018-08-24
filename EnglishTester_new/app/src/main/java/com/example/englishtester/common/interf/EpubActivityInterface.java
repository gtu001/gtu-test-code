package com.example.englishtester.common.interf;

import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.common.IFloatServiceAidlInterface;

public interface EpubActivityInterface extends ITxtReaderActivity {
    void setTitle(String titleVal);

    IFloatServiceAidlInterface getFloatService();

    RecentTxtMarkService getRecentTxtMarkService();

    int getFixScreenWidth();
}