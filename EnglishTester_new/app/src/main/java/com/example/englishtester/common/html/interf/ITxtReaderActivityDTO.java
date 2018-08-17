package com.example.englishtester.common.html.interf;

import android.widget.TextView;

import com.example.englishtester.common.TxtReaderAppender;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public interface ITxtReaderActivityDTO {
    TextView getTxtView();

    boolean isImageLoadMode();

    void setBookmarkHolder(Map<Integer, TxtReaderAppender.WordSpan> bookmarkHolder);

    StringBuilder getFileName();

    boolean getBookmarkMode();

    Map<Integer, TxtReaderAppender.WordSpan> getBookmarkHolder();

    AtomicReference<Integer> getBookmarkIndexHolder();

}