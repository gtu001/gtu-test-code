package com.example.englishtester.common.epub.base;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.SpannableString;
import android.widget.TextView;

import com.example.englishtester.DropboxFileLoadService;
import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.ITxtReaderActivity;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.html.image.ImageLoaderCandidate4EpubHtml;
import com.example.englishtester.common.html.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.html.parser.HtmlEpubParser;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.util.ClassUtil;
import nl.siegmann.epublib.browsersupport.NavigationEvent;
import nl.siegmann.epublib.browsersupport.NavigationEventListener;
import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by wistronits on 2018/8/7.
 */

public class EpubViewerMainHandler {

    private static final String TAG = EpubViewerMainHandler.class.getSimpleName();

    private HTMLDocumentFactory htmlDocumentFactory;
    private MyHtmlEditorKit myHtmlEditorKit;
    private EpubSpannableTextHandler epubSpannableTextHandler;
    private Object self;
    private Navigator navigator;
    private EpubActivityInterface epubActivityInterface;
    private final Handler handler = new Handler();
    private Runnable pagesReadyEvent;


    private EpubDTO dto;

    public EpubViewerMainHandler(EpubActivityInterface epubActivityInterface) {
        this.myHtmlEditorKit = new MyHtmlEditorKit();
        this.epubSpannableTextHandler = new EpubSpannableTextHandler();
        this.self = this;
        this.epubActivityInterface = epubActivityInterface;
        this.dto = new EpubDTO();
    }

    public void setTextView(TextView textView) {
        this.dto.setTextView(textView);
    }

    public void initBook(File bookFile) {
        try {
            InputStream bookStream = new FileInputStream(bookFile);
            Book book = (new EpubReader()).readEpub(bookStream);
            this.navigator = new Navigator(book);
            this.htmlDocumentFactory = new HTMLDocumentFactory(navigator, myHtmlEditorKit, self, epubSpannableTextHandler);
            this.dto.setBookFile(bookFile);
            this.dto.bookStatusHolder = new BookStatusHolder();
        } catch (Exception ex) {
            throw new RuntimeException("initBook ERR : " + ex.getMessage(), ex);
        }
    }

    public boolean isInitDone() {
        if (this.navigator != null && this.navigator.getBook() != null && dto != null && dto.getTxtView() != null && dto.getBookFile() != null) {
            return true;
        }
        Log.v(TAG, "isInitDone fail : 1:" + (this.navigator != null) + " , 2:"
                        + (this.navigator != null && this.navigator.getBook() != null) + " , 3:"
                        + (this.dto != null) + " , 4:"
                        + (this.dto.getTxtView() != null) + " , 5:"
                        + (this.dto.getBookFile() != null) + ""
                , 0
        );
        return false;
    }

    public void gotoFirstSpineSection(Runnable pagesReadyEvent) {
        setPagesReadyEvent(pagesReadyEvent);
        this.navigator.gotoFirstSpineSection(this.self);
    }

    public void gotoPreviousSpineSection(Runnable pagesReadyEvent) {
        setPagesReadyEvent(pagesReadyEvent);
        this.navigator.gotoPreviousSpineSection(this.self);
    }

    public void gotoNextSpineSection(Runnable pagesReadyEvent) {
        setPagesReadyEvent(pagesReadyEvent);
        this.navigator.gotoNextSpineSection(this.self);
    }

    public void gotoSpineSection(int spinePos, Runnable pagesReadyEvent) {
        setPagesReadyEvent(pagesReadyEvent);
        this.navigator.gotoSpineSection(spinePos, this.self);
    }

    public int getCurrentSpinePos() {
        return this.navigator.getCurrentSpinePos();
    }

    public static class MyNavigationEvent extends NavigationEvent {
        public MyNavigationEvent(Object source, Navigator navigator) {
            super(source, navigator);
        }
    }

    public void triggerEvent() {
        Log.v(TAG, "#- triggerEvent start");
        try {
            MyNavigationEvent event = new MyNavigationEvent(self, navigator);
            Method mth = Navigator.class.getDeclaredMethod("handleEventListeners", NavigationEvent.class);
            mth.setAccessible(true);
            mth.invoke(navigator, event);
        } catch (Exception e) {
            Log.e(TAG, "triggerEvent ERR : " + e.getMessage(), e);
        }
    }

