package com.example.englishtester.common.html.parser;

import com.example.englishtester.common.Log;

import com.example.englishtester.BuildConfig;
import com.example.englishtester.Constant;
import com.example.englishtester.common.FileUtilAndroid;
import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.TagMatcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.util.StringUtil_;


public class HtmlWordParser extends HtmlBaseParser {

    public static final String WORD_HTML_ENCODE = "BIG5";
    public static final int HYPER_LINK_LABEL_MAX_LENGTH = 50;

    public static void main(String[] args) {
        HtmlWordParser parser = HtmlWordParser.newInstance();

        File file = new File("e:/gtu001_dropbox/Dropbox/Apps/gtu001_test/english_txt/A Life-Changing Exercise to Make You a Better Writer.htm");

        String result = parser.getFromFile(file, true, "");
        String dropboxDir = parser.picDirForDropbox;
        System.out.println("picDirForDropbox -- " + dropboxDir);
        System.out.println(result);

        System.out.println("done...");
    }

    private static final String TAG = HtmlEpubParser.class.getSimpleName();

    private HtmlWordParser() {
    }

    private String picDirForDropbox = "";

    public static HtmlWordParser newInstance() {
        return new HtmlWordParser();
    }

    public String getFromFile(File file) {
        return getFromFile(file, false, null);
    }

    public String getFromFile(File file, boolean isPure, String checkStr) {
        String content = FileUtilGtu.loadFromFile(file, WORD_HTML_ENCODE);
        return getFromContent(content, isPure, checkStr);
    }

    public String getFromFileDebug(File file, boolean isPure) {
        String content = FileUtilGtu.loadFromFile(file, WORD_HTML_ENCODE);
        return getFromContent(content, isPure, "");
    }

    public String getFromContent(String context) {
        return getFromContent(context, false, null);
    }

    public String getFromContentDebug(String content, boolean isPure) {
        return getFromContent(content, isPure, "");
    }

    public String getFromContent(String content, boolean isPure, String checkStr) {
        log("ORIGN start : =======================================================================");
//        log(content);
        log("ORIGN end   : =======================================================================");

        saveToFileDebug("before", content);

        try {
            content = getFromContentMain(content, isPure, checkStr);
        } catch (Throwable e) {
            throw new RuntimeException("getFromContent ERR : " + e.getMessage(), e);
        }

        log("RESULT start : =======================================================================");
//        logContent(content);
        log("RESULT end    : =======================================================================");
        return content;
    }

    @Override
    protected String _stepFinal_customPlus(String content, boolean isPure) {
        return content;
    }

    public String getPicDirForDropbox() {
        return picDirForDropbox;
    }
}
