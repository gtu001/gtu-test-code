package com.example.epub.com.example.epub.base;

/**
 * Created by wistronits on 2018/7/31.
 */

public class MyHtmlEditorKit {
    public HTMLDocument createDefaultDocument() {
        return new HTMLDocument();
    }

    public Parser getParser() {
        return new MyParser();
    }
}
