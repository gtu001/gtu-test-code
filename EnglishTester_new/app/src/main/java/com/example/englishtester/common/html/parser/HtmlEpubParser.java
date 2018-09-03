package com.example.englishtester.common.html.parser;

import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.Log;

import java.io.File;


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
    protected String _stepFinal_customPlus(String content, boolean isPure) {

//        content = _stepFinal_customPlus_TagP_noAttribute(content, isPure);
//        validateLog("_stepFinal_customPlus_TagP_noAttribute <epub 001>");

        return content;
    }


    public String getPicDirForDropbox() {
        return picDirForDropbox;
    }

}
