package com.example.englishtester.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.RemoteException;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.BuildConfig;
import com.example.englishtester.FloatViewService;
import com.example.englishtester.RecentTxtMarkDAO;
import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.common.epub.base.EpubViewerMainHandler;
import com.example.englishtester.common.interf.ITxtReaderActivity;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2018/7/8.
 */

public class TxtReaderAppender {

    private static final String TAG = TxtReaderAppender.class.getSimpleName();

    RecentTxtMarkService recentTxtMarkService;
    ITxtReaderActivity activity;
    IFloatServiceAidlInterface mService;
    ITxtReaderActivityDTO dto;
    TextView txtView;

    public TxtReaderAppender(ITxtReaderActivity activity, RecentTxtMarkService recentTxtMarkService, ITxtReaderActivityDTO dto, TextView txtView) {
        this.recentTxtMarkService = recentTxtMarkService;
        this.activity = activity;
        this.mService = dto.getIFloatService();
        this.dto = dto;
        this.txtView = txtView;
    }

    public void reset(ITxtReaderActivity activity, RecentTxtMarkService recentTxtMarkService, ITxtReaderActivityDTO dto, TextView txtView) {
        this.recentTxtMarkService = recentTxtMarkService;
        this.activity = activity;
        this.mService = dto.getIFloatService();
        this.dto = dto;
        this.txtView = txtView;
    }

    private class TxtAppenderProcess {

        String txtContent;
        boolean isWordHtml;
        SpannableString ss;
        int maxPicWidth;
        List<Pair<Integer, Integer>> normalIgnoreLst = new ArrayList<>();

        TxtAppenderProcess(String txtContent, boolean isWordHtml, int maxPicWidth) {
            this.txtContent = txtContent;
            this.isWordHtml = isWordHtml;
            this.ss = new SpannableString(txtContent);
            this.maxPicWidth = maxPicWidth;
        }

        public SpannableString getResult() {
            if (isWordHtml) {
                wordHtmlProcess();
            }

            normalTxtProcess();

            return ss;
        }

        private boolean isNormalIgnore(Matcher mth) {
            if (normalIgnoreLst.isEmpty()) {
                return false;
            }
            int start = mth.start();
            int end = mth.end();
            for (Pair<Integer, Integer> p : normalIgnoreLst) {
                if (p.getLeft() <= start && p.getRight() >= end) {
                    return true;
                }
            }
            return false;
        }

        private void wordHtmlProcess() {
            TxtReaderAppenderForHtmlTag ruler = new TxtReaderAppenderForHtmlTag(//
                    txtContent,//
                    ss,//
                    maxPicWidth,//
                    normalIgnoreLst,//
                    dto,//
                    activity.getApplicationContext()//
            );
            ruler.apply();
        }

