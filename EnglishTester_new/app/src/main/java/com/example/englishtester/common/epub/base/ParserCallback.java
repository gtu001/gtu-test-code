package com.example.englishtester.common.epub.base;

public class ParserCallback {
    /**
     * This is passed as an attribute in the attributeset to indicate
     * the element is implied eg, the string '&lt;&gt;foo&lt;\t&gt;'
     * contains an implied html element and an implied body element.
     *
     * @since 1.3
     */
    public static final Object IMPLIED = "_implied_";


    public void flush() throws BadLocationException {
    }

    public void handleText(char[] data, int pos) {
    }

    public void handleComment(char[] data, int pos) {
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
    }

    public void handleEndTag(HTML.Tag t, int pos) {
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
    }

    public void handleError(String errorMsg, int pos) {
    }

    /**
     * This is invoked after the stream has been parsed, but before
     * <code>flush</code>. <code>eol</code> will be one of \n, \r
     * or \r\n, which ever is encountered the most in parsing the
     * stream.
     *
     * @since 1.3
     */
    public void handleEndOfLineString(String eol) {
    }
}