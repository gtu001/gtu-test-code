package com.example.epub.com.example.epub.base;

/**
 * Created by wistronits on 2018/7/31.
 */

public class HTMLDocument {

    public void remove(int start, int end) {
        //TODO
    }

    public int getLength() {
        return -1;
    }

    public ParserCallback getReader(int pos) {
        return new DamnCoreParserCallback();
    }
}
