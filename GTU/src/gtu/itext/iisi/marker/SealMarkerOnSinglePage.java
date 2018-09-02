/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.marker;

import gtu.itext.iisi.DocumentUtils;

import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;

/**
 * 
 * @author tsaicf
 */
public class SealMarkerOnSinglePage extends SealMarker {
    //================================================
    //== [Enumeration types] Block Start
    //====
    //====
    //== [Enumeration types] Block End 
    //================================================
    //== [static variables] Block Start
    //====
    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SealMarkerOnSinglePage.class);

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====
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

    public SealMarkerOnSinglePage(SealInfo infos) {
        super(infos);
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

    //####################################################################
    //## [Method] sub-block : 
    //####################################################################

    /**
     * @throws IOException
     * @throws DocumentException
     * @see tw.gov.moi.ae.report.itext2.marker.Marker#drawMarker(tw.gov.moi.ae.report.itext2.DocumentUtils,
     *      com.lowagie.text.pdf.PdfContentByte, com.lowagie.text.Rectangle)
     */
    @Override
    public void drawMarker(DocumentUtils docUtils, PdfContentByte contentByte, PDFPageInfo pageInfo) throws IOException, DocumentException {

        /* TODO 文字範本，但沒參數值不畫圖 */
        /* 騎縫章或上面的字都要切成二半 */
        // 取得騎縫章或上面的文字(只能各有一組)，並將切成二半
        final Image[] img = getSealImage(docUtils, pageInfo);

        LOGGER.debug("draw Seal : {}", "" + img);

        if (img == null) {
            return;
        }

        final int currentPageNumber = pageInfo.getCurrentPageNumber();
        final int totalPageNumber = pageInfo.getTotalPageNumber();

        LOGGER.debug("totalPageNumber:{}", totalPageNumber);
        LOGGER.debug("final img1 w:{}, h:{}", img[0].getWidth(), img[0].getHeight());
        LOGGER.debug("final img2 w:{}, h:{}", img[1].getWidth(), img[1].getHeight());

        if (currentPageNumber == 1) {
            /* 第一頁(單數頁)要放img[0](上半、左半) */
            contentByte.addImage(img[0]);
        } else if (currentPageNumber == totalPageNumber) {
            /* 最一頁要放img[1](下半、右半) */
            contentByte.addImage(img[1]);
        } else {
            /* 其他二個都要放 */
            contentByte.addImage(img[0]);
            contentByte.addImage(img[1]);
        }

    }

    //====
    //== [Method] Block Stop 
    //================================================

}
