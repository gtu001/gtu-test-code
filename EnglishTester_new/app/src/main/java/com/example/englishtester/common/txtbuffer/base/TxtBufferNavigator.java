package com.example.englishtester.common.txtbuffer.base;

import com.example.englishtester.common.Log;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.html.parser.HtmlPdfParser;
import com.example.englishtester.common.html.parser.HtmlWordParser;
import com.example.englishtester.common.interf.TxtBufferActivityInterface;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class TxtBufferNavigator {

    private static final String TAG = TxtBufferNavigator.class.getSimpleName();

    private TxtBufferBookHandler mTxtBookHandler;
    private TxtBufferActivityInterface txtBufferActivityInterface;
    private TxtBufferViewerMainHandler.TxtBufferDTO dto;

    private Runnable pagesReadyEvent;
    private int currentInitSpinePos = 0;

    private SpineRangeHolder spineRangeHolder = new SpineRangeHolder();

    public TxtBufferNavigator(TxtBufferBookHandler mTxtBookHandler, TxtBufferActivityInterface txtBufferActivityInterface, TxtBufferViewerMainHandler.TxtBufferDTO dto) {
        this.mTxtBookHandler = mTxtBookHandler;
        this.txtBufferActivityInterface = txtBufferActivityInterface;
        this.dto = dto;
    }

    public void gotoFirstSpineSection(Object self) {

        StringBuffer sb = new StringBuffer();
        for(StackTraceElement s : Thread.currentThread().getStackTrace()){
            sb.append(s + "\r\n");
        }
        Log.line(TAG, "# gotoFirstSpineSection #" + sb);


        String htmlContent = this.mTxtBookHandler.getFirstPage();
        this.initSpine(htmlContent);
    }

    public void gotoNextSpineSection(Runnable pagesReadyEvent) {

        StringBuffer sb = new StringBuffer();
        for(StackTraceElement s : Thread.currentThread().getStackTrace()){
            sb.append(s + "\r\n");
        }
        Log.line(TAG, "# gotoNextSpineSection #" + sb);

        this.pagesReadyEvent = pagesReadyEvent;
        this.mTxtBookHandler.next();
        String htmlContent = this.mTxtBookHandler.getPage();
        this.initSpine(htmlContent);
    }

    public void initSpine(String htmlContent) {
        Log.line(TAG, "1----");
        if (this.mTxtBookHandler.hasNext()) {
            Log.line(TAG, "2----");
            TxtBufferViewerMainHandler.PageContentHolder pageContentHolder = new TxtBufferViewerMainHandler.PageContentHolder();

            Log.line(TAG, "3----");
            HtmlWordParser wordParser = HtmlWordParser.newInstance();
            String $tempResultContent = wordParser.getFromContent(htmlContent);

            Log.line(TAG, "4----");
            TxtReaderAppender txtReaderAppender = new TxtReaderAppender(txtBufferActivityInterface, txtBufferActivityInterface.getRecentTxtMarkService(), dto, this.dto.getTxtView());
            Triple<List<TxtReaderAppender.TxtAppenderProcess>, List<String>, List<String>> pageHolder = txtReaderAppender.getAppendTxt_HtmlFromWord_4TxtBuffer(currentInitSpinePos, $tempResultContent, txtBufferActivityInterface.getFixScreenWidth());

            Log.line(TAG, "5----");
            dto.setFileName(dto.getBookFile().getName());
            pageContentHolder.setPages(pageHolder.getLeft(), pageHolder.getMiddle(), pageHolder.getRight());
            pageContentHolder.setSpinePos(currentInitSpinePos);

            Log.line(TAG, ">>> Spine : " + currentInitSpinePos + " , pages : " + pageHolder.getMiddle().size(), 10);

            spineRangeHolder.put(currentInitSpinePos, pageContentHolder, dto.getBookFile());

            Log.line(TAG, "7----");

            currentInitSpinePos++;

            if (pagesReadyEvent != null) {
                Log.line(TAG, "8----");
                pagesReadyEvent.run();
            }
            Log.line(TAG, "9----");
        }

        Log.line(TAG, "10----");
    }

    public TxtBufferViewerMainHandler.PageContentHolder gotoPosition(int position) {
        Log.line(TAG, "P---1");
        Log.v(TAG, ">> gotoPosition " + position);
        List keys = new ArrayList<Integer>(spineRangeHolder.spineHolder.get().keySet());
        Collections.sort(keys);

        Log.line(TAG, "P---2");
        for (int ii = 0; ii < keys.size(); ii++) {
            Pair<Integer, Integer> pair = spineRangeHolder.getPageRange(ii);

            if (pair != null) {
                boolean b1 = pair.getLeft() <= position;
                boolean b2 = pair.getRight() >= position;

                if (b1 && b2) {
                    TxtBufferViewerMainHandler.PageContentHolder pageContentHolder = spineRangeHolder.spineHolder.get().get(ii);
                    int currPos = position - pair.getLeft();
                    pageContentHolder.setCurrentPageIndex(currPos);
                    Log.v(TAG, "## position : " + position + " , currPos : " + currPos + " , pair : " + pair + " , spine : " + ii + " -- " + dto.getPageIndex());
                    return pageContentHolder;
                }
            }
        }

        final ArrayBlockingQueue<Boolean> blockQueue = new ArrayBlockingQueue<Boolean>(1);
        synchronized (this) {
            Log.line(TAG, "P---3-block");
            //觸發取得下個頁面
            gotoNextSpineSection(new Runnable() {
                @Override
                public void run() {
                    try {
                        blockQueue.offer(true, 10, TimeUnit.SECONDS);
                        Log.line(TAG, "P---3-offer");
                    } catch (InterruptedException e) {
                        blockQueue.clear();
                        blockQueue.add(false);
                        Log.line(TAG, "P---3-exception");
                    }
                }
            });
        }

        try {
//            Log.line(TAG, "!!取得下個頁面");
            boolean isOk = blockQueue.take();
            Log.line(TAG, "P---4-" + isOk);
            if (!isOk) {
                throw new Exception("Spine取得超時:" + position);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return gotoPosition(position);
    }

    private static class SpineRangeHolder extends Observable {
        private AtomicReference<Map<Integer, TxtBufferViewerMainHandler.PageContentHolder>> spineHolder = new AtomicReference<>();
        private Map<Integer, String> titleMap = new HashMap<>();

        private SpineRangeHolder() {
            spineHolder.set(new HashMap<Integer, TxtBufferViewerMainHandler.PageContentHolder>());
        }

        private AtomicReference<Map<Integer, TxtBufferViewerMainHandler.PageContentHolder>> getSpineHolder() {
            return spineHolder;
        }

        private void processTitleMap(int spinePos, TxtBufferViewerMainHandler.PageContentHolder pageHolder, File bookFile) {
            String fileName = bookFile.getName();
            int pageSize = pageHolder.size();
            TxtBufferViewerMainHandler.TxtPageTitleHandler titleHandler = new TxtBufferViewerMainHandler.TxtPageTitleHandler(fileName, spinePos, pageSize);

            Pair<Integer, Integer> pageIdx = getPageRange(spinePos);
            for (int ii = pageIdx.getLeft(), idx = 0; ii <= pageIdx.getRight(); ii++, idx++) {
                titleMap.put(ii, titleHandler.getTitle(idx));
            }
        }

        private void put(int spinePos, TxtBufferViewerMainHandler.PageContentHolder pageHolder, File bookFile) {
            this.spineHolder.get().put(spinePos, pageHolder);
            this.processTitleMap(spinePos, pageHolder, bookFile);
        }

        //page index 從 0 開始 (left : include , right : include)
        private Pair<Integer, Integer> getPageRange(int spinePos) {
            try {
                int totalSize = 0;
                for (int ii = 0; ii < spinePos; ii++) {
                    totalSize += spineHolder.get().get(ii).getProcessPages().size();
                }
                int pageRight = totalSize + spineHolder.get().get(spinePos).getProcessPages().size() - 1;
                return Pair.of(totalSize, pageRight);
            } catch (Exception ex) {
                Log.e(TAG, "getPageRange spinePos : " + spinePos + " , ERR : " + ex.getMessage(), ex);
                return null;
            }
        }
    }

    public Map<Integer, String> getTitleMap() {
        return spineRangeHolder.titleMap;
    }
}
