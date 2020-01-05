package com.example.englishtester.common.txtbuffer.base;


import android.content.Context;

import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.FileUtilAndroid;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.TxtReaderAppenderPageDivider;
import com.example.englishtester.common.html.parser.HtmlWordParser;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TxtBufferBookHandler {

    private static final String TAG = TxtBufferBookHandler.class.getSimpleName();

    Map<Integer, String> pageMap = new HashMap<>();
    int pageNo = 0;
    int finalPageNo = -1;

    List<String> pageLst = new ArrayList<>();
    List<String> page4TransalteLst = new ArrayList<String>();

    private File txtFile;

    private void initTxtBufferBefore(File txtFile, Context context) {
        try {
            this.txtFile = txtFile;
            String pageContent = FileUtilAndroid.loadFileToString(txtFile);
            pageLst.add(pageContent);
            finalPageNo = 99999;
        } catch (Exception e) {
            throw new RuntimeException("initTxtBufferBefore ERR : " + e.getMessage(), e);
        }
    }

    private String getPage(int pageNo) {
        try {
            if(pageNo > 1){
                return "     ";
            }
            //---------------------------------------
            if (pageMap.containsKey(pageNo)) {
                return pageMap.get(pageNo);
            }
            String pageContent = pageLst.get(0);//pageLst.get(pageNo - 1);
            pageMap.put(pageNo, pageContent);
            return pageContent;
        } catch (Exception ex) {
            throw new RuntimeException("getPage, ERR : " + ex.getMessage(), ex);
        }
    }

    public TxtBufferBookHandler(File pdfFile, Context context) {
        this.initTxtBufferBefore(pdfFile, context);
    }

    public void next() {
        if (pageNo >= finalPageNo) {
            pageNo = finalPageNo;
        } else {
            pageNo++;
        }
    }

    public void previous() {
        if (pageNo <= 1) {
            pageNo = 1;
        } else {
            pageNo--;
        }
    }

    public String getPage() {
        return getPage(pageNo);
    }

    public String getFirstPage() {
        pageNo = 1;
        return getPage();
    }

    public boolean hasNext() {
        return pageNo < finalPageNo;
    }
}