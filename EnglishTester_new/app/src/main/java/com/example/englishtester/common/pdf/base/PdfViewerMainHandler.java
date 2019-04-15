package com.example.englishtester.common.pdf.base;

import android.graphics.Bitmap;
import android.text.SpannableString;
import android.widget.TextView;

import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.OOMHandler2;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.TxtReaderAppenderSpanClass;
import com.example.englishtester.common.html.image.ImageLoaderCandidate4PdfHtml;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.interf.PdfActivityInterface;

import org.apache.commons.lang3.StringUtils;

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


public class PdfViewerMainHandler {
    private static final String TAG = PdfViewerMainHandler.class.getSimpleName();

    private Object self;
    private PdfNavigator navigator;
    private PdfActivityInterface PdfActivityInterface;
    private PdfBookHandler mPdfBookHandler;


    private PdfDTO dto;

    public PdfViewerMainHandler(PdfActivityInterface PdfActivityInterface) {
        this.self = this;
        this.PdfActivityInterface = PdfActivityInterface;
        this.dto = new PdfDTO(PdfActivityInterface, this);
    }

    public void setTextView(TextView textView) {
        this.dto.setTextView(textView);
    }

    public void initBook(File bookFile) {
        try {
            this.dto.setBookFile(bookFile);
            this.dto.getGoDirectLinkStack().clear();
            InputStream bookStream = new FileInputStream(bookFile);
            this.mPdfBookHandler = new PdfBookHandler(bookFile);
            this.navigator = new PdfNavigator(mPdfBookHandler, this.PdfActivityInterface, this.dto);
        } catch (Exception ex) {
            throw new RuntimeException("initBook ERR : " + ex.getMessage(), ex);
        }
    }

    public boolean isInitDone() {
        if (mPdfBookHandler != null && this.navigator != null) {
            return true;
        }
        return false;
    }

    public void gotoFirstSpineSection(Runnable pagesReadyEvent) {
        this.navigator.gotoFirstSpineSection(this.self);
    }

    public void triggerEvent() {
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

    public static class PdfDTO implements ITxtReaderActivityDTO {
        private File bookFile;

        private StringBuilder fileName;
        private TextView textView;

        private boolean bookmarkMode = false;
        private AtomicReference<Integer> bookmarkIndexHolder = new AtomicReference<Integer>(-1);
        //        private BookStatusHolder bookStatusHolder;
        private PdfActivityInterface epubActivityInterface;
        private PdfViewerMainHandler handler;
        private boolean goDirectLink;
        private Stack<Integer> goDirectLinkStack = new Stack<Integer>();

        private int pageIndex = 0;

        public PdfDTO(PdfActivityInterface epubActivityInterface, PdfViewerMainHandler handler) {
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
            final PdfViewerMainHandler.PageContentHolder holder = handler.gotoPosition(getPageIndex());
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

        public Bitmap getBitmapForPdf(ImageLoaderCandidate4PdfHtml imgLoader) {
            return this.handler.getBitmapForPdf(imgLoader);
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

    private Bitmap getBitmapForPdf(ImageLoaderCandidate4PdfHtml imgLoader) {
        try {
//            if (StringUtils.isNotBlank(imgLoader.getAltData()) && StringUtils.isNotBlank(imgLoader.getSrcData())) {
//                byte[] imageByteArry = this.mPdfBookHandler.getImage(imgLoader.getSrcData());
//                return OOMHandler2.byteArrayToBitmap(imageByteArry);
//            }
//            throw new Exception("無法判斷的pic : [alt]" + imgLoader.getAltData() + ", [src]" + imgLoader.getSrcData());
            throw new UnsupportedOperationException("getBitmapForPdf");
        } catch (OutOfMemoryError ex) {
            Log.line(TAG, " !!! OutOfMemoryError : " + imgLoader.getAltData() + " , " + imgLoader.getSrcData() + " , ERR : " + ex.getMessage(), ex);
        } catch (Throwable ex) {
            Log.line(TAG, "getBitmapForEpub ERR : " + imgLoader.getAltData() + " , " + imgLoader.getSrcData() + " , ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("getBitmapForEpub ERR : " + imgLoader.getAltData() + " , " + imgLoader.getSrcData() + " , ERR : " + ex.getMessage(), ex);
        }
        return null;
    }


    public PdfDTO getDto() {
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

    public static class PdfPageTitleHandler {

        String fileName;
        int currentSpinePos;
        int pageSize;

        public PdfPageTitleHandler(String fileName, int currentSpinePos, int pageSize) {
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
            Pattern ptn = Pattern.compile("(.*?)\\.(?:Pdf)", Pattern.CASE_INSENSITIVE);
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