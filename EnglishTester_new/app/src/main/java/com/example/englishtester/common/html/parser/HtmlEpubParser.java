package com.example.englishtester.common.html.parser;

import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlEpubParser extends HtmlBaseParser {

    public static final String EPUB_HTML_ENCODE = "UTF8";
    public static final int HYPER_LINK_LABEL_MAX_LENGTH = 50;

    public static void main(String[] args) {
        HtmlEpubParser parser = HtmlEpubParser.newInstance();

        File file = new File("e:/gtu001_dropbox/Dropbox/Apps/gtu001_test/english_txt/A Life-Changing Exercise to Make You a Better Writer.htm");

        String result = parser.getFromFile(file, true, "");
        String dropboxDir = parser.picDirForDropbox;
        System.out.println("picDirForDropbox -- " + dropboxDir);
        System.out.println(result);

        System.out.println("done...");
    }

    private static final String TAG = HtmlEpubParser.class.getSimpleName();

    private HtmlEpubParser() {
    }

    public static HtmlEpubParser newInstance() {
        return new HtmlEpubParser();
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

//        content = _stepFinal_customPlus_TagP_noAttribute(content, isPure);
//        validateLog("_stepFinal_customPlus_TagP_noAttribute <epub 001>");

        return content;
    }

//    @Override
    protected String _step3_imageProc_custom(String content, boolean isPure, String checkStr) {
        content = _step3_imageProc_4Epub(content, isPure, checkStr);
        validateContent("_step3_imageProc_4Epub", content, checkStr);
        return content;
    }

    protected String _step3_imageProc_4Epub(String content, boolean isPure, String checkPic) {
        Pattern titleStylePtn = Pattern.compile("\\<image(?:.|\n)*?xlink\\:href\\=\"((?:.|\n)*?)\"(?:.|\n)*?\\/?\\>", Pattern.DOTALL | Pattern.MULTILINE);
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

    public String getPicDirForDropbox() {
        return picDirForDropbox;
    }

}