    public interface EpubActivityInterface extends ITxtReaderActivity {
        void setTitle(String titleVal);

        IFloatServiceAidlInterface getFloatService();

        RecentTxtMarkService getRecentTxtMarkService();

        int getFixScreenWidth();
    }

    private class EpubSpannableTextHandler implements NavigationEventListener {
        private final String TAG = EpubSpannableTextHandler.class.getSimpleName();

        private EpubSpannableTextHandler() {
        }

        @Override
        public void navigationPerformed(NavigationEvent navigationEvent) {
            Log.v(TAG, "navigationPerformed start -------------------------------------------------------------");
            Log.v(TAG, "navigationPerformed debug start -------------------------------------------------------------");
            for (Method m : NavigationEvent.class.getDeclaredMethods()) {
                if (ClassUtil.isPrimitiveOrWrapper(m.getReturnType()) || m.getReturnType() == String.class) {
                    try {
                        Object value = MethodUtils.invokeMethod(navigationEvent, m.getName());
                        Log.v(TAG, "\t" + m.getName() + "\t" + value);
                    } catch (Exception e) {
                        Log.e(TAG, "navigationPerformed err : " + m.getName() + " --> " + e.getMessage());
                    }
                }
            }

            Log.v(TAG, "navigationPerformed debug end   -------------------------------------------------------------");

            Resource resource = navigationEvent.getCurrentResource();
            Resource resource2 = navigationEvent.getOldResource();

            Log.v(TAG, "resource1 " + resource);
            Log.v(TAG, "resource2 " + resource2);

            if (navigationEvent.isResourceChanged() || navigationEvent.isSpinePosChanged() || navigationEvent instanceof MyNavigationEvent) {
                Log.v(TAG, "change CONTENT !!!!", 10);

                PageContentHolder pageContentHolder = null;
                if (!dto.bookStatusHolder.spineRangeHolder.getSpineHolder().get().containsKey(navigationEvent.getCurrentSpinePos())) {
                    pageContentHolder = new PageContentHolder();

                    //設定內文
                    HTMLDocument currentDocument = EpubViewerMainHandler.this.htmlDocumentFactory.getDocument(resource);
                    dto.htmlDocument = currentDocument;
                    pageContentHolder.htmlContent.set(currentDocument.getHtml());

                    //處理成克制版
                    HtmlEpubParser wordParser = HtmlEpubParser.newInstance();
                    String $tempResultContent = wordParser.getFromContent(pageContentHolder.htmlContent.get());
                    pageContentHolder.customContent.set($tempResultContent);

                    dto.setFileName(dto.getBookFile().getName());
                    TxtReaderAppender txtReaderAppender = new TxtReaderAppender(epubActivityInterface, epubActivityInterface.getRecentTxtMarkService(), epubActivityInterface.getFloatService(), dto, EpubViewerMainHandler.this.dto.textView);
                    Pair<List<SpannableString>, List<String>> pageHolder = txtReaderAppender.getAppendTxt_HtmlFromWord_4Epub(navigationEvent.getCurrentSpinePos(), pageContentHolder.customContent.get(), epubActivityInterface.getFixScreenWidth());

                    pageContentHolder.setPages(pageHolder.getLeft(), pageHolder.getRight());
                    pageContentHolder.setSpinePos(navigationEvent.getCurrentSpinePos());

                    dto.bookStatusHolder.spineRangeHolder.put(navigationEvent.getCurrentSpinePos(), pageContentHolder, dto.getBookFile());

//                    Log.line(TAG, "PUT________" + navigationEvent.getCurrentSpinePos());
                } else {
                    Log.v(TAG, "unchange CONTENT !!!!", 10);
                }

                if (pagesReadyEvent != null) {
                    pagesReadyEvent.run();
                }
            }

            Log.v(TAG, "navigationPerformed end   -------------------------------------------------------------");
        }

