/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

import gtu.itext.iisi.marker.MarkInfo;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import tw.gov.moi.ae.filesystem.RisFile;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;

/**
 * 
 * @author tsaicf
 */
public abstract class AbstractPDFGenerator {

    //================================================
    //== [Enumeration types] Block Start
    //====
    //====
    //== [Enumeration types] Block End 
    //================================================
    //== [static variables] Block Start
    //====
    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AbstractPDFGenerator.class);

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    private Rectangle pageSize;

    protected LayoutInfo marginInfo;

    private List<MarkInfo> markInfos;

    private Properties properties;

    //====
    //== [instance variables] Block Stop 
    //================================================
    //== [static Constructor] Block Start
    //====
    //====
    //== [static Constructor] Block Stop 
    //================================================
    //== [Constructors] Block Start (含init method)
    //====

    final protected void setup(Rectangle pageSize) {
        this.setup(pageSize, new LayoutInfo(), Collections.<MarkInfo> emptyList(), null);
    }

    final protected void setup(Rectangle pageSize, LayoutInfo marginInfo) {
        this.setup(pageSize, marginInfo, Collections.<MarkInfo> emptyList(), null);
    }

    final protected void setup(Rectangle pageSize, LayoutInfo marginInfo, List<MarkInfo> markInfos) {
        this.setup(pageSize, marginInfo, markInfos, null);
    }

    final protected void setup(Rectangle pageSize, LayoutInfo marginInfo, List<MarkInfo> markInfos, Properties properties) {
        this.pageSize = pageSize;
        this.marginInfo = marginInfo;
        this.markInfos = markInfos;
        this.properties = properties;
    }

    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Accessor] Block Start
    //====
    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (Ex. toString/equals+hashCode)
    //====
    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====

    /**
     * 產生指定帳號的對應PDF檔案.
     * 
     * @param account
     * @return
     * @throws Exception
     */
    public PDFInfo generate(PDFDocumentManager manager, RisFile outputFile) throws Exception {
        return generate(manager, outputFile.getFile());
    }

    /**
     * 完成開檔及存檔的動作, 實際內容的產生交由子Class負責.
     */
    public PDFInfo generate(PDFDocumentManager manager, File outputFile) throws DocumentException {
        File baseDir = outputFile.getParentFile();
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        PDFDocument pdfDocBuilder = null;
        PDFInfo pdfInfo = null;
        try {
            if (this.properties == null) {
                this.properties = new Properties();
            }

            pdfDocBuilder = manager.getPDFDocument(outputFile, this.pageSize, this.marginInfo, this.markInfos, this.properties);
            this.generateContent(pdfDocBuilder);
        } catch (Exception ex) {
            if (pdfDocBuilder != null) {
                try {
                    pdfDocBuilder.writeText("未完成文件輸出" + "：" + ex.getMessage());
                } catch (Exception e) {
                }
            } else {
                LOGGER.warn("無法取得 Pdf 所需資源!");
            }
            throw new DocumentException(ex);
        } finally {
            if (pdfDocBuilder != null) {
                try {
                    pdfInfo = pdfDocBuilder.close();
                    LOGGER.debug("產出檔案長度為：" + pdfInfo.getFileSize());
                    LOGGER.debug("產出檔案頁數為：" + pdfInfo.getPages());
                } catch (IOException ex) {
                    throw new DocumentException("無法關閉 pdf resource! ");
                }
            }
        }
        return pdfInfo;
    }

    //####################################################################
    //## [Method] sub-block : 
    //####################################################################

    abstract protected void generateContent(PDFDocument pdfDocument) throws DocumentException, InterruptedException;

    //====
    //== [Method] Block Stop 
    //================================================

}
