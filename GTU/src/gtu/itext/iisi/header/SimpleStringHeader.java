/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.header;

import java.awt.geom.Point2D.Float;

import org.apache.commons.lang3.StringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author tsaicf
 */
public class SimpleStringHeader implements HeaderStyle {

    final private String text;

    final private int fontSize;

    public SimpleStringHeader(String text, int fontSize) {
        super();
        this.text = text;
        this.fontSize = fontSize;
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    //dc.showTextAligned(PdfContentByte.ALIGN_LEFT, PDFDocument.this.leftHeader, document.left(), document.top() + 5, 0); //     輸出左上角的 header
    //dc.showTextAligned(PdfContentByte.ALIGN_RIGHT, PDFDocument.this.rightHeader, document.right(), document.top() + 5, 0);//   輸出右上角的 header
    //dc.showTextAligned(PdfContentByte.ALIGN_LEFT, PDFDocument.this.leftFooter, document.left(), 15, 0);//                      輸出左下角的 footer
    //dc.showTextAligned(PdfContentByte.ALIGN_RIGHT, PDFDocument.this.rightFooter, document.right(), 15, 0); //                  輸出右下角的 footer

    @Override
    public void draw(HeaderPosition position, PdfContentByte dc, Document document, BaseFont baseFont, String value, float extra) {

        if (StringUtils.isBlank(this.text)) {
            return;
        }

        String[] split = this.text.split("\\s+");

        final int align = position.getAlign();
        final Float point = position.getPoint(document, this.fontSize, extra);
        dc.setFontAndSize(baseFont, this.fontSize);

        if (split.length == 1) {
            //System.out.println(split[0]);
            dc.showTextAligned(align, split[0], point.x, point.y, 0); // rotation : 0
        } else {

            final float lingHeight = this.fontSize + 0.5f;
            float ty = point.y + (lingHeight * (split.length - 1));
            for (int i = 0; i < split.length; i++) {
                //System.out.println(split[i]);
                dc.showTextAligned(align, split[i], point.x, ty, 0); // rotation : 0
                ty -= lingHeight;
            }
        }

    }

    /**
     * @see tw.gov.moi.ae.report.itext2.header.HeaderStyle#drawTemplate(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.pdf.BaseFont, java.lang.String)
     */
    @Override
    public void drawTemplate(PdfWriter writer, BaseFont baseFont, String value) {
    }

}
