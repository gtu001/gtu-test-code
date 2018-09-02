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
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author tsaicf
 */
public interface HeaderStyle {

    /**
     * @param position
     * @param dc
     * @param document
     * @param baseFont
     */
    void draw(HeaderPosition position, PdfContentByte dc, Document document, BaseFont baseFont, String value, float extra);

    /**
     * @param writer
     * @param baseFont
     * @param value
     */
    void drawTemplate(PdfWriter writer, BaseFont baseFont, String value);

}
