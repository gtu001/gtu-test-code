package com.example.englishtester.common.interf;

import android.graphics.Bitmap;

import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.html.image.ImageLoaderCandidate4MobiHtml;
import com.example.englishtester.common.interf.ITxtReaderActivity;

public interface MobiActivityInterface extends ITxtReaderActivity {
    void setTitle(String titleVal);

    IFloatServiceAidlInterface getFloatService();

    RecentTxtMarkService getRecentTxtMarkService();

    int getFixScreenWidth();

    void gotoViewPagerPosition(int position);

    int getCurrentPageIndex();
}