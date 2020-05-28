package com.example.englishtester.common.html.parser;

import com.example.englishtester.BuildConfig;
import com.example.englishtester.Constant;
import com.example.englishtester.common.FileUtilAndroid;
import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.TagMatcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.util.StringUtil_;

/**
 * Created by gtu001 on 2018/8/11.
 */

public abstract class HtmlBaseParser {

    public static void main(String[] args) {
        HtmlBaseParser t = new HtmlBaseParser() {
            protected String _step3_imageProc_custom(String content, boolean isPure, String checkStr) {
                return content;
            }

            protected String _stepFinal_customPlus(String content, boolean isPure, String chkStr) {
                return content;
            }
        };
        File file = new File("");
        String content = t.getFromFile(file, false, "");
        System.out.println("start -------------------------------------------------");
        System.out.println(content);
        System.out.println("end   -------------------------------------------------");
    }


    public static final String WORD_HTML_ENCODE = "BIG5";
    public static final int HYPER_LINK_LABEL_MAX_LENGTH = 50;
    protected static final String NEW_LINE = "\r\n\r\n";
    protected static final String NEW_LINE_Single = "\r\n";

    private static final String TAG = HtmlBaseParser.class.getSimpleName();

    protected HtmlBaseParser() {
    }

    protected String picDirForDropbox = "";

    protected String getFromFile(File file) {
        return getFromFile(file, false, null);
    }

    protected String getFromFile(File file, boolean isPure, String checkStr) {
        String content = FileUtilGtu.loadFromFile(file, getEncoding());
        return getFromContent(content, isPure, checkStr);
    }

    protected String getFromFileDebug(File file, boolean isPure) {
        String content = FileUtilGtu.loadFromFile(file, getEncoding());
        return getFromContent(content, isPure, "");
    }

    protected String getFromContent(String context) {
        return getFromContent(context, false, null);
    }

    protected String getFromContentDebug(String content, boolean isPure) {
        return getFromContent(content, isPure, "");
    }

    protected String getFromContent(String content, boolean isPure, String checkStr) {
        long startTime = System.currentTimeMillis();
        Log.v(TAG, "ORIGN start : =======================================================================");
//        log(content);
        Log.v(TAG, "total len : " + StringUtils.length(content));
        Log.v(TAG, "ORIGN end   : =======================================================================");

//        saveToFileDebug("before", content);

        try {
            content = getFromContentMain(content, isPure, checkStr);
        } catch (Throwable e) {
            throw new RuntimeException("getFromContent ERR : " + e.getMessage(), e);
        }

        Log.v(TAG, "RESULT start : =======================================================================");
//        logContent(content);
        Log.v(TAG, "total len : " + StringUtils.length(content));
        Log.v(TAG, "RESULT end    : =======================================================================");
        long duringTime = System.currentTimeMillis() - startTime;
        Log.v(TAG, "duringTime : " + duringTime);
        return content;
    }

    protected void saveToFileDebug(String suffix, String context) {
        if (BuildConfig.DEBUG) {
            String dateStr = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
            String name = HtmlBaseParser.class.getSimpleName();
            if (StringUtils.isNotBlank(suffix)) {
                name = name + "_" + suffix;
            }
            File file = new File(Constant.PropertiesFindActivity_PATH + File.separator + name + "_" + dateStr + ".txt");
            try {
                FileUtilAndroid.saveToFile(file, context);
            } catch (Exception e) {
                Log.e(TAG, "saveToFileDebug ERR : " + e.getMessage(), e);
            }
        }
    }


