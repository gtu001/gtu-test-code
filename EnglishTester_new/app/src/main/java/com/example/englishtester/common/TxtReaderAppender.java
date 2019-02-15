package com.example.englishtester.common;

import android.os.RemoteException;
import android.text.SpannableString;
import android.text.Spanned;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public class TxtAppenderProcess {

        String txtContent;
        boolean isWordHtml;
        SpannableString ss;
        int maxPicWidth;
        List<Pair<Integer, Integer>> normalIgnoreLst = new ArrayList<>();
        String dtoFileName;
        Map<Integer, TxtReaderAppenderSpanClass.WordSpan> bookmarkMap = new HashMap<Integer, TxtReaderAppenderSpanClass.WordSpan>();

        TxtAppenderProcess(String txtContent, boolean isWordHtml, int maxPicWidth, String dtoFileName) {
            this.txtContent = txtContent;
            this.isWordHtml = isWordHtml;
            this.ss = new SpannableString(txtContent);
            this.maxPicWidth = maxPicWidth;
            this.dtoFileName = dtoFileName;
        }

        public SpannableString getResult() {
            long startTime = System.currentTimeMillis();
            if (isWordHtml) {
                wordHtmlProcess();
            }

            normalTxtProcess();

            long duringTime = System.currentTimeMillis() - startTime;
            Log.v(TAG, "duringTime : " + duringTime);
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
            String franceCharacter = "àâéêèìôùûç";
            String germanCharacter = "äöüÄÖÜß";
            Pattern ptn = Pattern.compile("(" + EnglishSearchRegexConf.getSearchRegex(false, false) + "|[\\u4e00-\\u9fa5]+)", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(txtContent);
            final String txtContent_ = txtContent;

            final TxtReaderAppenderEscaper escaper = new TxtReaderAppenderEscaper(txtContent_);

            Log.v(TAG, "recentTxtMark fileName = " + dto.getFileName());
            List<RecentTxtMarkDAO.RecentTxtMark> qList = recentTxtMarkService.getFileMark(dtoFileName);
            Log.v(TAG, "recentTxtMark list size = " + qList.size());

            //debug ↓↓↓↓↓↓↓↓↓↓
            if (BuildConfig.DEBUG) {
                StringBuilder sb = new StringBuilder();
                for (RecentTxtMarkDAO.RecentTxtMark v : qList) {
                    sb.append(v.getFileName() + " : " + v.getMarkEnglish() + " : " + v.getMarkIndex() + "\r\n");
                }
//                Log.line(TAG, ">> mark size : " + qList.size() + " \t " + dtoFileName + " -->  \n" + sb); //TODO
            }
            //debug ↑↑↑↑↑↑↑↑↑↑

            //補上最後一個查詢為bookmark
            int finalSearchIndex = getFinalSearch2Bookmark(qList);

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

                TxtReaderAppenderSpanClass.WordSpan clickableSpan = new TxtReaderAppenderSpanClass.WordSpan(index, mth.start(), mth.end(), escaper) {//sentance

                    private void checkFloatServiceOn() {
                        if (!ServiceUtil.isServiceRunning(activity.getApplicationContext(), FloatViewService.class)) {
                            activity.doOnoffService(true);
                        }
                    }

                    @Override
                    public void onClick(View view) {
                        Log.v(TAG, "click " + this.id + " - " + txtNow, 0);

                        // 按下單字 callback
                        activity.onWordClickBefore_TxtReaderAppender(txtNow);

                        boolean isClickBookmark = false;

                        if (!dto.getBookmarkMode()) {
                            //myService.searchWordForActivity(txtNow);
                            try {
                                checkFloatServiceOn();

                                mService.searchWord(txtNow, this.getSentance());
                            } catch (RemoteException e) {
                                Log.e(TAG, e.getMessage(), e);
                                Toast.makeText(activity.getApplicationContext(), "查詢失敗!", Toast.LENGTH_SHORT).show();
                            }

                            setMarking(true);
                        } else {
                            isClickBookmark = true;
                            setBookmarking(!this.isBookmarking());
                            putToBookmarkHolder(this);

                            dto.setBookmarkMode(false);
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
                    if (finalSearchIndex != -1 && finalSearchIndex == clickableSpan.getId()) {
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
            int maxPos = -1;
            if (qList != null && !qList.isEmpty()) {
                for (RecentTxtMarkDAO.RecentTxtMark finalBo : qList) {
                    maxPos = Math.max(maxPos, finalBo.getMarkIndex());
                }
            }
            Log.v(TAG, "maxPos " + maxPos);
            return maxPos;
        }

        private void putToBookmarkHolder(TxtReaderAppenderSpanClass.WordSpan clickableSpan) {
            bookmarkMap.put(clickableSpan.id, clickableSpan);
            dto.getBookmarkHolder().put(clickableSpan.id, clickableSpan);
        }

        public Map<Integer, TxtReaderAppenderSpanClass.WordSpan> getBookmarkMap() {
            return bookmarkMap;
        }
    }

    /**
     * 建立可點擊文件
     */
    public SpannableString getAppendTxt(String txtContent, String dtoFileName) {
        TxtAppenderProcess appender = new TxtAppenderProcess(txtContent, false, -1, dtoFileName);
        return appender.getResult();
    }

    /**
     * 建立可點擊文件
     */
    public SpannableString getAppendTxt_HtmlFromWord(String txtContent, int maxPicWidth, String dtoFileName) {
        TxtAppenderProcess appender = new TxtAppenderProcess(txtContent, true, maxPicWidth, dtoFileName);
        return appender.getResult();
    }

    /**
     * 建立可點擊文件
     */
    public Triple<List<TxtAppenderProcess>, List<String>, List<String>> getAppendTxt_HtmlFromWord_4Epub(int spinePos, String txtContent, int maxPicWidth) {
        long startTime = System.currentTimeMillis();

        List<TxtAppenderProcess> pageDividLst = new ArrayList<>();
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

            TxtAppenderProcess appender = new TxtAppenderProcess(page, true, maxPicWidth, dto.getFileName().toString());
            pageDividLst.add(appender);
        }

        long duringTime = System.currentTimeMillis() - startTime;

        Log.v(TAG, "duringTime : " + duringTime);
        return Triple.of(pageDividLst, pages, orign4TranslateLst);
    }
}
