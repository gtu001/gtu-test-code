/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.marker;

import gtu.binary.UnicodeUtil;
import gtu.itext.iisi.CHTFontFactory;
import gtu.itext.iisi.DocumentUtils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.io.IOException;

import tw.gov.moi.common.SystemConfig;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;

/**
 * 定義浮水印繪製器
 * 
 * @author tsaicf
 */
public abstract class Marker {

    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Marker.class);

    protected static final int PAGE_MARGIN = 100; // 頁面與內文邊距

    protected static final int SEAL_MARGIN = 10; // 騎縫章邊距 moidfy 2013/01/29不留邊

    protected static final int SEAL_MARGIN_BOTTOM = 18;

    protected static final int SEAL_MARGIN_TOP = 18;

    protected static final int FONT_SUNG = 1;// 宋體

    protected static final int FONT_KAI = 2;// 楷體

    /**
     * 浮水印繪製實作.
     * 
     * @param contentByte
     * @param pageSize
     * @throws IOException
     * @throws BadElementException
     * @throws DocumentException
     */
    public abstract void drawMarker(DocumentUtils docUtils, PdfContentByte contentByte, PDFPageInfo pageInfo) throws IOException,
            BadElementException, DocumentException;

    /** P:一般;B：粗體；I：斜體；U：底線 */
    protected static int genFontStyle(final String fontType) {
        int fontStyle = 0;
        if (-1 != fontType.indexOf("P")) {
            fontStyle += Font.PLAIN;
        }
        if (-1 != fontType.indexOf("B")) {
            fontStyle += Font.BOLD;
        }
        if (-1 != fontType.indexOf("I")) {
            fontStyle += Font.ITALIC;
        }
        return fontStyle;
    }

    protected static void drawLine(final Graphics2D g2, final double x0, final double y0, final double x1, final double y1, final Color color) {
        final Shape line = new java.awt.geom.Line2D.Double(x0, y0, x1, y1);
        g2.setColor(color);
        g2.draw(line);
    }

    @SuppressWarnings("null")
    protected static Font getFont(int fontid, int fontstyle, int size, int unichar) {
        final SystemConfig systemConfig = AeBean.INSTANCE.getSystemConfig();
        final CHTFontFactory.RISFont risFont = CHTFontFactory.RISFont.lookup(fontid);
        final CHTFontFactory factory = risFont.getFactory(systemConfig);
        final int plane;
        if (unichar < 0x10000) {
            plane = 0x00;
        } else if (unichar >= 0x20000 && unichar < 0x30000) {
            plane = 0x02;
        } else if (unichar >= 0xf0000) {
            plane = 0x0f;
        } else {
            plane = 0x00;
        }
        Font awtFont = factory.getAwtFont(plane);
        if (awtFont == null) {
            LOGGER.error("can't get font for plane {}", plane);
            if (plane != 0) {
                awtFont = factory.getAwtFont(0);
            }
        }
        return awtFont.deriveFont(fontstyle, size); // TODO CATCH....

        //        return getRisFont(plane, 0x00).deriveFont(fontstyle, size);
        //        return getRisFont(fontid, 0x02).deriveFont(fontstyle, size);
        //        return getRisFont(fontid, 0x0f).deriveFont(fontstyle, size);
        //        return getRisFont(fontid, 0).deriveFont(fontstyle, size);
    }

    /**
     * 畫出一般字串
     * 
     * @param g2D
     * @param fontid
     * @param fontstyle
     * @param size
     * @param color
     * @param curX
     * @param curY
     * @param str
     */
    protected static void drawString(Graphics2D g2D, int fontid, int fontstyle, int size, Color color, float curX, float curY, String str) {
        int buf[] = UnicodeUtil.toCodePointArray(str);
        int curx = (int) curX;
        for (int i = 0; i < buf.length; i++) {
            curx += drawOneChar(g2D, fontid, fontstyle, size, color, curx, (int) curY, buf[i]);
        }
    }

    /**
     * 畫出中字空字串
     * 
     * @param g2d
     * @param fontid
     * @param fontstyle
     * @param size
     * @param color
     * @param width 邊線的寬度
     * @param curX
     * @param curY
     * @param str
     */
    protected static void drawOutlineString(Graphics2D g2d, int fontid, int fontstyle, int size, Color color, float width, float curX, float curY,
                                            String str) {
        int buf[] = UnicodeUtil.toCodePointArray(str);
        int curx = (int) curX;
        for (int i = 0; i < buf.length; i++) {
            curx += drawOutLineChar(g2d, fontid, fontstyle, size, color, width, curx, (int) curY, buf[i]);
        }
    }

    /**
     * 畫出一般字
     * 
     * @param g2D
     * @param fontid
     * @param fontstyle
     * @param size
     * @param color
     * @param curX
     * @param curY
     * @param unichar
     * @return
     */
    protected static int drawOneChar(Graphics2D g2D, int fontid, int fontstyle, int size, Color color, int curX, int curY, int unichar) {
        int[] buf = new int[1];
        Font font = getFont(fontid, fontstyle, size, unichar);
        buf[0] = unichar;
        String str = UnicodeUtil.toString(buf);
        FontMetrics fm = g2D.getFontMetrics();
        g2D.setFont(font);
        g2D.setColor(color);
        g2D.drawString(str, (int) curX, (int) curY);
        return fm.stringWidth(str);
    }

    /**
     * 畫出中字空字
     * 
     * @param g2d
     * @param fontid
     * @param fontstyle
     * @param size
     * @param color
     * @param width
     * @param curX
     * @param curY
     * @param unichar
     * @return
     */
    protected static int drawOutLineChar(Graphics2D g2d, int fontid, int fontstyle, int size, Color color, float width, int curX, int curY,
                                         int unichar) {

        int[] buf = new int[1];
        Font font = getFont(fontid, fontstyle, size, unichar);

        Graphics2D g2 = (Graphics2D) g2d.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        buf[0] = unichar;
        String str = UnicodeUtil.toString(buf);

        GlyphVector v = font.createGlyphVector(g2.getFontMetrics(font).getFontRenderContext(), str);
        Shape s = v.getOutline();
        g2.setStroke(new BasicStroke(width));
        g2.setFont(font);
        g2.setColor(color);
        g2.translate(curX, curY);
        g2.draw(s);

        FontMetrics fm = g2.getFontMetrics(font);
        return fm.stringWidth(str);
    }

    /**
     * @param docUtils
     * @param pageInfo
     * @throws IOException
     * @throws BadElementException
     */
    abstract protected Image[] genImages(ImageCacheKey key) throws IOException, BadElementException;

}
