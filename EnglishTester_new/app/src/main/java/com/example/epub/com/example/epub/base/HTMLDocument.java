package com.example.epub.com.example.epub.base;

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by wistronits on 2018/7/31.
 */

public class HTMLDocument {

    private static final String CHANGE_PAGE_TAG = "<!--pageChange-->";

    private StringBuffer sb = new StringBuffer();
    private URL baseUrl;
    private Dictionary<Object, Object> documentProperties = null;

    public void addHtml(String html) {
        sb.append(html);
    }

    public String getHtml(){
        return sb.toString();
    }

    public void remove(int start, int end) {
        sb.delete(start, end);
    }

    public int getLength() {
        return sb.length();
    }

    public ParserCallback getReader(int pos) {
        return new DamnCoreParserCallback();
    }

    public void setBase(URL url) {
        this.baseUrl = url;
    }

    public Dictionary<Object, Object> getDocumentProperties() {
        if (documentProperties == null) {
            documentProperties = new Hashtable<Object, Object>(2);
        }
        return documentProperties;
    }
}
