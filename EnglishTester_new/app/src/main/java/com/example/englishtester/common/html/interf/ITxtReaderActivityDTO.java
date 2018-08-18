package com.example.englishtester.common.html.interf;

import android.widget.TextView;

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
}