package com.example.englishtester.common.epub.base;

/**
 * Created by wistronits on 2018/7/31.
 */

public class MyHtmlEditorKit {

    private HTMLDocument htmlDocument;

    public MyHtmlEditorKit() {
    }

    public HTMLDocument createDefaultDocument() {
        this.htmlDocument = new HTMLDocument();
        return htmlDocument;
    }

    public Parser getParser() {
        return new MyParser(htmlDocument);
    }
}
