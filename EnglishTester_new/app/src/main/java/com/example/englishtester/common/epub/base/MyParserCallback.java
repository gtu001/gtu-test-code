package com.example.englishtester.common.epub.base;

public class MyParserCallback extends ParserCallback {

    private ParserCallback parserCallback;

    public MyParserCallback(ParserCallback reader) {
        super();
        this.parserCallback = reader;
    }

    public void flush() throws BadLocationException {
        parserCallback.flush();
    }

    public void handleText(char[] data, int pos) {
        parserCallback.handleText(data, pos);
    }

    public void handleComment(char[] data, int pos) {
        parserCallback.handleComment(data, pos);
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        parserCallback.handleStartTag(t, a, pos);
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        parserCallback.handleEndTag(t, pos);
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        parserCallback.handleSimpleTag(t, a, pos);
    }

    public void handleError(String errorMsg, int pos) {
        parserCallback.handleError(errorMsg, pos);
    }

    public void handleEndOfLineString(String eol) {
        parserCallback.handleEndOfLineString(eol);
    }
}