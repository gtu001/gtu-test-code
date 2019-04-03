package com.example.englishtester.common.mobi.base;

import org.apache.commons.lang3.StringUtils;
import org.rr.mobi4java.MobiDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobiBookHandler {
    String allContent;
    Map<String, byte[]> imgMap = new HashMap<String, byte[]>();
    List<String> pages = new ArrayList<String>();
    int pageIndex = -1;
    MobiDocument mobiDoc;

    private void initImage(MobiDocument mobiDoc) {
        Pattern ptn = Pattern.compile("recindex\\=\"(\\d+)\"", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(allContent);
        while (mth.find()) {
            String img = mth.group(1);
            byte[] bytes = mobiDoc.getImages().get(Integer.parseInt(img) - 1);
            imgMap.put(img, bytes);
        }
    }

    private void initPages() {
        Pattern ptn = Pattern.compile("\\<\\/?mbp\\:pagebreak\\/?\\>", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(allContent);
        String tmpPage = "";
        int startPos = 0;
        while (mth.find()) {
            tmpPage = StringUtils.substring(allContent, startPos, mth.start());
            startPos = mth.end();
            if (StringUtils.isNotBlank(tmpPage)) {
                pages.add(tmpPage);
            }
        }
    }

    public MobiBookHandler(MobiDocument mobiDoc) throws IOException {
        this.mobiDoc = mobiDoc;
        this.allContent = mobiDoc.getTextContent();
        this.initImage(mobiDoc);
        this.initPages();
    }

    public void next() {
        if (pageIndex >= pages.size() - 1) {
            pageIndex = pages.size() - 1;
        } else {
            pageIndex++;
        }
    }

    public void previous() {
        if (pageIndex <= 0) {
            pageIndex = 0;
        } else {
            pageIndex--;
        }
    }

    public String getPage() {
        return pages.get(pageIndex);
    }

    public String getFirstPage() {
        pageIndex = 0;
        return getPage();
    }

    public boolean hasNext() {
        return !(pageIndex >= pages.size() - 1);
    }

    public byte[] getImage(String recindex){
        return imgMap.get(recindex);
    }
}