    protected String getFromContentMain(String content, boolean isPure, String checkStr) {
        Log.v(TAG, "# getFromContentMain START...");

        content = _step0_CustomPicDirForDropbox(content, isPure);
        validateContent("_step0_CustomPicDirForDropbox", content, checkStr);


        content = _step0_hiddenHead(content, isPure);
        validateContent("_step0_hiddenHead", content, checkStr);
        content = _step1_hTitleHandler(content, isPure);
        validateContent("_step1_hTitleHandler", content, checkStr);
        content = _step1_replaceTo_Bold(content, isPure);
        validateContent("_step1_replaceTo_Bold", content, checkStr);
        content = _step1_replaceTo_NormalPTag(content, isPure);
        validateContent("_step1_replaceTo_NormalPTag", content, checkStr);
        content = _step1_replaceTo_Italic(content, isPure);
        validateContent("_step1_replaceTo_Italic", content, checkStr);
        content = _step1_replaceStrongTag(content, isPure);
        validateContent("_step1_replaceStrongTag", content, checkStr);
        content = _step1_fontSize_Indicate(content, isPure);
        validateContent("_step1_fontSize_Indicate", content, checkStr);
        content = _step2_normalContent(content, isPure);
        validateContent("_step2_nomalContent", content, checkStr);
        content = _step2_replaceToNewLine(content, isPure);
        validateContent("_step2_replaceToNewLine_byPattern", content, checkStr);
        content = _step2_hrefTag(content, isPure);
        validateContent("_step2_hrefTag", content, checkStr);
        content = _step2$1_hrefTag(content, isPure);
        validateContent("_step2$1_hrefTag", content, checkStr);
        content = _step3_imageProcMaster(content, isPure, checkStr);
        validateContent("_step3_imageProcMaster", content, checkStr);
        content = _step3_imageProc_custom(content, isPure, checkStr);
        validateContent("_step3_imageProc_custom", content, checkStr);
        content = _step4_wordBlockCheck(content, isPure);
        validateContent("_step4_wordBlockCheck", content, checkStr);
        content = _step4_fontSize_Indicate_4PTag(content, isPure);
        validateContent("_step4_fontSize_Indicate_4PTag", content, checkStr);
        content = _step5_olAndLi(content, isPure);
        validateContent("_step5_olAndLi", content, checkStr);
        content = _step5_li_check(content, isPure);
        validateContent("_step5_li_check", content, checkStr);
        content = _step6_preTag(content, isPure);
        validateContent("_step6_preTag", content, checkStr);
        content = _step7_codeTag(content, isPure);
        validateContent("_step7_codeTag", content, checkStr);
        content = _step8_endNote(content, isPure);
        validateContent("_step8_endNote", content, checkStr);
        content = _step9_pagebreak(content, isPure);
        validateContent("_step9_pagebreak", content, checkStr);

        content = _stepFinal_customPlus(content, isPure, checkStr);
        validateContent("_stepFinal_customPlus", content, checkStr);

        content = _step999_hiddenSomething(content, isPure, checkStr);
        validateContent("_step999_hiddenSomething", content, checkStr);

        content = _stepFinal_escapeTag(content, isPure);
        validateContent("_stepFinal_removeMultiChangeLine", content, checkStr);

        content = _stepFinal_removeMultiChangeLine(content, isPure);
        validateContent("_stepFinal_removeMultiChangeLine", content, checkStr);

        // 最後做這塊才會正常
        content = org.springframework.web.util.HtmlUtils.htmlUnescape(content);

        Log.v(TAG, "# getFromContentMain END...");
        return content;
    }

    private String _step2_replaceToNewLine(String content, boolean isPure) {
        content = _step2_replaceToNewLine_byPattern(content, "\\<title\\s+class\\=(?:.|\n)*?\\>", false, isPure);
        content = _step2_replaceToNewLine_byPattern(content, "\\<br\\/\\>", true, isPure);
        return content;
    }

    protected abstract String _step3_imageProc_custom(String content, boolean isPure, String checkStr);

    protected abstract String _stepFinal_customPlus(String content, boolean isPure, String checkStr);

