package com.example.englishtester.common.html.parser;

import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.util.StringUtil_;


public class HtmlMobiParser extends HtmlBaseParser {

    public static final String EPUB_HTML_ENCODE = "UTF8";
    public static final int HYPER_LINK_LABEL_MAX_LENGTH = 50;

    public static void main(String[] args) {
        HtmlMobiParser parser = HtmlMobiParser.newInstance();

        File file = new File("e:/gtu001_dropbox/Dropbox/Apps/gtu001_test/english_txt/A Life-Changing Exercise to Make You a Better Writer.htm");

        String result = parser.getFromFile(file, true, "");
        String dropboxDir = parser.picDirForDropbox;
        System.out.println("picDirForDropbox -- " + dropboxDir);
        System.out.println(result);

        System.out.println("done...");
    }

    private static final String TAG = HtmlMobiParser.class.getSimpleName();

    private HtmlMobiParser() {
    }

    public static HtmlMobiParser newInstance() {
        return new HtmlMobiParser();
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
        content = super.getFromContent(content, isPure, checkStr);
        return content;
    }

    @Override
    protected String _stepFinal_customPlus(String content, boolean isPure, String checkStr) {
        content = _step1_replace_P_tag(content, isPure);
        validateLog("_step1_replace_P_tag");
        content = _step1_replace_FontSize(content, isPure);
        validateLog("_step1_replace_FontSize");
        return content;
    }

    //    @Override
    protected String _step3_imageProc_custom(String content, boolean isPure, String checkStr) {
        content = _step3_imageProc_4Mobi(content, isPure, checkStr);
        validateContent("_step3_imageProc_4Mobi", content, checkStr);
        return content;
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    protected String _step1_replace_P_tag(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<p(?:.|\n)*?>((?:.|\n)*?)</p>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String textContent = mth.group(1);
            if (StringUtils.isNotBlank(textContent)) {
                textContent = StringUtil_.appendReplacementEscape(textContent);
                textContent = textContent + NEW_LINE;
                mth.appendReplacement(sb, textContent);
            }
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step1_replace_FontSize(String content, boolean isPure) {
        Pattern titleStylePtn = Pattern.compile("\\<font\\ssize\\=\"(\\d+)\">((?:.|\n)*?)</font>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String fontSize = mth.group(1);
            String textContent = mth.group(2);
            if (StringUtils.isNotBlank(textContent)) {
                textContent = StringUtil_.appendReplacementEscape(textContent);
                if (!isPure) {
                    int size = (Integer.parseInt(fontSize) / 3) * 16;
                    textContent = "{{font size:" + size + ",text:" + textContent + "}}";
                }
                mth.appendReplacement(sb, textContent);
            }
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected String _step3_imageProc_4Mobi(String content, boolean isPure, String checkPic) {
        Pattern titleStylePtn = Pattern.compile("\\<img(?:.|\n)*?recindex\\=\"(\\d+)\"(?:.|\n)*?\\/?\\>", Pattern.DOTALL | Pattern.MULTILINE);
        StringBuffer sb = new StringBuffer();
        Matcher mth = titleStylePtn.matcher(content);
        while (mth.find()) {
            String srcDesc = mth.group(1);
            String picLink = mth.group(1);

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

    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    public String getPicDirForDropbox() {
        return picDirForDropbox;
    }

}