        private void normalTxtProcess() {
            Pattern ptn = Pattern.compile("([a-zA-Z\\-]+|[\\u4e00-\\u9fa5]+)", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(txtContent);
            final String txtContent_ = txtContent;

            Log.v(TAG, "recentTxtMark fileName = " + dto.getFileName());
            List<RecentTxtMarkDAO.RecentTxtMark> qList = recentTxtMarkService.getFileMark(dto.getFileName().toString());
            Log.v(TAG, "recentTxtMark list size = " + qList.size());

            //debug ↓↓↓↓↓↓↓↓↓↓
            if (BuildConfig.DEBUG) {
                StringBuilder sb = new StringBuilder();
                for (RecentTxtMarkDAO.RecentTxtMark v : qList) {
                    sb.append(v.getFileName() + " : " + v.getMarkEnglish() + " : " + v.getMarkIndex() + "\r\n");
                }
//                Log.line(TAG, ">> mark size : " + dto.getFileName().toString() + " -->  \n" + sb);
            }
            //debug ↑↑↑↑↑↑↑↑↑↑

            int index = 0;
            while (mth.find()) {
                final int start = mth.start();
                final int end = mth.end();

                if (isNormalIgnore(mth)) {
                    continue;
                }

                final String txtNow = mth.group(); //txtContent_.substring(start, end)
                if (StringUtils.isBlank(txtNow) || StringUtils.equals(txtNow, "-") || StringUtils.length(txtNow) == 1) {
                    continue;
                }

                WordSpan clickableSpan = new WordSpan(index) {

                    private void checkFloatServiceOn() {
                        if (!ServiceUtil.isServiceRunning(activity.getApplicationContext(), FloatViewService.class)) {
                            activity.doOnoffService(true);
                        }
                    }

                    @Override
                    public void onClick(View view) {
                        Log.v(TAG, "click " + this.id + " - " + txtNow, 0);

                        boolean isClickBookmark = false;

                        if (!dto.getBookmarkMode()) {
                            //myService.searchWordForActivity(txtNow);
                            try {
                                checkFloatServiceOn();

                                mService.searchWord(txtNow);
                            } catch (RemoteException e) {
                                Log.e(TAG, e.getMessage(), e);
                                Toast.makeText(activity.getApplicationContext(), "查詢失敗!", Toast.LENGTH_SHORT).show();
                            }

                            setMarking(true);
                        } else {
                            isClickBookmark = true;
                            setBookmarking(!this.isBookmarking());
                            putToBookmarkHolder(this);
                        }

                        // 新增單字
                        RecentTxtMarkDAO.RecentTxtMark clickVo = recentTxtMarkService.addMarkWord(dto.getFileName().toString(), txtNow, this.id, dto.getPageIndex(), isClickBookmark);

                        //debug ↓↓↓↓↓↓↓↓↓↓
                        if (BuildConfig.DEBUG) {
//                            Log.line(TAG, "MARK : " + clickVo.getFileName() + " --- " + clickVo.getMarkEnglish() + " --- " + clickVo.getMarkIndex() + ", p-" + dto.getPageIndex());
                        }
                        //debug ↑↑↑↑↑↑↑↑↑↑

                        view.invalidate();
                        txtView.invalidate();
                    }
                };

                //設定內文
                clickableSpan.setWord(txtNow);

                //補上最後一個查詢為bookmark
                Integer finalSearchIndex = getFinalSearch2Bookmark(qList);

                // 設定單字為以查詢狀態
                for (RecentTxtMarkDAO.RecentTxtMark bo : qList) {
                    if (bo.getMarkIndex() == clickableSpan.id && StringUtils.equals(bo.getMarkEnglish(), txtNow)) {
                        Log.v(TAG, "remark : " + txtNow + " - " + clickableSpan.id);

                        //書籤模式
                        if (bo.getBookmarkType() == RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.getType()) {
                            clickableSpan.setBookmarking(true);
                            putToBookmarkHolder(clickableSpan);
                        } else {
                            //查詢模式
                            clickableSpan.setMarking(true);
                        }
                    }

                    //補上最後一個查詢為bookmark
                    if (finalSearchIndex == bo.getMarkIndex()) {
                        putToBookmarkHolder(clickableSpan);
                    }
                }

                if (index % 100 == 0) {
                    Log.v(TAG, "setSpan - " + clickableSpan.id + " - " + StringUtils.substring(txtNow, 0, 10) + "...");
                }

                index++;
                ss.setSpan(clickableSpan, start, end, Spanned.SPAN_COMPOSING);// SPAN_EXCLUSIVE_EXCLUSIVE
            }
        }

        private int getFinalSearch2Bookmark(List<RecentTxtMarkDAO.RecentTxtMark> qList) {
            if (qList != null && !qList.isEmpty()) {
                RecentTxtMarkDAO.RecentTxtMark finalBo = qList.get(qList.size() - 1);
                if (finalBo.getBookmarkType() != RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.getType()) {
                    return finalBo.getMarkIndex();
                }
            }
            return -1;
        }

        private void putToBookmarkHolder(WordSpan clickableSpan) {
            dto.getBookmarkHolder().put(clickableSpan.id, clickableSpan);
        }
    }

    /**
     * 建立可點擊文件
     */
    public SpannableString getAppendTxt(String txtContent) {
        TxtAppenderProcess appender = new TxtAppenderProcess(txtContent, false, -1);
        return appender.getResult();
    }

    /**
     * 建立可點擊文件
     */
    public SpannableString getAppendTxt_HtmlFromWord(String txtContent, int maxPicWidth) {
        TxtAppenderProcess appender = new TxtAppenderProcess(txtContent, true, maxPicWidth);
        return appender.getResult();
    }

    /**
     * 建立可點擊文件
     */
    public Triple<List<SpannableString>, List<String>, List<String>> getAppendTxt_HtmlFromWord_4Epub(int spinePos, String txtContent, int maxPicWidth) {
        List<SpannableString> pageDividLst = new ArrayList<>();
        Pair<List<String>, List<String>> pair = TxtReaderAppenderPageDivider.getInst().getPages(txtContent);
        List<String> pages = pair.getLeft();
        List<String> orign4TranslateLst = pair.getRight();

        String fileName = dto.getFileName().toString();

        //拿掉原本[*]部分
        Pattern ptn = Pattern.compile("^(.*)\\[.*?\\]$");
        Matcher mth = ptn.matcher(fileName);
        if (mth.find()) {
            fileName = mth.group(1);
        }

        int pageSize = pages.size();
        EpubViewerMainHandler.EpubPageTitleHandler titleHandler = new EpubViewerMainHandler.EpubPageTitleHandler(fileName, spinePos, pageSize);

        for (int ii = 0; ii < pages.size(); ii++) {
            String page = pages.get(ii);

            dto.setFileName(titleHandler.getTitle(ii));

            TxtAppenderProcess appender = new TxtAppenderProcess(page, true, maxPicWidth);
            pageDividLst.add(appender.getResult());
        }

        return Triple.of(pageDividLst, pages, orign4TranslateLst);
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
            context.startActivity(browserIntent);
        }
    }

    public static class WordSpan extends ClickableSpan {
        int id = -1;
        private String word;
        private boolean marking = false;
        private boolean bookmarking = false;

        public WordSpan() {
        }

        public WordSpan(int id) {
            this.id = id;
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
    }
}
