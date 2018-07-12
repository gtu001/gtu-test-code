package com.example.englishtester.common;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.util.StringUtil_;


public class WordHtmlParser {

    public static final String WORD_HTML_ENCODE = "BIG5";
    public static final int HYPER_LINK_LABEL_MAX_LENGTH = 50;

    public static void main(String[] args) {
        WordHtmlParser parser = WordHtmlParser.newInstance();

        File file = new File("D:/gtu001_dropbox/Dropbox/Apps/gtu001_test/english_txt/The revolution will wear a dashiki.htm");

        String result = parser.getFromFile(file, "1");
        String dropboxDir = parser.picDirForDropbox;
        System.out.println("picDirForDropbox -- " + dropboxDir);
        System.out.println(result);

        System.out.println("done...");
    }

    private static final String TAG = WordHtmlParser.class.getSimpleName();

    private WordHtmlParser() {
    }

    private String picDirForDropbox = "";

    public static WordHtmlParser newInstance() {
        return new WordHtmlParser();
    }

    private void validateContent(String stepLabel, String content, String checkStr) {
        if (StringUtils.isBlank(checkStr)) {
            return;
        }
        if (!StringUtils.trimToEmpty(content).contains(checkStr)) {
            throw new RuntimeException(stepLabel + " -> 查無 : " + checkStr);
        } else {
            Log.v(TAG, "CHECK : " + stepLabel + " OK!!!");
        }
    }

    public String getFromFile(File file) {
        return getFromFile(file, null);
    }

    public String getFromFile(File file, String checkStr) {
        String content = FileUtilGtu.loadFromFile(file, WORD_HTML_ENCODE);
        Log.v(TAG, "ORIGN : =======================================================================");
        Log.v(TAG, content);
        Log.v(TAG, "ORIGN : =======================================================================");

        content = _step0_headEmpty(content);
        validateContent("_step0_headEmpty", content, checkStr);
        content = _step1_replaceTo_title(content);
        validateContent("_step1_replaceTo_title", content, checkStr);
        content = _step2_nomalContent(content);
        validateContent("_step2_nomalContent", content, checkStr);
        content = _step2_hrefTag(content);
        validateContent("_step2_hrefTag", content, checkStr);
        content = _step3_imageProc(content, checkStr);
        validateContent("_step3_imageProc", content, checkStr);
        content = _step4_wordBlockCheck(content);
        validateContent("_step4_wordBlockCheck", content, checkStr);
        content = _step5_li_check(content);
        validateContent("_step5_li_check", content, checkStr);
        content = _step6_hiddenSomething(content, checkStr);
        validateContent("_step6_hiddenSomething", content, checkStr);
        content = _stepFinal_removeMultiChangeLine(content);
        validateContent("_stepFinal_removeMultiChangeLine", content, checkStr);

        // 最後做這塊才會正常
        content = org.springframework.web.util.HtmlUtils.htmlUnescape(content);

        return content;
    }

