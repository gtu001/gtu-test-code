/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

import gtu.itext.iisi.header.HeaderPosition;
import gtu.itext.iisi.header.HeaderStyle;
import gtu.itext.iisi.header.SimpleStringHeader;

import java.util.EnumMap;
import java.util.Map.Entry;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author tsaicf
 */
public class LayoutInfo {

    final EnumMap<HeaderPosition, HeaderStyle> headers = new EnumMap<HeaderPosition, HeaderStyle>(HeaderPosition.class);

    final float marginLeft;

    final float marginRight;

    final float marginTop;

    final float marginBottom;

    float headerExtra = 6f;

    float footerExtra = 6f;

    int firstPage = 1;

    public LayoutInfo() {
        this(36f, 36f, 36f, 36f);
    }

    public LayoutInfo(PageSize pageSize) {
        this(36f, 36f, 36f, 36f);
    }

    public LayoutInfo(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        super();
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }

    public void setHeader(final HeaderPosition position, String text, int fontSize) {
        final HeaderStyle header = new SimpleStringHeader(text, fontSize);
        setHeader(position, header);
    }

    public void setHeader(final HeaderPosition position, final HeaderStyle header) {
        this.headers.put(position, header);
    }

    public boolean hasHeader() {
        return this.headers.size() > 0;
    }

    public void drawHeaders(PdfContentByte dc, Document document, BaseFont baseFont, int pageNo) {
        final String pageNoStr = String.valueOf(pageNo);
        for (Entry<HeaderPosition, HeaderStyle> entry : this.headers.entrySet()) {
            HeaderPosition position = entry.getKey();
            HeaderStyle style = entry.getValue();
            if (position.isHeader()) {
                style.draw(position, dc, document, baseFont, pageNoStr, this.headerExtra);
            } else {
                style.draw(position, dc, document, baseFont, pageNoStr, this.footerExtra);
            }
        }
    }

    public void drawHeaderTemplates(PdfWriter writer, BaseFont baseFont, int totalPage) {
        for (Entry<HeaderPosition, HeaderStyle> entry : this.headers.entrySet()) {
            HeaderStyle style = entry.getValue();
            style.drawTemplate(writer, baseFont, String.valueOf(totalPage));
        }
    }

    /**
     * @param footerExtra the footerExtra to set
     */
    public void setFooterExtra(float footerExtra) {
        this.footerExtra = footerExtra;
    }

    /**
     * @param headerExtra the headerExtra to set
     */
    public void setHeaderExtra(float headerExtra) {
        this.headerExtra = headerExtra;
    }

    /**
     * @param firstPage the firstPage to set
     */
    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

}
