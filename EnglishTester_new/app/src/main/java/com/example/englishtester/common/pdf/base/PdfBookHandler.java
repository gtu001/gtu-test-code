package com.example.englishtester.common.pdf.base;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfBookHandler {

    List<String> pages = new ArrayList<String>();
    int pageIndex = -1;

    private File pdfFile;
    private PDDocument document;
    private PDFTextStripper stripper;


    private void initPdfBefore(File pdfFile) {
        try {
            this.pdfFile = pdfFile;
            document = PDDocument.load(pdfFile);
            stripper = new PDFTextStripper();
            int keyLength = 256;
            AccessPermission ap = new AccessPermission();
            ap.setCanPrint(false);
            StandardProtectionPolicy spp = new StandardProtectionPolicy("12345", "", ap);
            spp.setEncryptionKeyLength(keyLength);
            spp.setPermissions(ap);
        } catch (Exception e) {
            throw new RuntimeException("initPdfBefore ERR : " + e.getMessage(), e);
        }
    }

    private String getPage(int pageNo) {
        try {
            stripper.setStartPage(pageNo);
            stripper.setEndPage(pageNo);
            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("getPage ERR : " + e.getMessage(), e);
        }
    }

    public void close() {
        try {
            document.close();
        } catch (Exception ex) {
            throw new RuntimeException("close ERR : " + ex.getMessage(), ex);
        }
    }

    public PdfBookHandler(File pdfFile) {
        this.initPdfBefore(pdfFile);
        for (int ii = 1; ii <= document.getNumberOfPages(); ii++) {
            String pageContent = this.getPage(ii);
            pages.add(pageContent);
        }
        this.close();
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
}