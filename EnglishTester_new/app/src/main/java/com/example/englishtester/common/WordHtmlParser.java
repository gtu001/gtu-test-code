package com.example.englishtester.common;

import android.util.Log;
import android.widget.Toast;

import com.example.englishtester.BuildConfig;
import com.example.englishtester.Constant;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
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

        File file = new File("D:/gtu001_dropbox/Dropbox/Apps/gtu001_test/english_txt/The Stress of Remote Working.htm");

        String result = parser.getFromFile(file, true, "");
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
            log("CHECK : " + stepLabel + " OK!!!");
        }
    }

    public String getFromFile(File file) {
        return getFromFile(file, false, null);
    }

    public String getFromFile(File file, boolean isPure, String checkStr) {
        String content = FileUtilGtu.loadFromFile(file, WORD_HTML_ENCODE);
        return getFromContent(content, isPure, checkStr);
    }

    public String getFromContent(String context) {
        return getFromContent(context, false, null);
    }

    public String getFromContent(String content, boolean isPure, String checkStr) {
//        log("ORIGN : =======================================================================");
//        log(content);
//        log("ORIGN : =======================================================================");

        content = _step0_hiddenHead(content, isPure);
        validateContent("_step0_hiddenHead", content, checkStr);
        content = _step1_hTitleHandler(content, isPure);
        validateContent("_step1_hTitleHandler", content, checkStr);
        content = _step1_replaceTo_Bold(content, isPure);
        validateContent("_step1_replaceTo_Bold", content, checkStr);
        content = _step1_replaceTo_Italic(content, isPure);
        validateContent("_step1_replaceTo_Italic", content, checkStr);
        content = _step1_replaceStrongTag(content, isPure);
        validateContent("_step1_replaceStrongTag", content, checkStr);
        content = _step1_fontSize_Indicate(content, isPure);
        validateContent("_step1_fontSize_Indicate", content, checkStr);
        content = _step2_normalContent(content, isPure);
        validateContent("_step2_nomalContent", content, checkStr);
        content = _step2_hrefTag(content, isPure);
        validateContent("_step2_hrefTag", content, checkStr);
        content = _step3_imageProc(content, isPure, checkStr);
        validateContent("_step3_imageProc", content, checkStr);
        content = _step4_wordBlockCheck(content, isPure);
        validateContent("_step4_wordBlockCheck", content, checkStr);
        content = _step5_li_check(content, isPure);
        validateContent("_step5_li_check", content, checkStr);
        content = _step6_hiddenSomething(content, isPure, checkStr);
        validateContent("_step6_hiddenSomething", content, checkStr);
        content = _stepFinal_removeMultiChangeLine(content, isPure);
        validateContent("_stepFinal_removeMultiChangeLine", content, checkStr);

        // 最後做這塊才會正常
        content = org.springframework.web.util.HtmlUtils.htmlUnescape(content);

//        log( "RESULT : =======================================================================");
//        logContent(content);
//        log( "RESULT : =======================================================================");

        return content;
    }

    private void logContent(String content) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(content));
            for (String line = null; (line = reader.readLine()) != null; ) {
                log(line);
            }
        } catch (Exception ex) {
            Log.e(TAG, "logContent ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                Log.e(TAG, "logContent ERR : " + e.getMessage(), e);
            }
        }
    }

    private String _step1_replaceStrongTag(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<strong(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/strong\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String boldStr = mth.group(1);
            String tmpVal = "";
            if (StringUtils.isNotBlank(boldStr)) {
                tmpVal = "{{strong:" + StringUtil_.appendReplacementEscape(boldStr) + "}}";
                if (isPure) {
                    tmpVal = StringUtil_.appendReplacementEscape(boldStr);
                }
            }
            mth.appendReplacement(sb, tmpVal);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private enum FontSizeIndicateEnum {
        PTN01("\\<span(?:.|\n)*?font\\-size\\:([\\d\\.]+)pt\\;(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/span\\>"),//
//        PTN01("\\<span(?:.|\n)*?style\\=\"font\\-size\\:([\\d\\.]+)pt\\;(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/span\\>"),//
//        PTN02("\\<span(?:.|\n)*?style\\=\"mso\\-bidi\\-font\\-size\\:([\\d\\.]+)pt\\;(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/span\\>"),//
        ;

        final Pattern ptn;

        FontSizeIndicateEnum(String ptnStr) {
            ptn = Pattern.compile(ptnStr, Pattern.DOTALL | Pattern.MULTILINE);
        }

        String apply(String content, boolean isPure) {
            StringBuffer sb = new StringBuffer();
            Matcher mth = ptn.matcher(content);
            while (mth.find()) {
                String size = mth.group(1);
                String text = mth.group(2);
                String tmpVal = "";
                log("....." + size + " , " + text);
                if (StringUtils.isNotBlank(size) && StringUtils.isNotBlank(text)) {
                    tmpVal = "{{font size:" + size + ",text:" + StringUtil_.appendReplacementEscape(text) + "}}";
                    if (isPure) {
                        tmpVal = StringUtil_.appendReplacementEscape(text);
                    }
                }
                mth.appendReplacement(sb, tmpVal);
            }
            mth.appendTail(sb);
            return sb.toString();
        }
    }

    private String _step1_fontSize_Indicate(String content, boolean isPure) {
        for (FontSizeIndicateEnum e : FontSizeIndicateEnum.values()) {
            content = e.apply(content, isPure);
        }
        return content;
    }

    private String _step0_hiddenHead(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<head\\>.*\\<\\/head\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            mth.appendReplacement(sb, "");
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step1_hTitleHandler(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<h(\\d+)(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/h\\d+\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String titleSize = mth.group(1);
            String titleText = mth.group(2);
            String title = "";
            if (StringUtils.isNotBlank(titleSize) && StringUtils.isNotBlank(titleText)) {
                title = "{{h size:" + titleSize + ",text:" + StringUtil_.appendReplacementEscape(titleText) + "}}";
                if (isPure) {
                    title = StringUtil_.appendReplacementEscape(titleText);
                }
            }
            mth.appendReplacement(sb, title);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step1_replaceTo_Bold(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<b\\>((?:.|\n)*?)<\\/b\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String titleStr = mth.group(1);
            String title = "";
            if (StringUtils.isNotBlank(titleStr)) {
                title = "{{b:" + StringUtil_.appendReplacementEscape(titleStr) + "}}";
                if (isPure) {
                    title = StringUtil_.appendReplacementEscape(titleStr);
                }
            }
            mth.appendReplacement(sb, title);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step1_replaceTo_Italic(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<i\\>((?:.|\n)*?)<\\/i\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String titleStr = mth.group(1);
            String title = "";
            if (StringUtils.isNotBlank(titleStr)) {
                title = "{{i:" + StringUtil_.appendReplacementEscape(titleStr) + "}}";
                if (isPure) {
                    title = StringUtil_.appendReplacementEscape(titleStr);
                }
            }
            mth.appendReplacement(sb, title);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step2_normalContent(String content, boolean isPure) {
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

    private String _step2_hrefTag(String content, boolean isPure) {
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
                replaeStr = "{{link:" + link + ",value:" + StringUtil_.appendReplacementEscape(linkLabel) + "}}";
                if (isPure) {
                    replaeStr = StringUtil_.appendReplacementEscape(linkLabel);
                }
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
            tmp = tmp.substring(0, tmp.lastIndexOf("/"));
            tmp = org.springframework.web.util.HtmlUtils.htmlUnescape(tmp);
            picDirForDropbox = tmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String _step3_imageProc(String content, boolean isPure, String checkPic) {
        Pattern titleStylePtn = Pattern.compile("\\<img(?:.|\n)*?src\\=\"((?:.|\n)*?)\"(?:.|\n)*?alt\\=\"描述\\:\\s((?:.|\n)*?)\"(?:.|\n)*?>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String srcDesc = mth.group(1);
            String picLink = mth.group(2);

            if (StringUtils.isNotBlank(checkPic) && picLink.contains(checkPic)) {
                log("!!!!!!  <<<_step3_imageProc>>> Find Pic : " + checkPic);
            }

            filterImageDir(srcDesc);

            String repStr = "{{img src:" + srcDesc + ",alt:" + picLink + "}}";
            if (isPure) {
                repStr = "";
            }

            mth.appendReplacement(sb, repStr);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step4_wordBlockCheck(String content, boolean isPure) {
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

    private String _step5_li_check(String content, boolean isPure) {
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

    private String _step6_hiddenSomething(String content, boolean isPure, String checkStr) {
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
        content = _stepFinal_hidden_tag(content, "\\<p\\sclass\\=(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 30", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<section(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 32", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<header\\s(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 33", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<path\\s(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 34", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<g\\s(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 35", content, checkStr);
        content = _stepFinal_hidden_tag(content, "\\<figcaption\\s(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 36", content, checkStr);

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

    private String _stepFinal_removeMultiChangeLine(String content, boolean isPure) {
        Pattern ptn = Pattern.compile("\n[\r\\s\t]*\n[\r\\s\t]*\n");
        for (; ; ) {
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

    private static void log(Object message) {
        System.out.println(message);
        try {
            Log.v(TAG, "" + message);
        } catch (Exception ex) {
        }
    }
}
