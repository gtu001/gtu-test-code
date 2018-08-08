package com.example.epub.com.example.epub.base;

/**
 * Created by wistronits on 2018/7/31.
 */

public class MyHtmlEditorKit {

    private HTMLDocument htmlDocument;

    public MyHtmlEditorKit(){
        htmlDocument = new HTMLDocument();
    }

    public HTMLDocument createDefaultDocument() {
        return htmlDocument;
    }

    public Parser getParser() {
        return new MyParser(htmlDocument);
    }
}
