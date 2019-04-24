package com.example.englishtester.common.pdf.base;


import android.content.Context;

import com.example.englishtester.common.Log;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfBookHandler {

    private static final String TAG = PdfBookHandler.class.getSimpleName();

    Map<Integer, String> pageMap = new HashMap<>();
    int pageNo = 0;
    int finalPageNo = -1;

    private File pdfFile;
    private PDDocument document;
    private PDFTextStripper stripper;

    private static final String PAGE_END = "^-----PageEnd-----^";
    private static final Pattern PAGE_END_PTN = Pattern.compile("\\^\\-{5}PageEnd\\-{5}\\^", Pattern.DOTALL | Pattern.MULTILINE);

    private void initPdfBefore(File pdfFile, Context context) {
        try {
            PDFBoxResourceLoader.init(context);

            pageMap.clear();

            this.pdfFile = pdfFile;
            document = PDDocument.load(pdfFile);
            stripper = new PDFTextStripper();
            stripper.setLineSeparator("");
            stripper.setParagraphStart("\n");
            stripper.setParagraphEnd("\n");
            stripper.setPageEnd("");//PAGE_END

            finalPageNo = document.getNumberOfPages();

            pageNo = 0;
            stripper.setStartPage(1);
            stripper.setEndPage(Integer.MAX_VALUE);
        } catch (Exception e) {
            throw new RuntimeException("initPdfBefore ERR : " + e.getMessage(), e);
        }
    }

    private String getPage(int pageNo) {
        try {
            if (pageMap.containsKey(pageNo)) {
                return pageMap.get(pageNo);
            }
            stripper.setStartPage(pageNo);
            stripper.setEndPage(pageNo);
            String pageContent = stripper.getText(document);
            pageMap.put(pageNo, pageContent);
            return pageContent;
        } catch (Exception ex) {
            throw new RuntimeException("getPage, ERR : " + ex.getMessage(), ex);
        }
    }

    public void closePreviousBook() {
        try {
            document.close();
        } catch (Exception ex) {
        }
    }

    public PdfBookHandler(File pdfFile, Context context) {
        this.closePreviousBook();
        this.initPdfBefore(pdfFile, context);
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