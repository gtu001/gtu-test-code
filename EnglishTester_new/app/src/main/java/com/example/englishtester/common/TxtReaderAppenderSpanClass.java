package com.example.englishtester.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.epub.base.EpubViewerMainHandler;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.mobi.base.MobiViewerMainHandler;
import com.example.englishtester.common.txtbuffer.base.TxtBufferViewerMainHandler;

public class TxtReaderAppenderSpanClass {

    private static final String TAG = TxtReaderAppenderSpanClass.class.getSimpleName();

    public static final Class[] CLICKABLE_SPAN_IMPL_CLZ = new Class[]{//
            TxtReaderAppenderSpanClass.WordSpan.class, //
            TxtReaderAppenderSpanClass.SimpleUrlLinkSpan.class,//
            TxtReaderAppenderSpanClass.EpubUrlLinkSpan.class,//
            TxtReaderAppenderSpanClass.MobiUrlLinkSpan.class,//
    };//

    public static ClickableSpan createLinkSpan(Context context, String url, ITxtReaderActivityDTO dto) {
        if (dto instanceof TxtReaderActivity.TxtReaderActivityDTO || dto instanceof TxtBufferViewerMainHandler.TxtBufferDTO) {
            Log.v(TAG, ">> create Link [SimpleUrlLinkSpan] : " + url);
            return new SimpleUrlLinkSpan(context, url);
        } else if (dto instanceof EpubViewerMainHandler.EpubDTO) {
            Log.v(TAG, ">> create Link [EpubUrlLinkSpan] : " + url);
            return new EpubUrlLinkSpan(context, url, (EpubViewerMainHandler.EpubDTO) dto);
        } else if (dto instanceof MobiViewerMainHandler.MobiDTO) {
            Log.v(TAG, ">> create Link [MobiUrlLinkSpan] : " + url);
            return new MobiUrlLinkSpan(context, url, (MobiViewerMainHandler.MobiDTO) dto);
        } else {
            throw new RuntimeException("createLinkSpan Unknow DTO !!");
        }
    }

    public static class SimpleUrlLinkSpan extends ClickableSpan {
        Context context;
        String url;

        public SimpleUrlLinkSpan(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View view) {
            Log.v(TAG, "click " + " - " + url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }
    }

    public static class EpubUrlLinkSpan extends ClickableSpan {
        Context context;
        String url;
        EpubViewerMainHandler.EpubDTO epubDTO;

        public EpubUrlLinkSpan(Context context, String url, EpubViewerMainHandler.EpubDTO epubDTO) {
            this.context = context;
            this.url = url;
            this.epubDTO = epubDTO;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View view) {
            Log.v(TAG, "click " + " - " + url);
            String tmpUrl = url;
//            if (tmpUrl.indexOf("#") != -1) {
//                tmpUrl = StringUtils.substring(tmpUrl, 0, url.indexOf("#"));
//            }
            Log.v(TAG, "realUrl " + " - " + tmpUrl);
            this.epubDTO.gotoLink(tmpUrl);
        }
    }

    public static class MobiUrlLinkSpan extends ClickableSpan {
        Context context;
        String url;
        MobiViewerMainHandler.MobiDTO mobiDTO;

        public MobiUrlLinkSpan(Context context, String url, MobiViewerMainHandler.MobiDTO mobiDTO) {
            this.context = context;
            this.url = url;
            this.mobiDTO = mobiDTO;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View view) {
            Log.v(TAG, "click " + " - " + url);
            String tmpUrl = url;
//            if (tmpUrl.indexOf("#") != -1) {
//                tmpUrl = StringUtils.substring(tmpUrl, 0, url.indexOf("#"));
//            }
            Log.v(TAG, "realUrl " + " - " + tmpUrl);
            this.mobiDTO.gotoLink(tmpUrl);
        }
    }

    public static class WordSpan extends ClickableSpan {
        int id = -1;
        private String word;
        private boolean marking = false;
        private boolean bookmarking = false;
        private int groupStart;
        private int groupEnd;
        private TxtReaderAppenderEscaper escaper;

        public WordSpan() {
        }

        public WordSpan(int id, int groupStart, int groupEnd, TxtReaderAppenderEscaper escaper) {
            this.id = id;
            this.groupStart = groupStart;
            this.groupEnd = groupEnd;
            this.escaper = escaper;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // ds.bgColor = Color.WHITE;
            ds.setColor(Color.BLACK);
            ds.setUnderlineText(false);
            if (marking) {
                ds.setTypeface(Typeface.create("新細明體", Typeface.BOLD));
            }

            // if the word selected is the same as the ID set the highlight flag
            if (bookmarking) {
                ds.setColor(ds.linkColor);
                ds.bgColor = Color.YELLOW;
//                ds.setARGB(255, 255, 255, 255);
            }
        }

        @Override
        public void onClick(View v) {
        }

        public void setMarking(boolean m) {
            marking = m;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isMarking() {
            return marking;
        }

        public boolean isBookmarking() {
            return bookmarking;
        }

        public void setBookmarking(boolean bookmarking) {
            this.bookmarking = bookmarking;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getSentance() {
            try {
                return escaper.getSentance(groupStart, groupEnd);
            } catch (Exception ex) {
                return "";
            }
        }
    }
}