    protected void logContent(String content) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(content));
            for (String line = null; (line = reader.readLine()) != null; ) {
                Log.v(TAG, line);
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

    protected String _step0_CustomPicDirForDropbox(String content, boolean isPure) {
//        <!-- picBaseUrl:https://docs.docker.com -->
        Pattern ptn = Pattern.compile("\\<\\!\\-\\-\\s*picBaseUrl\\:(.*?)\\s*\\-\\-\\>", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(content);
        if (mth.find()) {
            picDirForDropbox = mth.group(1);
        }
        return content;
    }

    protected String _step1_replaceStrongTag(String content, boolean isPure) {
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

    protected enum FontSizeIndicateEnum {
        SPAN001("\\<span(?:.|\n)*?font\\-size\\:([\\d\\.]+)pt\\;(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/span\\>"),//
        P001("\\<p(?:.|\\n)*?font\\-size\\:\\s*?([\\d\\.]+)(?:px|pt)(?:.|\\n)*?\\>(.*?)\\<\\/p\\>"),//
        ;

        final Pattern ptn;

        FontSizeIndicateEnum(String ptnStr) {
            ptn = Pattern.compile(ptnStr, Pattern.DOTALL | Pattern.MULTILINE);
        }

        String apply(String content, boolean isPure) {
            String beforeVal = "";
            String tmpVal = "";
            try {
                StringBuffer sb = new StringBuffer();
                Matcher mth = ptn.matcher(content);
                while (mth.find()) {
                    String size = mth.group(1);
                    beforeVal = mth.group(2);
                    tmpVal = "";
                    if (StringUtils.isNotBlank(size) && StringUtils.isNotBlank(beforeVal)) {
                        tmpVal = "{{font size:" + size + ",text:" + StringUtil_.appendReplacementEscape(beforeVal) + "}}" + "";//NEW_LINE
                        if (isPure) {
                            tmpVal = StringUtil_.appendReplacementEscape(beforeVal);
                        }
                    }
                    mth.appendReplacement(sb, tmpVal);
                }
                mth.appendTail(sb);
                return sb.toString();
            } catch (Exception ex) {
                throw new RuntimeException("FontSizeIndicateEnum Failed : " + beforeVal + " <-> " + tmpVal + " , ERR : " + ex.getMessage(), ex);
            }
        }
    }

    protected String _getSelectionString(String orignText, Callable<String> task) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<String> future = executor.submit(task);
        try {
            return future.get(10000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return orignText;
        }
    }

    protected String _step1_fontSize_Indicate(String content, final boolean isPure) {
        Matcher mth = null;
        try {
            Pattern ptn = Pattern.compile("\\<span(?:.|\n)*?\\<\\/span\\>", Pattern.MULTILINE | Pattern.DOTALL);
            mth = ptn.matcher(content);
            final StringBuffer sb = new StringBuffer();
            while (mth.find()) {
                final String spanContent = mth.group();
                String spanContent1 = _getSelectionString(spanContent, new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String spanContent1 = spanContent;
                        spanContent1 = HtmlBaseParser.FontSizeIndicateEnum.SPAN001.apply(spanContent1, isPure);
                        spanContent1 = StringUtil_.appendReplacementEscape(spanContent1);
                        return spanContent1;
                    }
                });
                mth.appendReplacement(sb, spanContent1);
            }
            mth.appendTail(sb);
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("_step1_fontSize_Indicate Group : " + mth.group() + " , ERR : " + ex.getMessage(), ex);
        }
    }

    protected String _step4_fontSize_Indicate_4PTag(String content, boolean isPure) {
        Pattern ptn = Pattern.compile("\\<p(?:.|\n)*?\\<\\/p\\>", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            String spanContent = mth.group();
            spanContent = HtmlBaseParser.FontSizeIndicateEnum.P001.apply(spanContent, isPure);
            spanContent = StringUtil_.appendReplacementEscape(spanContent);
            mth.appendReplacement(sb, spanContent);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step0_hiddenHead(String content, boolean isPure) {
        return hiddenByTagMatcher("<head", "</head>", "\\<head[\r\n\\s\t\\>]{1}", "\\<\\/head\\>", 0, 0, content);
    }

    protected String _step1_hTitleHandler(String content, boolean isPure) {
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

    protected String _step5_olAndLi(String content, boolean isPure) {
        StringBuffer tempSb = new StringBuffer();
        Pattern titleStylePtn = Pattern.compile("\\<(ol|ul).*?\\>((?:.|\n)*?)\\<\\/(?:ol|ul)\\>", Pattern.DOTALL | Pattern.MULTILINE);
        Pattern titleDtlStylePtn = Pattern.compile("\\<li.*?\\>((?:.|\n)*?)\\<\\/li\\>", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String detailType = mth.group(1);
            String detailContent = mth.group(2);
            // ------------------------------------
            int index = 1;
            StringBuffer dtlSb = new StringBuffer();
            Matcher mth2 = titleDtlStylePtn.matcher(detailContent.toString());
            while (mth2.find()) {
                String mth2_li_string = mth2.group(1);
                mth2_li_string = StringUtil_.appendReplacementEscape(mth2_li_string);
                String indexFix = isPure ? "" + index : String.format("{{b:%d.}}", index);
                if ("ul".equals(detailType)) {
                    indexFix = "● ";
                }
                mth2.appendReplacement(dtlSb, indexFix + mth2_li_string);
                index++;
            }
            mth2.appendTail(dtlSb);
            // ------------------------------------
            mth.appendReplacement(tempSb, StringUtil_.appendReplacementEscape(dtlSb.toString()));
        }
        mth.appendTail(tempSb);
        return tempSb.toString();
    }

    protected String _step1_replaceTo_NormalPTag(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<p\\>((?:.|\n)*?)<\\/p\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String titleStr = mth.group(1);
            String title = "";
            if (StringUtils.isNotBlank(titleStr)) {
                title = StringUtil_.appendReplacementEscape(titleStr) + "";//NEW_LINE
                if (isPure) {
                    title = StringUtil_.appendReplacementEscape(titleStr);
                }
            }
            mth.appendReplacement(sb, title);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step1_replaceTo_Bold(String content, boolean isPure) {
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

    protected String _step1_replaceTo_Italic(String content, boolean isPure) {
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

    protected String _step2_replaceToNewLine_byPattern(String content, String pattern, boolean isSingleNewLine, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        String newLine = NEW_LINE;
        if (isSingleNewLine) {
            newLine = NEW_LINE_Single;
        }
        while (mth.find()) {
            AtomicReference<String> errMsg = new AtomicReference<String>();
            try {
                mth.appendReplacement(sb, newLine);
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

    protected String _step2_normalContent(String content, boolean isPure) {
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

    protected String _step2_hrefTag(String content, boolean isPure) {
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

    protected String _step2$1_hrefTag(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<a\\s(?:.|\n)*?href\\=\"((?:.|\n)*?)\"(?:.|\n)*?>((?:.|\n)*?)\\<\\/a\\>", Pattern.DOTALL | Pattern.MULTILINE);
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

    protected String _fixPicUrl(String orignLink, String srcDesc) {
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
                String tmpDir = URLDecoder.decode(srcDesc, getEncoding());
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

    protected void filterImageDir(String srcDesc) {
        if (StringUtils.isNotBlank(picDirForDropbox)) {
            return;
        }
        try {
            String tmp = StringUtils.trimToEmpty(srcDesc);
            tmp = URLDecoder.decode(tmp, getEncoding());
            if (!tmp.contains("/")) {
                return;
            }
            tmp = tmp.substring(0, tmp.lastIndexOf("/"));
            tmp = org.springframework.web.util.HtmlUtils.htmlUnescape(tmp);
            picDirForDropbox = tmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getEncoding() {
        return WORD_HTML_ENCODE;
    }

    protected String _step3_imageProcMaster(String content, boolean isPure, String checkPic) {
        Pattern ptn = Pattern.compile("\\<img(?:.|\n)*?\\>", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            String spanContent = mth.group();
            spanContent = _step3_imageProc_WordMode(spanContent, isPure, checkPic);
            spanContent = _step3_imageProc_WordMode2(spanContent, isPure, checkPic);
            spanContent = _step3_imageProc_InternetMode(spanContent, isPure, checkPic);
            spanContent = StringUtil_.appendReplacementEscape(spanContent);
            mth.appendReplacement(sb, spanContent);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step6_preTag(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<pre(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/pre\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String preText = mth.group(1);

            String processText = StringUtil_.appendReplacementEscape(preText);
            processText = _specialCodeChangeLine(processText);

            String repStr = "{{pre:" + processText + "}}";
            if (isPure) {
                repStr = preText;
            }

            mth.appendReplacement(sb, repStr);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step7_codeTag(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<code(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/code\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String preText = mth.group(1);

            String processText = StringUtil_.appendReplacementEscape(preText);
            processText = _specialCodeChangeLine(processText);

            String repStr = "{{code:" + processText + "}}";
            if (isPure) {
                repStr = preText;
            }

            mth.appendReplacement(sb, repStr);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step8_endNote(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<sup\\s*?class\\=\"endnote\"(?:.|\n)*?>((?:.|\n)*?)\\<\\/sup\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String link = mth.group(1);
            link = _stepFinal_hidden_tag(link, "\\<(?:.|\n)*?\\>");
            mth.appendReplacement(sb, link);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _specialCodeChangeLine(String context) {
        String tag = "{{chgLine}}";
        Pattern ptn = Pattern.compile("(\\Q" + tag + "\\E\n|\n)", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(context);
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            String tmp = mth.group();
            if (tmp.contains(tag)) {
                mth.appendReplacement(sb, tmp);
            } else {
                mth.appendReplacement(sb, tag + "\n");
            }
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step3_imageProc_WordMode(String content, boolean isPure, String checkPic) {
        Pattern titleStylePtn = Pattern.compile("\\<img(?:.|\n)*?src\\=\"((?:.|\n)*?)\"(?:.|\n)*?alt\\=\"描述\\:\\s((?:.|\n)*?)\"(?:.|\n)*?>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String srcDesc = mth.group(1);
            String picLink = mth.group(2);

            if (StringUtils.isNotBlank(checkPic) && picLink.contains(checkPic)) {
                Log.v(TAG, "!!!!!!  <<<_step3_imageProc_WordMode>>> Find Pic : " + checkPic);
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

    protected String _step3_imageProc_WordMode2(String content, boolean isPure, String checkPic) {
        Pattern titleStylePtn = Pattern.compile("\\<img(?:.|\n)*?src\\=\"((?:.|\n)*?)\"(?:.|\n)*?alt\\=\"((?:.|\n)*?)\"(?:.|\n)*?>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String srcDesc = mth.group(1);
            String picLink = mth.group(2);

            if (StringUtils.isNotBlank(checkPic) && picLink.contains(checkPic)) {
                Log.v(TAG, "!!!!!!  <<<_step3_imageProc_WordMode>>> Find Pic : " + checkPic);
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

    protected String _step3_imageProc_InternetMode(String content, boolean isPure, String checkPic) {
        Pattern titleStylePtn = Pattern.compile("\\<img(?:.|\n)*?src\\=\"((?:.|\n)*?)\"(?:.|\n)*?\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String srcDesc = mth.group(1);

            if (StringUtils.isNotBlank(checkPic) && srcDesc.contains(checkPic)) {
                Log.v(TAG, "!!!!!!  <<<_step3_imageProc_InternetMode>>> Find Pic : " + checkPic);
            }

            String repStr = "{{img src:" + "" + ",alt:" + srcDesc + "}}";
            if (isPure) {
                repStr = "";
            }

            mth.appendReplacement(sb, repStr);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step4_wordBlockCheck(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<p\\s*?class\\=\"?MsoNormal(?:.|\n)*?\\>((?:.|\n)*?)(?:\\<\\/p\\>)", Pattern.DOTALL | Pattern.MULTILINE);
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

    protected String _step5_li_check(String content, boolean isPure) {
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

    protected String hiddenByTagMatcher(String startTag, String endTag, String startPtn, String endPtn, int startTagOffset, int endTagOffset, String content) {
        TagMatcher tag = new TagMatcher(startTag, endTag, startPtn, endPtn, startTagOffset, endTagOffset, content);
        while (tag.findUnique()) {
            tag.appendReplacementForUnique("", true, true, true);
        }
        String rtnVal = tag.getContent();
        System.gc();
        return rtnVal;
    }

    protected String _step999_hiddenSomething(String content, boolean isPure, String checkStr) {
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
        content = _stepFinal_hidden_tag(content, "\\<meta\\s(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag 37", content, checkStr);

        content = _stepFinal_hidden_tag(content, "\\<a(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag (for link contain IMG)", content, checkStr);

        //for Standard Html
        content = hiddenByTagMatcher("<script", "</script>", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 1", content, checkStr);
        content = hiddenByTagMatcher("<rect", ">", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 2", content, checkStr);
        content = hiddenByTagMatcher("<polyline", ">", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 3", content, checkStr);
        content = hiddenByTagMatcher("<select", "</select>", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 4", content, checkStr);
        content = hiddenByTagMatcher("<circle", ">", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 5", content, checkStr);
        content = hiddenByTagMatcher("<input", ">", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 6", content, checkStr);
        content = hiddenByTagMatcher("<nav", ">", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 7", content, checkStr);
        content = hiddenByTagMatcher("<!--", "-->", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 8", content, checkStr);
        content = hiddenByTagMatcher("<iframe", ">", "", "", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 9", content, checkStr);
        content = hiddenByTagMatcher("<ins", ">", "<ins[\\s\\>]", "\\>", 0, 0, content);
        validateContent("_stepFinal_hidden_tag STD 1", content, checkStr);

        //移除結尾tag
        content = _stepFinal_hidden_tag(content, "\\<\\/(?:.|\n)*?\\>");
        validateContent("_stepFinal_hidden_tag <END_TAG>", content, checkStr);

        return content;
    }

    protected String _stepFinal_hidden_tag(String content, String patternStr) {
        Pattern titleStylePtn = Pattern.compile(patternStr, Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            mth.appendReplacement(sb, "");
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _stepFinal_removeMultiChangeLine(String content, boolean isPure) {
        Pattern ptn = Pattern.compile("\n[\r\\s\t]*\n[\r\\s\t]*\n");
        for (; ; ) {
            boolean findOk = false;
            StringBuffer sb = new StringBuffer();
            Matcher mth = ptn.matcher(content);
            while (mth.find()) {
                findOk = true;
                mth.appendReplacement(sb, NEW_LINE);
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

    protected String _step9_pagebreak(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile(Pattern.quote("<div class=\"mbppagebreak\"></div>"), Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String pagebreak = mth.group();
            String tmpVal = "";
            if (StringUtils.isNotBlank(pagebreak)) {
                tmpVal = "{{pagebreak}}" + NEW_LINE;
                if (isPure) {
                    tmpVal = NEW_LINE;
                }
            }
            mth.appendReplacement(sb, tmpVal);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String getPicDirForDropbox() {
        return picDirForDropbox;
    }


    protected void validateContent(String stepLabel, String content, String checkStr) {
        if (StringUtils.length(content) == 0) {
            throw new RuntimeException(stepLabel + " -> 長度為0");
        }
        if (checkStr == null) {
            return;
        }
        if (checkStr != null && checkStr.length() == 0) {
            Log.v(TAG, "PROC : " + stepLabel + " Done !!!");
            return;
        }
        if (!StringUtils.trimToEmpty(content).contains(checkStr)) {
            throw new RuntimeException(stepLabel + " -> 查無 : " + checkStr);
        } else {
            Log.v(TAG, "CHECK : " + stepLabel + " OK!!!");
        }
    }

    protected void validateLog(String stepLabel) {
        Log.v(TAG, "CHECK : " + stepLabel + " OK!!!");
    }

    protected String _stepFinal_escapeTag(String content, boolean isPure) {
        HtmlBaseParser.CustomTagEscaper escaper = new HtmlBaseParser.CustomTagEscaper(content);
        return escaper.getResult();
    }

    protected class CustomTagEscaper {
        String orignContent;
        String resultContent;

        Pattern startPtn = Pattern.compile("\\{{3,}", Pattern.DOTALL | Pattern.MULTILINE);
        Pattern endPtn = Pattern.compile("\\}{3,}", Pattern.DOTALL | Pattern.MULTILINE);

        protected CustomTagEscaper(String orignContent) {
            this.orignContent = orignContent;
            resultContent = orignContent.toString();
            resultContent = getDivideTag(resultContent, startPtn, true);
            resultContent = getDivideTag(resultContent, endPtn, false);
        }

        private String getStartTag(int length, boolean isStartTag) {
            LinkedList<String> lst = new LinkedList<String>();
            char tag = isStartTag ? '{' : '}';
            for (int ii = 0; ii < (length / 2); ii++) {
                lst.add("" + tag + tag);
            }
            if (length % 2 != 0) {
                if (isStartTag) {
                    lst.push("" + tag);
                } else {
                    lst.add("" + tag);
                }
            }
            return StringUtils.join(lst, " ");
        }

        private String getDivideTag(String content, Pattern ptn, boolean isStart) {
            StringBuffer sb = new StringBuffer();
            Matcher mth = ptn.matcher(content);
            while (mth.find()) {
                String tag = mth.group();
                String repStr = getStartTag(tag.length(), isStart);
                mth.appendReplacement(sb, repStr);
            }
            mth.appendTail(sb);
            return sb.toString();
        }

        public String getResult() {
            return resultContent;
        }
    }
}
