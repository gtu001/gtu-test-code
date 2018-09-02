/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.header;

import gtu.itext.iisi.RisFontSelector;
import gtu.itext.iisi.SubPhrase;

import java.awt.geom.Point2D.Float;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author tsaicf
 */
public class StringHeader implements HeaderStyle {

    final private SubPhrase[] phrases;

    public StringHeader(SubPhrase... phrases) {
        super();
        this.phrases = phrases;
    }

    @Override
    public void draw(HeaderPosition position, PdfContentByte dc, Document document, BaseFont baseFont, String value, float extra) {

        if (this.phrases == null || this.phrases.length == 0) {
            return;
        }

        final int align = position.getAlign();
        int maxSize = 0;
        for (SubPhrase sp : this.phrases) {
            maxSize = Math.max(maxSize, sp.getFontInfo().getSize());
        }
        final Float point = position.getPoint(document, maxSize, extra);

        try {
            Paragraph p = RisFontSelector.process(this.phrases);
            //System.out.println(p.getContent());
            PdfPTable ptable = new PdfPTable(1);
            ptable.setWidthPercentage(100);
            final PdfPCell c = new PdfPCell(p);
            c.setBorder(0);
            c.setBorderWidth(0);
            c.setHorizontalAlignment(align);
            c.setBackgroundColor(null);
            ptable.setTotalWidth(document.right() - document.left());

            dc.endText();
            if (position.isHeader()) {
                c.setVerticalAlignment(Element.ALIGN_BOTTOM);
                c.setMinimumHeight(document.getPageSize().getHeight() - point.y);
                ptable.addCell(c);
                ptable.writeSelectedRows(0, 1, document.left(), document.getPageSize().getHeight() - 1, dc);
            } else {
                ptable.addCell(c);
                ptable.writeSelectedRows(0, 1, document.left(), document.bottomMargin() - extra, dc);
            }
            dc.beginText();
        } catch (DocumentException e) {
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