    private String _step1_replaceTo_title(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<b\\>\\<span(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/span\\>\\<\\/b\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String titleStr = mth.group(1);
            String title = "";
            if (StringUtils.isNotBlank(titleStr)) {
                title = "{{title:" + StringUtil_.appendReplacementEscape(titleStr) + "}}";
            }
            mth.appendReplacement(sb, title);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step2_nomalContent(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<span(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/span\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            AtomicReference<String> errMsg = new AtomicReference<String>();
            try {
                String normalContent = mth.group(1);
                String normalContentNew = StringUtil_.appendReplacementEscape(normalContent);
                errMsg.set("處理前 : " + normalContent + " --> 處理後 : " + normalContentNew);
                mth.appendReplacement(sb, normalContentNew);
            } catch (Exception ex) {
                for (int ii = 0; ii < 10; ii++)
                    Log.e(TAG, "處理失敗 : " + ex.getMessage() + " ==== " + errMsg.get());
                ex.printStackTrace();
                mth.appendReplacement(sb, mth.group());
            }
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step2_hrefTag(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<a\\s*?href\\=\"((?:.|\n)*?)\"(?:.|\n)*?>((?:.|\n)*?)\\<\\/a\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String link = mth.group(1);
            String linkLabel = StringUtils.trimToEmpty(mth.group(2));

            if (linkLabel.contains("<img")) {
                continue;
            }

            // linkLabel = appendReplacementEscape(linkLabel);
            linkLabel = _stepFinal_hidden_tag(linkLabel, "\\<(?:.|\n)*?\\>");
            String replaeStr = "";
            if (StringUtils.isNotBlank(link) && StringUtils.isNotBlank(linkLabel)) {
                replaeStr = "{{link:" + link + ",value:" + linkLabel + "}}";
            }
            mth.appendReplacement(sb, replaeStr);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _fixPicUrl(String orignLink, String srcDesc) {
        String url = StringUtils.trimToEmpty(orignLink);
        if (url.matches("https?\\:.*") || //
                url.matches("www\\..*") || //
                url.matches("\\w+\\.\\w+.*") //
                ) {
            return url;
        }

        // 取得dropbox 目錄名稱
        if (StringUtils.isBlank(picDirForDropbox)) {
            try {
                String tmpDir = URLDecoder.decode(srcDesc, WORD_HTML_ENCODE);
                if (tmpDir.contains("/")) {
                    tmpDir = tmpDir.substring(0, tmpDir.indexOf("/"));
                }
                picDirForDropbox = tmpDir;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return StringUtils.trimToEmpty(srcDesc);
    }

    private void filterImageDir(String srcDesc) {
        if (StringUtils.isNotBlank(picDirForDropbox)) {
            return;
        }
        try {
            String tmp = StringUtils.trimToEmpty(srcDesc);
            tmp = URLDecoder.decode(tmp, WORD_HTML_ENCODE);
            picDirForDropbox = tmp.substring(0, tmp.lastIndexOf("/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String _step3_imageProc(String content, String checkPic) {
        Pattern titleStylePtn = Pattern.compile("\\<img(?:.|\n)*?src\\=\"((?:.|\n)*?)\"(?:.|\n)*?alt\\=\"描述\\:\\s((?:.|\n)*?)\"(?:.|\n)*?>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String srcDesc = mth.group(1);
            String picLink = mth.group(2);

            if (StringUtils.isNotBlank(checkPic) && picLink.contains(checkPic)) {
                Log.v(TAG, "!!!!!!  <<<_step3_imageProc>>> Find Pic : " + checkPic);
            }

            filterImageDir(srcDesc);

            String repStr = "{{img src:" + srcDesc + ",alt:" + picLink + "}}";
            mth.appendReplacement(sb, repStr);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step4_wordBlockCheck(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<p\\s*?class\\=MsoNormal(?:.|\n)*?\\>((?:.|\n)*?)(?:\\<\\/p\\>)", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String tempContent = mth.group(1);
            tempContent = StringUtil_.appendReplacementEscape(tempContent);
            mth.appendReplacement(sb, tempContent);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step0_headEmpty(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<head\\>.*\\<\\/head\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            mth.appendReplacement(sb, "");
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step5_li_check(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<i\\>((?:.|\n)*?)\\<\\/i\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String liContent = mth.group(1);
            mth.appendReplacement(sb, liContent);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step6_hiddenSomething(String content, String checkStr) {
        content = _stepFinal_hidden_tag(content, "\\<body(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 1", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<v\\:imagedata(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 2", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<body(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 3", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<div(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 4", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<ul(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 5", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<li(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 6", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<hr(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 7", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<u(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 8", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<b(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 9", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<html(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 10", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<span(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 11", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<\\/(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 12", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<o\\:p>");
        validateContent("_stepFinal_hidden_tag 13", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<\\!\\[(?:.|\n)*?\\]\\>");
        validateContent("_stepFinal_hidden_tag 14", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<\\!\\-\\-\\[(?:.|\n)*?\\]\\>");
        validateContent("_stepFinal_hidden_tag 15", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<v\\:shape.*?\\>");
        validateContent("_stepFinal_hidden_tag 16", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<table(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 17", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<td(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 18", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<tr(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 19", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<o\\:(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 20", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<v\\:(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 21", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<main(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 22", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<em(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 23", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<canvas(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 24", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<time(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 25", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<h\\d(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 26", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<figure(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 27", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<svg(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 28", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<w\\:(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 29", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<p\\sclass\\=MsoNormal(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 30", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<p\\sclass\\=graf(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 30", content, checkStr);

        content = _stepFinal_hidden_tag(content, "\\<a(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag (for link contain IMG)", content, checkStr);
        return content;
    }

    private String _stepFinal_hidden_tag(String content, String patternStr) {
        Pattern titleStylePtn = Pattern.compile(patternStr, Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            mth.appendReplacement(sb, "");
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _stepFinal_removeMultiChangeLine(String content) {
        Pattern ptn = Pattern.compile("\n[\r\\s]*\n[\r\\s]*\n");
        for (;;) {
            boolean findOk = false;
            StringBuffer sb = new StringBuffer();
            Matcher mth = ptn.matcher(content);
            while (mth.find()) {
                findOk = true;
                mth.appendReplacement(sb, "\n\n");
            }
            mth.appendTail(sb);
            if (findOk) {
                content = sb.toString();
            } else {
                break;
            }
        }
        return content;
    }

    public String getPicDirForDropbox() {
        return picDirForDropbox;
    }
}
