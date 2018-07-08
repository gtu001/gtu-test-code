package com.example.englishtester.common;

import android.util.Log;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordHtmlParser {

    private static final String TAG = WordHtmlParser.class.getSimpleName();

    private static final WordHtmlParser _INST = new WordHtmlParser();

    private WordHtmlParser() {
    }

    public static WordHtmlParser getInstance() {
        return _INST;
    }

    public String getFromFile(File file) {
        String content = FileUtilGtu.loadFromFile(file, "BIG5");
        content = _step0_headEmpty(content);
        content = _step1_replaceTo_title(content);
        content = _step2_nomalContent(content);
        content = _step2_hrefTag(content);
        content = _step3_imageProc(content);
        content = _step4_wordBlockCheck(content);
        content = _step5_li_check(content);

        content = _stepFinal_hidden_tag(content, "\\<body.*?\\>");
        content = _stepFinal_hidden_tag(content, "\\<div.*?\\>");
        content = _stepFinal_hidden_tag(content, "\\<ul.*?\\>");
        content = _stepFinal_hidden_tag(content, "\\<li.*?\\>");
        content = _stepFinal_hidden_tag(content, "\\<hr.*?\\>");
        content = _stepFinal_hidden_tag(content, "\\<u.*?\\>");
        content = _stepFinal_hidden_tag(content, "\\<b.*?\\>");
        content = _stepFinal_hidden_tag(content, "\\<html.*?\\>");
        content = _stepFinal_hidden_tag(content, "\\<\\/.*?\\>");

        return content;
    }

    private String _step1_replaceTo_title(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<b\\>\\<span.*?\\>(.*?)\\<\\/span\\>\\<\\/b\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String title = "{{title:" + mth.group(1) + "}}";
            title = contentFix(title);
            mth.appendReplacement(sb, title);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step2_nomalContent(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<span.*?\\>(.*?)\\<\\/span\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            AtomicReference<String> errMsg = new AtomicReference<>();
            try {
                String normalContent = mth.group(1);
                String normalContentNew = contentFix(normalContent);
                errMsg.set("處理前 : " + normalContent + " --> 處理後 : " + normalContentNew);
                mth.appendReplacement(sb, normalContentNew);
            } catch (Exception ex) {
                for (int ii = 0; ii < 10; ii++)
                    Log.e(TAG, "處理失敗 : " + ex.getMessage() + " ==== " + errMsg.get());
                mth.appendReplacement(sb, mth.group());
            }
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step2_hrefTag(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<a.*?href\\=\"(.*?)\">(.*?)\\<\\/a\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String link = mth.group(1);
            String linkLabel = mth.group(2);
            mth.appendReplacement(sb, "");
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step3_imageProc(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<img.*?alt\\=\"描述\\:\\s(.*?)\">", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String picLink = mth.group(1);
            mth.appendReplacement(sb, "{{img:" + picLink + "}}");
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _step4_wordBlockCheck(String content) {
        Pattern titleStylePtn = Pattern.compile("\\<p.*?class\\=MsoNormal\\>(.*?)\\<\\/p\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String tempContent = mth.group(1);
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
        Pattern titleStylePtn = Pattern.compile("\\<i\\>(.*?)\\<\\/i\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String liContent = mth.group(1);
            mth.appendReplacement(sb, liContent);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String _stepFinal_hidden_tag(String content, String patternStr) {
        Pattern titleStylePtn = Pattern.compile(patternStr, Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            mth.appendReplacement(sb, "");
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    private String contentFix(String content) {
        //escape &nbsp;
        Pattern ptn = Pattern.compile("\\&.{1,5}\\;");
        StringBuffer sb = new StringBuffer();
        Matcher mth = ptn.matcher(content);
        while (mth.find()) {
            mth.appendReplacement(sb, "");
        }
        mth.appendTail(sb);

        //escape for
        String rtnStr = appendReplacementEscape(sb.toString());
        return rtnStr;
    }

    private String appendReplacementEscape(String content){
        content = content.replaceAll("\\$", "\\\\$");
        content = content.replaceAll("\\/", "\\\\/");
        return content;
    }
}
