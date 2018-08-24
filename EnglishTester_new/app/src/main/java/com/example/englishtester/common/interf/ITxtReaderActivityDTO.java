package com.example.englishtester.common.interf;

import android.widget.TextView;

import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.TxtReaderAppender;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public interface ITxtReaderActivityDTO {
    TextView getTxtView();

    boolean isImageLoadMode();

    StringBuilder getFileName();

    boolean getBookmarkMode();

    Map<Integer, TxtReaderAppender.WordSpan> getBookmarkHolder();

    AtomicReference<Integer> getBookmarkIndexHolder();

    int getPageIndex();

    void setFileName(String title);

    IFloatServiceAidlInterface getIFloatService();
}