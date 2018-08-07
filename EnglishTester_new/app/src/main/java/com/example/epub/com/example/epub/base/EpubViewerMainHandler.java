package com.example.epub.com.example.epub.base;

import nl.siegmann.epublib.browsersupport.Navigator;

/**
 * Created by wistronits on 2018/8/7.
 */

public class EpubViewerMainHandler {

    private Navigator navigator = new Navigator();
    private HTMLDocumentFactory htmlDocumentFactory;
    private MyHtmlEditorKit myHtmlEditorKit;

    public EpubViewerMainHandler() {
        myHtmlEditorKit = new MyHtmlEditorKit();
        htmlDocumentFactory = new HTMLDocumentFactory(navigator, myHtmlEditorKit);
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public HTMLDocumentFactory getHtmlDocumentFactory() {
        return htmlDocumentFactory;
    }
}
