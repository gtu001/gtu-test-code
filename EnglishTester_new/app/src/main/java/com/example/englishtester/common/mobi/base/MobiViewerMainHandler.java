package com.example.englishtester.common.mobi.base;

import android.graphics.Bitmap;
import android.text.SpannableString;
import android.widget.TextView;

import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.TxtReaderAppenderSpanClass;
import com.example.englishtester.common.epub.base.HTMLDocument;
import com.example.englishtester.common.html.image.ImageLoaderCandidate4EpubHtml;
import com.example.englishtester.common.interf.ITxtReaderActivity;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.interf.MobiActivityInterface;

import org.apache.commons.lang3.StringUtils;
import org.rr.mobi4java.MobiDocument;
import org.rr.mobi4java.MobiReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MobiViewerMainHandler {
    private static final String TAG = MobiViewerMainHandler.class.getSimpleName();

    private Object self;
    private MobiNavigator navigator;
    private MobiActivityInterface epubActivityInterface;
    private MobiBookHandler mMobiBookHandler;


    private MobiDTO dto;

    public MobiViewerMainHandler(MobiActivityInterface epubActivityInterface) {
        this.self = this;
        this.epubActivityInterface = epubActivityInterface;
        this.dto = new MobiDTO(epubActivityInterface, this);
    }

    public void setTextView(TextView textView) {
        this.dto.setTextView(textView);
    }

    public void initBook(File bookFile) {
        try {
            this.dto.setBookFile(bookFile);
            this.dto.getGoDirectLinkStack().clear();
            InputStream bookStream = new FileInputStream(bookFile);
            MobiDocument doc = new MobiReader().read(bookStream);
            this.mMobiBookHandler = new MobiBookHandler(doc);
            this.navigator = new MobiNavigator(mMobiBookHandler, this.epubActivityInterface, this.dto);
        } catch (Exception ex) {
            throw new RuntimeException("initBook ERR : " + ex.getMessage(), ex);
        }
    }

    public boolean isInitDone() {
        if (mMobiBookHandler != null && this.navigator != null) {
            return true;
        }
        return false;
    }

    public void gotoFirstSpineSection(Runnable pagesReadyEvent) {
        this.navigator.gotoFirstSpineSection(this.self);
    }

    public void triggerEvent() {
    }

    public enum PageForwardEnum {
        NEXT_PAGE(), //
        PREVIOUS_PAGE(), //
        JUMP_SPINE_SECTION(), //
    }

    public static class PageContentHolder {
        private AtomicInteger spinePos = new AtomicInteger(-1);
        private List<TxtReaderAppender.TxtAppenderProcess> processPages;
        private Map<Integer, SpannableString> pageMap = new HashMap<Integer, SpannableString>();
        private List<String> pages4Debug;
        private List<String> translateLst;
        private String[] translateDoneArry;

        private int currentPageIndex = 0;

        private AtomicReference<String> htmlContent = new AtomicReference<>();
        private AtomicReference<String> customContent = new AtomicReference<>();

        public boolean isEmpty() {
            return processPages == null || processPages.isEmpty();
        }

        private SpannableString getSpannablePage(int index) {
            if (!pageMap.containsKey(index)) {
                SpannableString span = processPages.get(index).getResult();
                pageMap.put(index, span);
            }
            return pageMap.get(index);
        }

        public Map<Integer, TxtReaderAppenderSpanClass.WordSpan> getBookmarkMap() {
            try {
                return this.processPages.get(this.currentPageIndex).getBookmarkMap();
            } catch (Exception ex) {
                return new HashMap<>();
            }
        }

        public SpannableString getCurrentPage() {
            return getSpannablePage(currentPageIndex);
        }

        public int size() {
            return processPages.size();
        }

        public String getPageContent4Debug() {
            return pages4Debug.get(currentPageIndex);
        }

        public SpannableString gotoPage(int index) {
            if (index < 0) {
                currentPageIndex = 0;
            } else if (index >= processPages.size()) {
                currentPageIndex = processPages.size() - 1;
            }
            return getSpannablePage(currentPageIndex);
        }

        public void setTranslateDoneText(String text) {
            this.translateDoneArry[currentPageIndex] = text;
        }

        public String getTranslateDoneText() {
            return this.translateDoneArry[currentPageIndex];
        }

        public String getTranslateOrignText() {
            return this.translateLst.get(currentPageIndex);
        }

        public void setPages(List<TxtReaderAppender.TxtAppenderProcess> pages, List<String> pages4Debug, List<String> page4Translate) {
            this.processPages = pages;
            this.pages4Debug = pages4Debug;
            this.currentPageIndex = 0;
            this.translateLst = page4Translate;
            this.translateDoneArry = new String[pages.size()];
        }

        public boolean hasNext() {
            if (this.currentPageIndex + 1 >= processPages.size()) {
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
            return getSpannablePage(this.currentPageIndex);
        }

        public SpannableString previousPage() {
            if (!hasPrevious()) {
                return null;
            }
            this.currentPageIndex--;
            return getSpannablePage(this.currentPageIndex);
        }

        public SpannableString firstPage() {
            this.currentPageIndex = 0;
            return getSpannablePage(this.currentPageIndex);
        }

        public SpannableString lastPage() {
            this.currentPageIndex = this.processPages.size() - 1;
            return getSpannablePage(this.currentPageIndex);
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

        public List<TxtReaderAppender.TxtAppenderProcess> getProcessPages() {
            return processPages;
        }

        public void setCurrentPageIndex(int currentPageIndex) {
            this.currentPageIndex = currentPageIndex;
        }
    }

    public static class MobiDTO implements ITxtReaderActivityDTO {
        private File bookFile;

        private StringBuilder fileName;
        private TextView textView;
        private HTMLDocument htmlDocument;

        private boolean bookmarkMode = false;
        private AtomicReference<Integer> bookmarkIndexHolder = new AtomicReference<Integer>(-1);
        private PageForwardEnum pageForwardEnum;
        //        private BookStatusHolder bookStatusHolder;
        private MobiActivityInterface epubActivityInterface;
        private MobiViewerMainHandler handler;
        private boolean goDirectLink;
        private Stack<Integer> goDirectLinkStack = new Stack<Integer>();

        private int pageIndex = -1;

        public MobiDTO(MobiActivityInterface epubActivityInterface, MobiViewerMainHandler handler) {
            this.epubActivityInterface = epubActivityInterface;
            this.handler = handler;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public void gotoLink(String link) {
        }

        @Override
        public Map<Integer, TxtReaderAppenderSpanClass.WordSpan> getBookmarkHolder() {
            final MobiViewerMainHandler.PageContentHolder holder = handler.gotoPosition(getPageIndex());
            return holder.getBookmarkMap();
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
        public IFloatServiceAidlInterface getIFloatService() {
            return epubActivityInterface.getFloatService();
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
            } catch (OutOfMemoryError ex) {
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

//        public SpineRangeHolder getSpineRangeHolder() {
//            return bookStatusHolder.spineRangeHolder;
//        }

        @Override
        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public void setGoDirectLink(boolean goDirectLink) {
            this.goDirectLink = goDirectLink;
        }

        public boolean isGoDirectLink() {
            return goDirectLink;
        }

        public Stack<Integer> getGoDirectLinkStack() {
            return goDirectLinkStack;
        }

        public void setGoDirectLinkStack(Stack<Integer> goDirectLinkStack) {
            this.goDirectLinkStack = goDirectLinkStack;
        }
    }


    public MobiDTO getDto() {
        return dto;
    }

    public boolean isReady4Position() {
        if (!this.isInitDone()) {
            return false;
        }
        return true;
    }

    public PageContentHolder gotoPosition(int position) {
        return this.navigator.gotoPosition(position);
    }

    public static class MobiPageTitleHandler {

        String fileName;
        int currentSpinePos;
        int pageSize;

        public MobiPageTitleHandler(String fileName, int currentSpinePos, int pageSize) {
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
        return this.navigator.getTitleMap().get(position);
    }
}