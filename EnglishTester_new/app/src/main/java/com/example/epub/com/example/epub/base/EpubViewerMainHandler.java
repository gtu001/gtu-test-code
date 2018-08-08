package com.example.epub.com.example.epub.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.BuildConfig;
import com.example.englishtester.FloatViewService;
import com.example.englishtester.RecentTxtMarkDAO;
import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.ITxtReaderActivity;
import com.example.englishtester.common.ServiceUtil;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.TxtReaderAppenderForHtmlTag;
import com.example.englishtester.common.html.image.ImageLoaderCandidate4EpubHtml;
import com.example.englishtester.common.html.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.html.parser.HtmlEpubParser;
import com.example.englishtester.common.html.parser.HtmlWordParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.util.ClassUtil;
import nl.siegmann.epublib.browsersupport.NavigationEvent;
import nl.siegmann.epublib.browsersupport.NavigationEventListener;
import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;

/**
 * Created by wistronits on 2018/8/7.
 */

public class EpubViewerMainHandler {

    private HTMLDocumentFactory htmlDocumentFactory;
    private MyHtmlEditorKit myHtmlEditorKit;
    private TextView textView;
    private EpubSpannableTextHandler epubSpannableTextHandler;
    private Object self;
    private Navigator navigator;
    private EpubActivityInterface epubActivityInterface;

    private AtomicReference<String> htmlContent = new AtomicReference<>();
    private AtomicReference<String> customContent = new AtomicReference<>();

    private EpubDTO dto;

    public EpubViewerMainHandler(TextView textView, EpubActivityInterface epubActivityInterface) {
        this.myHtmlEditorKit = new MyHtmlEditorKit();
        this.textView = textView;
        this.epubSpannableTextHandler = new EpubSpannableTextHandler();
        this.self = textView;
        this.epubActivityInterface = epubActivityInterface;
        this.dto = new EpubDTO(this.textView);
    }

    public void initBook(Book book) {
        this.navigator = new Navigator(book);
        this.htmlDocumentFactory = new HTMLDocumentFactory(navigator, myHtmlEditorKit, self, epubSpannableTextHandler);
    }

    public void gotoPreviousSpineSection() {
        this.navigator.gotoPreviousSpineSection(this.self);
    }

    public void gotoNextSpineSection() {
        this.navigator.gotoNextSpineSection(this.self);
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

            if (resource != null && resource != resource2) {
                Log.v(TAG, "change CONTENT !!!!");

                //設定標題
                epubActivityInterface.setTitle(resource.getTitle());
                dto.setFileName(resource.getTitle());

                //設定內文
                HTMLDocument currentDocument = EpubViewerMainHandler.this.htmlDocumentFactory.getDocument(resource);
                dto.setHTMLDocument(currentDocument);
                htmlContent.set(currentDocument.getHtml());

                //處理成克制版
                HtmlEpubParser wordParser = HtmlEpubParser.newInstance();
                String $tempResultContent = wordParser.getFromContent(htmlContent.get());
                customContent.set($tempResultContent);

                TxtReaderAppender txtReaderAppender = new TxtReaderAppender(epubActivityInterface, epubActivityInterface.getRecentTxtMarkService(), epubActivityInterface.getFloatService(), dto, EpubViewerMainHandler.this.textView);
                SpannableString spannableString = txtReaderAppender.getAppendTxt_HtmlFromWord(customContent.get(), epubActivityInterface.getFixScreenWidth());
                EpubViewerMainHandler.this.textView.setText(spannableString);
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

        private StringBuilder fileName;
        private TextView textView;
        private HTMLDocument htmlDocument;

        public EpubDTO(TextView textView) {
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

        @Override
        public boolean getBookmarkMode() {
            return false;
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
    }

    public String getHtmlContent() {
        return htmlContent.get();
    }

    public String getCustomContent() {
        return customContent.get();
    }
}
