package com.example.englishtester.common.epub.base;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.SpannableString;
import android.util.Log;
import android.widget.TextView;

import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.ITxtReaderActivity;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.html.image.ImageLoaderCandidate4EpubHtml;
import com.example.englishtester.common.html.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.html.parser.HtmlEpubParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

    private HTMLDocumentFactory htmlDocumentFactory;
    private MyHtmlEditorKit myHtmlEditorKit;
    private EpubSpannableTextHandler epubSpannableTextHandler;
    private Object self;
    private Navigator navigator;
    private EpubActivityInterface epubActivityInterface;
    private EpubChangePageEvent epubChangePageEvent;
    private final Handler handler = new Handler();

    private EpubDTO dto;

    public EpubViewerMainHandler(EpubActivityInterface epubActivityInterface, EpubChangePageEvent epubChangePageEvent) {
        this.myHtmlEditorKit = new MyHtmlEditorKit();
        this.epubSpannableTextHandler = new EpubSpannableTextHandler();
        this.self = this;
        this.epubActivityInterface = epubActivityInterface;
        this.epubChangePageEvent = epubChangePageEvent;
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
        } catch (Exception ex) {
            throw new RuntimeException("initBook ERR : " + ex.getMessage(), ex);
        }
    }

    public boolean isInitDone() {
        if (this.navigator != null && this.navigator.getBook() != null && dto != null && dto.getTxtView() != null && dto.getBookFile() != null) {
            return true;
        }
        return false;
    }

    public void gotoPreviousSpineSection() {
        this.navigator.gotoPreviousSpineSection(this.self);
    }

    public void gotoNextSpineSection() {
        this.navigator.gotoNextSpineSection(this.self);
    }

    public void gotoSpineSection(int spinePos) {
        this.navigator.gotoSpineSection(spinePos, this.self);
    }

    public interface EpubActivityInterface extends ITxtReaderActivity {
        void setTitle(String titleVal);

        IFloatServiceAidlInterface getFloatService();

        RecentTxtMarkService getRecentTxtMarkService();

        int getFixScreenWidth();

        String fixNameToTitle(String orignFileName);
    }

    public interface EpubChangePageEvent {
        void afterTxtViewChange();
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

            if (navigationEvent.isResourceChanged() || navigationEvent.isSpinePosChanged()) {
                Log.v(TAG, "change CONTENT !!!!");

                //設定標題
                String fileName = EpubViewerMainHandler.this.epubActivityInterface.fixNameToTitle(dto.getBookFile().getName());
                fileName = fileName + "[" + navigationEvent.getCurrentSpinePos() + "]";
                dto.setFileName(fileName);

                //設定內文
                HTMLDocument currentDocument = EpubViewerMainHandler.this.htmlDocumentFactory.getDocument(resource);
                dto.setHTMLDocument(currentDocument);
                dto.htmlContent.set(currentDocument.getHtml());

                //處理成克制版
                HtmlEpubParser wordParser = HtmlEpubParser.newInstance();
                String $tempResultContent = wordParser.getFromContent(dto.htmlContent.get());
                dto.customContent.set($tempResultContent);

                TxtReaderAppender txtReaderAppender = new TxtReaderAppender(epubActivityInterface, epubActivityInterface.getRecentTxtMarkService(), epubActivityInterface.getFloatService(), dto, EpubViewerMainHandler.this.dto.textView);
                final SpannableString spannableString = txtReaderAppender.getAppendTxt_HtmlFromWord(dto.customContent.get(), epubActivityInterface.getFixScreenWidth());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        EpubViewerMainHandler.this.epubActivityInterface.setTitle(dto.getFileName().toString());
                        EpubViewerMainHandler.this.dto.textView.setText(spannableString);
                    }
                });

                if (epubChangePageEvent != null) {
                    epubChangePageEvent.afterTxtViewChange();
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

    public static class EpubDTO implements ITxtReaderActivityDTO {
        private transient Map<Integer, TxtReaderAppender.WordSpan> bookmarkHolder;

        private File bookFile;
        private StringBuilder fileName;
        private TextView textView;
        private HTMLDocument htmlDocument;

        private AtomicReference<String> htmlContent = new AtomicReference<>();
        private AtomicReference<String> customContent = new AtomicReference<>();
        private boolean bookmarkMode = false;
        private AtomicReference<Integer> bookmarkIndexHolder = new AtomicReference<Integer>(-1);

        public EpubDTO() {
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        @Override
        public Map<Integer, TxtReaderAppender.WordSpan> getBookmarkHolder() {
            return bookmarkHolder;
        }

        @Override
        public TextView getTxtView() {
            return textView;
        }

        @Override
        public boolean isImageLoadMode() {
            return false;
        }

        @Override
        public void setBookmarkHolder(Map<Integer, TxtReaderAppender.WordSpan> bookmarkHolder) {
            this.bookmarkHolder = bookmarkHolder;
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

        public void setHTMLDocument(HTMLDocument document) {
            this.htmlDocument = document;
        }

        public Bitmap getBitmapForEpub(ImageLoaderCandidate4EpubHtml imgLoader) {
            ImageLoaderCache cache = (ImageLoaderCache) this.htmlDocument.getDocumentProperties().get("BitmapCache");
            if (StringUtils.isBlank(imgLoader.getAltData()) && StringUtils.isNotBlank(imgLoader.getSrcData())) {
                return cache.get(imgLoader.getSrcData());
            } else if (StringUtils.isBlank(imgLoader.getSrcData()) && StringUtils.isNotBlank(imgLoader.getAltData())) {
                return cache.get(imgLoader.getAltData());
            }
            return null;
        }

        public String getHtmlContent() {
            return StringUtils.trimToEmpty(htmlContent.get()).toString();
        }

        public String getCustomContent() {
            return StringUtils.trimToEmpty(customContent.get()).toString();
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
    }

    public EpubDTO getDto() {
        return dto;
    }
}
