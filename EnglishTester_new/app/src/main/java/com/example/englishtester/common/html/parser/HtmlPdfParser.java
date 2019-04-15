package com.example.englishtester.common.html.parser;

import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.util.StringUtil_;


public class HtmlPdfParser {

    public static final String EPUB_HTML_ENCODE = "UTF8";
    public static final int HYPER_LINK_LABEL_MAX_LENGTH = 50;


    private static final String TAG = HtmlPdfParser.class.getSimpleName();

    private HtmlPdfParser() {
    }

    public static HtmlPdfParser newInstance() {
        return new HtmlPdfParser();
    }

    public String getFromFile(File file) {
        return getFromFile(file, false, null);
    }

    public String getFromFile(File file, boolean isPure, String checkStr) {
        String content = FileUtilGtu.loadFromFile(file, EPUB_HTML_ENCODE);
        return getFromContent(content, isPure, checkStr);
    }

    public String getFromFileDebug(File file, boolean isPure) {
        String content = FileUtilGtu.loadFromFile(file, EPUB_HTML_ENCODE);
        return getFromContent(content, isPure, "");
    }

    protected String getEncoding() {
        return EPUB_HTML_ENCODE;
    }

    public String getFromContent(String context) {
        return getFromContent(context, false, null);
    }

    public String getFromContentDebug(String content, boolean isPure) {
        return getFromContent(content, isPure, "");
    }

    public String getFromContent(String content, boolean isPure, String checkStr) {
        return content;
    }

}
