/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.header;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author tsaicf
 */
public abstract class AbatractPageHeader implements HeaderStyle {

    public enum PagePattern {
        BOTH {
            @Override
            public void draw(AbatractPageHeader pageHeader, HeaderPosition position, PdfContentByte dc, Document document, BaseFont baseFont,
                             String value, float extra) {

                dc.setFontAndSize(baseFont, pageHeader.fontSize);
                final int align = position.getAlign();
                final java.awt.geom.Point2D.Float point = position.getPoint(document, pageHeader.fontSize, extra);
                final String textPage = pageHeader.prefix + pageHeader.toPageString(value) + pageHeader.conjunction; // pageHeader.counter.count
                final String textTotal = "    " + pageHeader.suffix;
                final float widthPage = baseFont.getWidthPoint(textPage, pageHeader.fontSize); // 頁碼部分的 width, 用於計算 template 位置.
                final float widthTotal = baseFont.getWidthPoint(textTotal, pageHeader.fontSize); // 頁碼部分的 width, 用於計算 template 位置.
                final float xPos;
                final float templateX;
                switch (align) {
                    case PdfContentByte.ALIGN_CENTER: {
                        final float helf = (widthPage + widthTotal) / 2;
                        xPos = point.x - helf;
                        templateX = point.x - helf + widthPage;
                        break;
                    }
                    case PdfContentByte.ALIGN_RIGHT: {
                        xPos = point.x - widthPage - widthTotal;
                        templateX = point.x - widthTotal;
                        break;
                    }
                    default: {
                        xPos = point.x;
                        templateX = point.x + widthPage;
                        break;
                    }
                }
                dc.showTextAligned(PdfContentByte.ALIGN_LEFT, textPage, xPos, point.y, 0);
                dc.endText();
                dc.addTemplate(pageHeader.getTemplate(dc.getPdfWriter(), widthTotal), templateX, point.y - 3); // -3 為暫時中文文字對齊線問題(治標方法). 根本原因為垂直對齊問題.
                dc.beginText();

            }
        },
        ONLY_PAGE {
            @Override
            public void draw(AbatractPageHeader pageHeader, HeaderPosition position, PdfContentByte dc, Document document, BaseFont baseFont,
                             String value, float extra) {

                dc.setFontAndSize(baseFont, pageHeader.fontSize);
                final int align = position.getAlign();
                final java.awt.geom.Point2D.Float point = position.getPoint(document, pageHeader.fontSize, extra);
                final String textPage = pageHeader.prefix + pageHeader.toPageString(value) + pageHeader.suffix; // pageHeader.counter.count
                final float widthPage = baseFont.getWidthPoint(textPage, pageHeader.fontSize); // 頁碼部分的 width, 用於計算 template 位置.
                final float xPos;
                switch (align) {
                    case PdfContentByte.ALIGN_CENTER: {
                        final float helf = (widthPage) / 2;
                        xPos = point.x - helf;

                        break;
                    }
                    case PdfContentByte.ALIGN_RIGHT: {
                        xPos = point.x - widthPage;
                        break;
                    }
                    default: {
                        xPos = point.x;
                        break;
                    }
                }
                dc.showTextAligned(PdfContentByte.ALIGN_LEFT, textPage, xPos, point.y, 0);
            }
        },
        ONLY_TOTAL {
            @Override
            public void draw(AbatractPageHeader pageHeader, HeaderPosition position, PdfContentByte dc, Document document, BaseFont baseFont,
                             String value, float extra) {

                dc.setFontAndSize(baseFont, pageHeader.fontSize);
                final int align = position.getAlign();
                final java.awt.geom.Point2D.Float point = position.getPoint(document, pageHeader.fontSize, extra);

                final String textTotal = pageHeader.prefix + "    " + pageHeader.suffix;
                final float widthTotal = baseFont.getWidthPoint(textTotal, pageHeader.fontSize); // 頁碼部分的 width, 用於計算 template 位置.

                final float templateX;
                switch (align) {
                    case PdfContentByte.ALIGN_CENTER: {
                        final float helf = (widthTotal) / 2;
                        templateX = point.x - helf;
                        break;
                    }
                    case PdfContentByte.ALIGN_RIGHT: {
                        templateX = point.x - widthTotal;
                        break;
                    }
                    default: {
                        templateX = point.x;
                        break;
                    }
                }

                dc.endText();
                dc.addTemplate(pageHeader.getTemplate(dc.getPdfWriter(), widthTotal), templateX, point.y - 3); // -3 為暫時中文文字對齊線問題(治標方法). 根本原因為垂直對齊問題.
                dc.beginText();

            }
        };

        public abstract void draw(AbatractPageHeader pageHeader, HeaderPosition position, PdfContentByte dc, Document document, BaseFont baseFont,
                                  String value, float extra);
    }

    final private int fontSize;

    final PagePattern pattern;

    private String prefix = "";

    private String conjunction = "";

    private String suffix = "";

    private PdfTemplate templatePageCount = null;

    public AbatractPageHeader(PagePattern pattern, int fontSize) {
        super();
        this.pattern = pattern;
        this.fontSize = fontSize;
    }

    @Override
    public void draw(HeaderPosition position, PdfContentByte dc, Document document, BaseFont baseFont, String value, float extra) {
        this.pattern.draw(this, position, dc, document, baseFont, value, extra);
    }

    private PdfTemplate getTemplate(PdfWriter writer, float width) {
        if (this.templatePageCount == null) {
            this.templatePageCount = writer.getDirectContent().createTemplate(width, this.fontSize * 2);
        }
        return this.templatePageCount;
    }

    @Override
    public void drawTemplate(PdfWriter writer, BaseFont baseFont, String value) {
        if (this.templatePageCount == null) {
            return;
        }
        this.templatePageCount.beginText();
        this.templatePageCount.setFontAndSize(baseFont, this.fontSize);
        if (this.pattern == PagePattern.BOTH) {
            this.templatePageCount.showTextAligned(PdfContentByte.ALIGN_LEFT, toTotalPageString(value) + this.suffix, 0, 3, 0);
        } else {
            this.templatePageCount.showTextAligned(PdfContentByte.ALIGN_LEFT, this.prefix + toTotalPageString(value) + this.suffix, 0, 3, 0);
        }
        this.templatePageCount.endText();
    }

    public AbatractPageHeader setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public AbatractPageHeader setConjunction(String conjunction) {
        this.conjunction = conjunction;
        return this;
    }

    public AbatractPageHeader setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    protected String toTotalPageString(String value) {
        return value;
    }

    protected String toPageString(String value) {
        return value;
    }

}
