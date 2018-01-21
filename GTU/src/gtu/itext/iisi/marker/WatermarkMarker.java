/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.marker;

import gtu.itext.iisi.DocumentUtils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;

/**
 * 
 * @author tsaicf
 */
public class WatermarkMarker extends Marker {
    //================================================
    //== [Enumeration types] Block Start
    //====
    //====
    //== [Enumeration types] Block End 
    //================================================
    //== [static variables] Block Start
    //====
    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WatermarkMarker.class);

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    final List<WatermarkInfo> infos;

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

    public WatermarkMarker(List<WatermarkInfo> infos) {
        super();
        this.infos = infos;
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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.infos.toString();
    }

    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====

    Image[] warkImg = null;

    private Image getWarkImg(DocumentUtils docUtils, PDFPageInfo pageInfo) throws BadElementException, IOException {
        if (this.warkImg == null) {
            final ImageCacheKey key = new ImageCacheKey(pageInfo.getPageSize()//
                    , this.infos.toArray(new WatermarkInfo[0])//
                    , docUtils == DocumentUtils.COGNOS);
            this.warkImg = ImageCacheUtil.get(this, key);
        }
        return this.warkImg[0];
    }

    /**
     * @see tw.gov.moi.ae.report.itext2.marker.Marker#genImages(tw.gov.moi.ae.report.itext2.DocumentUtils,
     *      tw.gov.moi.ae.report.itext2.marker.PDFPageInfo)
     */
    @Override
    protected Image[] genImages(ImageCacheKey key) throws IOException, BadElementException {

        if (this.infos == null || this.infos.isEmpty()) {
            return new Image[0];
        } else if (this.infos.size() == 1) {
            return generateSingle(key);
        } else {
            return generateMulti(key);
        }
    }

    private Image[] generateSingle(ImageCacheKey key) throws IOException, BadElementException {

        final WatermarkInfo info = this.infos.get(0);
        final BufferedImage img;
        if (info.getTypeImg() == 1) {// watermark is image
            img = fromImage(info);
        } else {
            img = fromText(info);
        }

        final int pageW = (int) key.getWidth();
        final int pageH = (int) key.getHeight();
        LOGGER.debug("orignal page w/h:{} after scale:{}", pageW + "/" + pageH);

        if (img != null) {
            final BufferedImage allPage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            final Graphics2D g = allPage.createGraphics();
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, img.getWidth(), img.getHeight());
            g.drawImage(img, 0, 0, null);
            g.dispose();
            final Image imgWar = Image.getInstance(allPage, null);

            // 計算起始點
            final int x = info.getX();
            final int y = info.getY();
            int tx = x, ty = y;
            if (info.isCenterTranslate()) {
                tx = x + ((int) (pageW / 2) - (int) (imgWar.getWidth() / 2));
                ty = y + ((int) (pageH / 2) - (int) (imgWar.getHeight() / 2));
                LOGGER.debug("watermark location afer translate at:({},{})", tx, ty);
            }
            if (key.isCognos()) {
                /* cognos基準點是左上角 */
                imgWar.setAbsolutePosition(tx, ty - pageH);
            } else {
                /* 一般pdf基準點是左下角 */
                imgWar.setAbsolutePosition(tx, ty);
            }
            return new Image[] { imgWar };
        } else {
            return new Image[0];
        }
    }

    private Image[] generateMulti(ImageCacheKey key) throws IOException, BadElementException {
        final int pageW = (int) key.getWidth();
        final int pageH = (int) key.getHeight();
        LOGGER.debug("orignal page w/h:{} after scale:{}", pageW + "/" + pageH);

        // int pageW = (int) key.getWidth();
        // int pageH = (int) key.getHeight();
        // 20131114 erice edit: 圖在字之下則影像平面化,設定背景色為白色

        int imageType = BufferedImage.TYPE_INT_RGB;
        final BufferedImage allPage = new BufferedImage(pageW, pageH, imageType);
        final Graphics2D g = allPage.createGraphics();

        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, pageW, pageH);

        for (final WatermarkInfo info : this.infos) {
            LOGGER.debug("process info: {}", info);
            final BufferedImage img;
            if (info.getTypeImg() == 1) {// watermark is image
                img = fromImage(info);
            } else {
                img = fromText(info);
            }
            if (img != null) {
                final int pdfX = info.getX();
                final int pdfY = info.getY();
                final int tx, ty;
                if (!info.isCenterTranslate()) {
                    tx = pdfX;
                    ty = pageH - img.getHeight() - pdfY; // (pdfY+AWTY)=pageH-imgH;
                } else {
                    tx = ((int) (pageW / 2) - (int) (img.getWidth() / 2)) + pdfX;
                    ty = ((int) (pageH / 2) - (int) (img.getHeight() / 2)) - pdfY;
                }
                LOGGER.debug("watermark location afer translate at:({},{})", tx, ty);
                LOGGER.debug("size ({},{})", img.getWidth(), img.getHeight());
                g.drawImage(img, tx, ty, null);
            }
        }
        g.dispose();

        final Image imgWar = Image.getInstance(allPage, null);
        if (key.isCognos()) {
            /* cognos基準點是左上角 */
            imgWar.setAbsolutePosition(0, -pageH);
        } else {
            /* 一般pdf基準點是左下角 */
            imgWar.setAbsolutePosition(0, 0);
        }
        return new Image[] { imgWar };
    }

    private static BufferedImage fromText(WatermarkInfo info) throws BadElementException, IOException {
        final int fontSize = info.getFontSize();
        final int fontStyle = genFontStyle(info.getFontType());
        final String message = info.getWatermarkString();

        final int len = message.length();

        // 起始點在左上角
        final int maxW = (int) (fontSize * len);
        final int maxH = (int) (fontSize) + SEAL_MARGIN;
        int afterWH = (int) (Math.sqrt(Math.pow(maxW * info.getScaleRatio(), 2) + Math.pow(maxH * info.getScaleRatio(), 2)));
        LOGGER.debug("watermark Text : {}", message);
        LOGGER.debug("orignal page w/h:{} after scale:{}", maxW + "/" + maxH, afterWH + "/" + afterWH);

        int imageType = BufferedImage.TYPE_INT_ARGB; // 設定背景色為透明, 不互蓋
        final BufferedImage before = new BufferedImage(afterWH, afterWH, imageType);
        final Graphics2D g = before.createGraphics();

        /* 圖片濃淡-不使用此設定值 */
        //            final int alpha = wa.getAlpha();
        //            if (alpha < 255) {
        //                // range between 0-255, 0 is 透明 , 255 is 不透明
        //                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, ((float) alpha) / 255));
        //            }

        /* 圖片旋轉角度 介於0~360 先平移再旋轉，需考慮放大縮小倍數 */
        g.translate(translateX(fontSize * len * info.getScaleRatio(), fontSize * info.getScaleRatio(), info.getRotateAngle()),
                translateY(fontSize * len * info.getScaleRatio(), fontSize * info.getScaleRatio(), info.getRotateAngle()));

        g.rotate(Math.toRadians(info.getRotateAngle()), 0, 0);

        // 字型檔名稱
        int fontName = 0;
        if (-1 != info.getFontType().toUpperCase().indexOf("K")) {
            fontName = FONT_KAI;
        } else {
            fontName = FONT_SUNG;
        }

        int wordHigh = (int) (fontSize * info.getScaleRatio());
        LOGGER.debug("word size:{}", wordHigh);

        // 字體顏色 20131114 erice edit :圖在字之上則吃設定檔
        //            Field f = Color.class.getField("LIGHT_GRAY");
        //            if (!wa.getPrintOrder().equals("0")) {
        //                f = Color.class.getField(wa.getColor().toUpperCase());
        //            }

        Color c = new Color(205, 205, 205);
        // 中空字
        if (-1 != info.getFontType().toUpperCase().indexOf("O")) {
            /* 畫上騎縫章的字，含字型、字體大小、顏色 */
            drawOutlineString(g, fontName, fontStyle, wordHigh, c, 1, 0, maxH - SEAL_MARGIN, message);
        } else {
            // sh - fm.getDescent() - fontSize / 8
            drawString(g, fontName, fontStyle, wordHigh, c, 0, maxH - SEAL_MARGIN, message);
        }

        /* 畫底線 */
        if (-1 != info.getFontType().indexOf("U")) {
            g.setPaint(c);
            drawLine(g, 0, maxH - 2, maxW * info.getScaleRatio(), maxH - 2, c);// 減2是底線寬
            LOGGER.debug("draw line ({}) to ({})", "0," + (maxH - 2), (int) (maxW * info.getScaleRatio()) + "," + (maxH - 2));
        }
        final BufferedImage after = new BufferedImage(afterWH, afterWH, imageType);
        after.getGraphics().drawImage(before, 0, 0, afterWH, afterWH, 0, 0, afterWH, afterWH, null);
        g.dispose();

        return after;
        //        final Image image = Image.getInstance(after, null);
        //        return image;
    }

    private static BufferedImage fromImage(WatermarkInfo info) throws IOException, BadElementException {
        final String path = info.getWatermarkImg().getPath();
        LOGGER.debug("watermark img:{}", path);

        final BufferedImage before = javax.imageio.ImageIO.read(info.getWatermarkImg());
        final int afterW = (int) (before.getWidth() * info.getScaleRatio());
        final int afterH = (int) (before.getHeight() * info.getScaleRatio());
        final BufferedImage after = new BufferedImage(afterW, afterH, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = after.createGraphics();

        /* 圖片濃淡 */
        final int alpha = info.getAlpha();
        if (alpha < 255) {
            // range between 0-255, 0 is 透明 , 255 is 不透明
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, ((float) alpha) / 255));
        }

        /* 圖片旋轉角度 介於0~360 */
        g.rotate(info.getRotateAngle() * Math.PI / 180.0, after.getWidth() / 2.0, after.getHeight() / 2.0);

        g.drawImage(before, 0, 0, afterW, afterH, 0, 0, afterW, afterH, null);

        /* 圖片放大縮小倍數 */
        g.scale(info.getScaleRatio(), info.getScaleRatio());

        g.dispose();
        return after;

        //        final Image image = Image.getInstance(after, null);
        //        return image;
    }

    private static double translateX(double imageWidth, double imageHeight, double angle) {
        double resultX = 0;
        angle = angle % 360;
        if (angle <= 90) {
            resultX = imageHeight * Math.cos(Math.toRadians(90 - angle));
        } else if (angle <= 180) {
            resultX = imageHeight * Math.cos(Math.toRadians(angle - 90)) + imageWidth * Math.cos(Math.toRadians(180 - angle));
        } else if (angle <= 270) {
            resultX = imageWidth * Math.sin(Math.toRadians(270 - angle));
        } else if (angle <= 360) {
            resultX = 0;
        }
        return resultX;
    }

    private static double translateY(double imageWidth, double imageHeight, double angle) {
        double resultY = 0;
        angle = angle % 360;
        if (angle <= 90) {
            resultY = 0;
        } else if (angle <= 180) {
            resultY = imageHeight * Math.sin(Math.toRadians(angle - 90));
        } else if (angle <= 270) {
            resultY = imageHeight * Math.cos(Math.toRadians(angle - 180)) + imageWidth * Math.cos(Math.toRadians(270 - angle));
        } else if (angle <= 360) {
            resultY = imageWidth * Math.sin(Math.toRadians(360 - angle));
        }
        return resultY;
    }

    //####################################################################
    //## [Method] sub-block : 
    //####################################################################    

    /**
     * @throws IOException
     * @throws DocumentException
     * @see tw.gov.moi.ae.report.itext2.marker.Marker#drawMarker(tw.gov.moi.ae.report.itext2.DocumentUtils,
     *      com.lowagie.text.pdf.PdfContentByte, tw.gov.moi.ae.report.itext2.marker.PDFPageInfo)
     */
    @Override
    public void drawMarker(DocumentUtils docUtils, PdfContentByte contentByte, PDFPageInfo pageInfo) throws IOException, DocumentException {
        Image warkImg = getWarkImg(docUtils, pageInfo);
        LOGGER.debug("drawWatermark {}", warkImg);
        contentByte.addImage(warkImg);
    }

    //    /**
    //     * @see tw.gov.moi.ae.report.itext2.marker.Marker#getPrintOrders()
    //     */
    //    @Override
    //    public String getPrintOrders() {
    //        return info.getPrintOrder();
    //    }

    //====
    //== [Method] Block Stop 
    //================================================

}
