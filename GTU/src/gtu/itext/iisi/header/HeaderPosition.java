/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.header;

import java.awt.geom.Point2D.Float;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;

/**
 * 
 * @author tsaicf
 */
public enum HeaderPosition {

    LeftHeader(PdfContentByte.ALIGN_LEFT) {
        @Override
        public Float getPoint(Document document, int fontSize, float extra) {
            return new Float(document.left(), document.top() + 5 + extra);
        }
    }, //
    LeftFooter(PdfContentByte.ALIGN_LEFT) {
        @Override
        public Float getPoint(Document document, int fontSize, float extra) {
            return new Float(document.left(), document.bottomMargin() - fontSize - extra);
        }
    }, //

    RightHeader(PdfContentByte.ALIGN_RIGHT) {
        @Override
        public Float getPoint(Document document, int fontSize, float extra) {
            return new Float(document.right(), document.top() + 5 + extra);
        }
    }, //
    RightFooter(PdfContentByte.ALIGN_RIGHT) {
        @Override
        public Float getPoint(Document document, int fontSize, float extra) {
            return new Float(document.right(), document.bottomMargin() - fontSize - extra);
        }
    }, //
    CenterHeader(PdfContentByte.ALIGN_CENTER) {
        @Override
        public Float getPoint(Document document, int fontSize, float extra) {
            return new Float((document.right() + document.leftMargin()) / 2, document.top() + 5 + extra);
        }
    }, //
    CenterFooter(PdfContentByte.ALIGN_CENTER) {
        @Override
        public Float getPoint(Document document, int fontSize, float extra) {
            return new Float((document.right() + document.leftMargin()) / 2, document.bottomMargin() - fontSize - extra);
        }
    }, //
    ;

    final int align;

    private HeaderPosition(int align) {
        this.align = align;
    }

    /**
     * @return
     */
    public int getAlign() {
        return this.align;
    }

    public boolean isHeader() {
        return this == LeftHeader || this == CenterHeader || this == RightHeader;
    }

    /**
     * @param document
     * @param fontSize TODO
     * @param extra TODO
     * @return
     */
    public abstract Float getPoint(Document document, int fontSize, float extra);

}
