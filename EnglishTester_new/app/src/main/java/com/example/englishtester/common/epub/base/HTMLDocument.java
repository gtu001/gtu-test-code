package com.example.englishtester.common.epub.base;

import com.example.englishtester.common.Log;

import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by wistronits on 2018/7/31.
 */

public class HTMLDocument {

    private static final String TAG = HTMLDocument.class.getSimpleName();

    private static final String CHANGE_PAGE_TAG = "<!--pageChange-->";

    private StringBuffer sb = new StringBuffer();
    private URL baseUrl;
    private Dictionary<Object, Object> documentProperties = null;

    private String href;
    private String title;
    private String id;

    public void addHtml(String html) {
        sb.append(html);
    }

    public String getHtml() {
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

    public void setHref(String href) {
        this.href = href;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