        private String getHtmlFromResource(Resource resource) {
            BufferedReader reader = null;
            try {
                StringBuffer sb = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), resource.getInputEncoding()));
                for (String line = null; (line = reader.readLine()) != null; ) {
                    sb.append(line);
                    sb.append("\r\n");
                }
                return sb.toString();
            } catch (IOException e) {
                throw new RuntimeException("read resource failed " + e.getMessage(), e);
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public enum PageForwardEnum {
        NEXT_PAGE(), //
        PREVIOUS_PAGE(), //
        JUMP_SPINE_SECTION(), //
    }

    public static class PageContentHolder {
        private AtomicInteger spinePos = new AtomicInteger(-1);
        private List<SpannableString> pages;
        private List<String> pages4Debug;

        private int currentPageIndex = 0;

        private AtomicReference<String> htmlContent = new AtomicReference<>();
        private AtomicReference<String> customContent = new AtomicReference<>();

        public boolean isEmpty() {
            return pages == null || pages.isEmpty();
        }

        public SpannableString getCurrentPage() {
            return pages.get(currentPageIndex);
        }

        public int size() {
            return pages.size();
        }

        public String getPageContent4Debug() {
            return pages4Debug.get(currentPageIndex);
        }

        public SpannableString gotoPage(int index) {
            if (index < 0) {
                currentPageIndex = 0;
            } else if (index >= pages.size()) {
                currentPageIndex = pages.size() - 1;
            }
            return pages.get(currentPageIndex);
        }

        public void setPages(List<SpannableString> pages, List<String> pages4Debug) {
            this.pages = pages;
            this.pages4Debug = pages4Debug;
            this.currentPageIndex = 0;
        }

        public boolean hasNext() {
            if (this.currentPageIndex + 1 >= pages.size()) {
                return false;
            }
            return true;
        }

        public boolean hasPrevious() {
            if (this.currentPageIndex - 1 < 0) {
                return false;
            }
            return true;
        }

        public SpannableString nextPage() {
            if (!hasNext()) {
                return null;
            }
            this.currentPageIndex++;
            return this.pages.get(this.currentPageIndex);
        }

        public SpannableString previousPage() {
            if (!hasPrevious()) {
                return null;
            }
            this.currentPageIndex--;
            return this.pages.get(this.currentPageIndex);
        }

        public SpannableString firstPage() {
            this.currentPageIndex = 0;
            return this.pages.get(this.currentPageIndex);
        }

        public SpannableString lastPage() {
            this.currentPageIndex = this.pages.size() - 1;
            return this.pages.get(this.currentPageIndex);
        }

        public int getSpinePos() {
            return spinePos.get();
        }

        public void setSpinePos(int spinePos) {
            this.spinePos.set(spinePos);
        }

        public AtomicReference<String> getHtmlContent() {
            return htmlContent;
        }

        public AtomicReference<String> getCustomContent() {
            return customContent;
        }

        public int getCurrentPageIndex() {
            return currentPageIndex;
        }
    }

    public static class EpubDTO implements ITxtReaderActivityDTO {
        private File bookFile;
        private StringBuilder fileName;
        private TextView textView;
        private HTMLDocument htmlDocument;

        private boolean bookmarkMode = false;
        private AtomicReference<Integer> bookmarkIndexHolder = new AtomicReference<Integer>(-1);
        private PageForwardEnum pageForwardEnum;
        private BookStatusHolder bookStatusHolder;

        private int pageIndex = -1;

        public EpubDTO() {
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        @Override
        public Map<Integer, TxtReaderAppender.WordSpan> getBookmarkHolder() {
            return bookStatusHolder.bookmarkHolder.getBookmarkHolder(this.getFileName().toString());
        }

        @Override
        public TextView getTxtView() {
            return textView;
        }

        @Override
        public boolean isImageLoadMode() {
            return false;
        }

        public void setFileName(String fname) {
            if (this.fileName == null) {
                this.fileName = new StringBuilder();
            }
            this.fileName.delete(0, fileName.length());
            this.fileName.append(fname);
        }

        @Override
        public StringBuilder getFileName() {
            if (this.fileName == null) {
                this.fileName = new StringBuilder();
            }
            return this.fileName;
        }

        public Bitmap getBitmapForEpub(ImageLoaderCandidate4EpubHtml imgLoader) {
            try {
                ImageLoaderCache cache = (ImageLoaderCache) this.htmlDocument.getDocumentProperties().get(ImageLoaderCache.BITMAP_RESOURCE_KEY);
                if (StringUtils.isNotBlank(imgLoader.getAltData()) && StringUtils.isNotBlank(imgLoader.getSrcData())) {
                    if (DropboxFileLoadService.isPicFileType(imgLoader.getSrcData())) {
                        return cache.get(imgLoader.getSrcData());
                    } else {
                        return cache.get(imgLoader.getAltData());
                    }
                } else {
                    if (StringUtils.isBlank(imgLoader.getAltData()) && StringUtils.isNotBlank(imgLoader.getSrcData())) {
                        return cache.get(imgLoader.getSrcData());
                    } else if (StringUtils.isBlank(imgLoader.getSrcData()) && StringUtils.isNotBlank(imgLoader.getAltData())) {
                        return cache.get(imgLoader.getAltData());
                    }
                }
                throw new Exception("無法判斷的pic : [alt]" + imgLoader.getAltData() + ", [src]" + imgLoader.getSrcData());
            } catch (java.lang.OutOfMemoryError ex) {
                Log.line(TAG, " !!! OutOfMemoryError : " + this.htmlDocument.getId() + " , " + this.htmlDocument.getTitle() + " , " + this.htmlDocument.getHref(), ex);
            } catch (Throwable ex) {
                Log.line(TAG, "getBitmapForEpub ERR : " + ex.getMessage(), ex);
                throw new RuntimeException("getBitmapForEpub ERR : " + ex.getMessage(), ex);
            }
            return null;
        }

        public File getBookFile() {
            return bookFile;
        }

        public void setBookFile(File bookFile) {
            this.bookFile = bookFile;
        }


        @Override
        public boolean getBookmarkMode() {
            return bookmarkMode;
        }

        public void setBookmarkMode(boolean bookmarkMode) {
            this.bookmarkMode = bookmarkMode;
        }

        @Override
        public AtomicReference<Integer> getBookmarkIndexHolder() {
            return bookmarkIndexHolder;
        }

        public PageForwardEnum getPageForwardEnum() {
            return pageForwardEnum;
        }

        public void setPageForwardEnum(PageForwardEnum pageForwardEnum) {
            this.pageForwardEnum = pageForwardEnum;
        }

        public SpineRangeHolder getSpineRangeHolder() {
            return bookStatusHolder.spineRangeHolder;
        }

        @Override
        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }
    }

    private static class BookmarkHolder {

        Map<String, Map<Integer, TxtReaderAppender.WordSpan>> fileNameBookmark = new HashMap<>();

        Map<Integer, TxtReaderAppender.WordSpan> getBookmarkHolder(String currentFileName) {
            Assert.assertTrue(StringUtils.isNotBlank(currentFileName));
            if (!fileNameBookmark.containsKey(currentFileName)) {
                fileNameBookmark.put(currentFileName, new TreeMap<Integer, TxtReaderAppender.WordSpan>());
            }
            return fileNameBookmark.get(currentFileName);
        }
    }

    private static class BookStatusHolder {
        private SpineRangeHolder spineRangeHolder = new SpineRangeHolder();
        private BookmarkHolder bookmarkHolder = new BookmarkHolder();
    }

    private static class SpineRangeHolder extends Observable {
        private AtomicReference<Map<Integer, PageContentHolder>> spineHolder = new AtomicReference<>();
        private Map<Integer, String> titleMap = new HashMap<>();

        private SpineRangeHolder() {
            spineHolder.set(new HashMap<Integer, PageContentHolder>());
        }

        private AtomicReference<Map<Integer, PageContentHolder>> getSpineHolder() {
            return spineHolder;
        }

        private void processTitleMap(int spinePos, PageContentHolder pageHolder, File bookFile) {
            String fileName = bookFile.getName();
            int pageSize = pageHolder.size();
            EpubViewerMainHandler.EpubPageTitleHandler titleHandler = new EpubViewerMainHandler.EpubPageTitleHandler(fileName, spinePos, pageSize);

            Pair<Integer, Integer> pageIdx = getPageRange(spinePos);
            for (int ii = pageIdx.getLeft(), idx = 0; ii <= pageIdx.getRight(); ii++, idx++) {
                titleMap.put(ii, titleHandler.getTitle(idx));
            }
        }

        private void put(int spinePos, PageContentHolder pageHolder, File bookFile) {
            this.spineHolder.get().put(spinePos, pageHolder);
            this.processTitleMap(spinePos, pageHolder, bookFile);
        }

        //page index 從 0 開始 (left : include , right : include)
        private Pair<Integer, Integer> getPageRange(int spinePos) {
            try {
                int totalSize = 0;
                for (int ii = 0; ii < spinePos; ii++) {
                    totalSize += spineHolder.get().get(ii).pages.size();
                }
                int pageRight = totalSize + spineHolder.get().get(spinePos).pages.size() - 1;
                return Pair.of(totalSize, pageRight);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public EpubDTO getDto() {
        return dto;
    }

    public void setPagesReadyEvent(Runnable pagesReadyEvent) {
        this.pagesReadyEvent = pagesReadyEvent;
    }

    public boolean isReady4Position() {
        if (!this.isInitDone()) {
            return false;
        }
        if (dto == null || dto.bookStatusHolder == null || dto.bookStatusHolder.spineRangeHolder == null ||
                dto.bookStatusHolder.spineRangeHolder.spineHolder == null || dto.bookStatusHolder.spineRangeHolder.spineHolder.get() == null) {
            return false;
        }
        if (dto.bookStatusHolder.spineRangeHolder.spineHolder.get().isEmpty()) {
            return false;
        }
        return true;
    }

    public PageContentHolder gotoPosition(int position) {
        List keys = new ArrayList<Integer>(dto.bookStatusHolder.spineRangeHolder.spineHolder.get().keySet());
        Collections.sort(keys);
        for (int ii = 0; ii < keys.size(); ii++) {
            Pair<Integer, Integer> pair = dto.bookStatusHolder.spineRangeHolder.getPageRange(ii);

            if (pair != null) {
                boolean b1 = pair.getLeft() <= position;
                boolean b2 = pair.getRight() >= position;

                if (b1 && b2) {
                    PageContentHolder pageContentHolder = dto.bookStatusHolder.spineRangeHolder.spineHolder.get().get(ii);
                    int currPos = position - pair.getLeft();
                    pageContentHolder.currentPageIndex = currPos;
//                    Log.line(TAG, "## position : " + position + " , currPos : " + currPos + " , pair : " + pair + " , spine : " + ii + " -- " + dto.getPageIndex());
                    return pageContentHolder;
                }
            }
        }

        final ArrayBlockingQueue<Boolean> blockQueue = new ArrayBlockingQueue<Boolean>(1);

        //觸發取得下個頁面
        gotoNextSpineSection(new Runnable() {
            @Override
            public void run() {
                blockQueue.add(true);
            }
        });

        try {
//            Log.line(TAG, "!!取得下個頁面");
            blockQueue.take();
        } catch (InterruptedException e) {
        }
        return gotoPosition(position);
    }

    public static class EpubPageTitleHandler {

        String fileName;
        int currentSpinePos;
        int pageSize;

        public EpubPageTitleHandler(String fileName, int currentSpinePos, int pageSize) {
            this.fileName = fileName;
            this.currentSpinePos = currentSpinePos;
            this.pageSize = pageSize;
        }

        public String getTitle(int index) {
            String pageStatus = getPageStatus(index);
            String title = fixNameToTitle(this.fileName) + "[" + pageStatus + "]";
            return title;
        }

        private String getPageStatus(int index) {
            String divideDetial = getPageDivideStatus(index);
            divideDetial = StringUtils.isBlank(divideDetial) ? "" : " " + divideDetial;
            return this.currentSpinePos + divideDetial;
        }

        private String getPageDivideStatus(int index) {
            if (this.pageSize <= 1) {
                return "";
            }
            return String.format("(%d/%d)", index + 1, this.pageSize);
        }

        public static String fixNameToTitle(String orignFileName) {
            orignFileName = StringUtils.trimToEmpty(orignFileName);
            Pattern ptn = Pattern.compile("(.*?)\\.(?:epub)", Pattern.CASE_INSENSITIVE);
            Matcher mth = ptn.matcher(orignFileName);
            if (mth.find()) {
                return mth.group(1);
            }
            return orignFileName;
        }
    }

    public String getCurrentTitle(int position) {
        return dto.bookStatusHolder.spineRangeHolder.titleMap.get(position);
    }
}