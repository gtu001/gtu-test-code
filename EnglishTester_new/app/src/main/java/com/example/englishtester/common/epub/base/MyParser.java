package com.example.englishtester.common.epub.base;


import com.example.englishtester.common.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by wistronits on 2018/8/7.
 */

public class MyParser implements Parser {
    private static final String TAG = MyParser.class.getSimpleName();

    private HTMLDocument htmlDocument;

    public MyParser(HTMLDocument htmlDocument) {
        this.htmlDocument = htmlDocument;
    }

    @Override
    public void parse(Reader contentReader, ParserCallback parserCallback, boolean ignoreCharSet) {
        String htmlContent = getString((StringReader) contentReader);
        Log.v(TAG, "html = " + htmlContent, 0);
        this.htmlDocument.addHtml(htmlContent);
    }

    private String getString(StringReader contentReader) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(contentReader);
            StringBuffer sb = new StringBuffer();
            for (String line = null; (line = reader.readLine()) != null; ) {
                sb.append(line + "\r\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("parse ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }
}
