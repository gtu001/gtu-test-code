package com.example.epub.com.example.epub.base;

/**
 * Created by wistronits on 2018/7/31.
 */

public class MyHtmlEditorKit {
    public HTMLDocument createDefaultDocument() {
        return new HTMLDocument();
    }


    public Parser getParser() {
        return null;
    }

    public static class MyParserCallback {

        private ParserCallback parserCallback;

        public MyParserCallback(ParserCallback parserCallback) {
            this.parserCallback = parserCallback;
        }

        public void flush() {

        }
    }

}
