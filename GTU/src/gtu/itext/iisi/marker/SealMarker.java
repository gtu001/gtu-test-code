/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.marker;

import gtu.itext.iisi.DocumentUtils;
import gtu.itext.iisi.marker.ImageCacheKey.MarkerSetting;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;

/**
 * 
 * @author tsaicf
 */
public class SealMarker extends Marker {
    //================================================
    //== [Enumeration types] Block Start
    //====
    //====
    //== [Enumeration types] Block End 
    //================================================
    //== [static variables] Block Start
    //====
    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SealMarker.class);

    /** 騎縫章裁切比率 */
    private static final double[] ratio = { 0.25, 0.3, 0.4, 0.5, 0.6, 0.7, 0.75 };

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    final SealInfo infos;

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

    public SealMarker(SealInfo infos) {
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

    /**
     * 取得騎縫章調整後的圖片
     * 
     * @param info
     * @param param
     * @return
     * @throws IOException
     */
    private static BufferedImage getSealImg(SealInfo info) throws IOException {

        Graphics2D g;
        BufferedImage before, after;

        if (info.getTypeImg() == 1) {// seamseal is image
            after = srcFromImage(info);
        } else {
            final int fontSize = info.getFontSize();
            final int fontStyle = genFontStyle(info.getFontType());
            String message = info.getSeamsealString();

            final int len = message.length();

            /* 字體寬度、高度乘上放大縮小倍數擇較大的=圖的長寬 */
            final int maxW = (int) (fontSize * len * info.getScaleRatio());
            final int maxH = (int) (fontSize * 2 * info.getScaleRatio());
            final int sidelineLen = (maxW > maxH ? maxW : maxH) + 10;// 加上邊界距離
            before = new BufferedImage(sidelineLen, sidelineLen, BufferedImage.TYPE_INT_ARGB);
            g = before.createGraphics();

            /* 圖片濃淡 */
            final int alpha = info.getAlpha();
            if (alpha < 255) {
                // range between 0-255, 0 is 透明 , 255 is 不透明
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, ((float) alpha) / 255));
            }

            /* 圖片旋轉角度 介於0~360 */
            g.rotate(info.getRotateAngle() * Math.PI / 180.0, before.getWidth() / 2.0, before.getHeight() / 2.0);

            /* 圖片放大縮小倍數 */
            g.scale(info.getScaleRatio(), info.getScaleRatio());

            final FontMetrics fm = g.getFontMetrics(); // 字型參數資料物件
            final int sw = fm.stringWidth(message); // 字串寬度
            final int sh = sw;// 旋轉關係，寬高會相等fm.getHeight(); // 字型高度
            g.setColor(new Color(255, 255, 255, 0)); // 背景顏色:透明
            g.fillRect(0, 0, sw, sh); // 背景塗色
            g.setColor(new Color(0, 0, 0, info.getAlpha())); // 字串顏色
            final RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHints(qualityHints);

            // 字型檔名稱
            int fontName = 0;
            if (-1 != info.getFontType().toUpperCase().indexOf("K")) {
                fontName = FONT_KAI;
            } else {
                fontName = FONT_SUNG;
            }

            // 中空字
            if (-1 != info.getFontType().toUpperCase().indexOf("O")) {
                /* 畫上騎縫章的字，含字型、字體大小、顏色 */
                drawOutlineString(g, fontName, fontStyle, fontSize, Color.BLACK, 1, 0, sh - fm.getDescent() - fontSize / 8, message);
            } else {
                drawString(g, fontName, fontStyle, fontSize, Color.BLACK, 0, sh - fm.getDescent() - fontSize / 8, message);
            }

            /* 畫底線 */
            if (-1 != info.getFontType().indexOf("U")) {
                g.setPaint(Color.black);
                drawLine(g, 0, sh - 2, sw, sh - 2, Color.BLACK);
            }

            after = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_ARGB);
            after.getGraphics().drawImage(before, 0, 0, sw, sh, 0, 0, sw, sh, null);
            g.dispose();
        }
        return after;
    }

    private static BufferedImage srcFromImage(SealInfo info) throws IOException {
        Graphics2D g;
        BufferedImage before;
        BufferedImage after;
        final String path = info.getSeamsealImg().getPath();
        LOGGER.debug("seamseal img:{}", path);

        before = javax.imageio.ImageIO.read(info.getSeamsealImg());
        final int afterW = (int) (before.getWidth() * info.getScaleRatio());
        final int afterH = (int) (before.getHeight() * info.getScaleRatio());
        after = new BufferedImage(afterW, afterH, BufferedImage.TYPE_INT_ARGB);
        g = after.createGraphics();

        /* 圖片濃淡 */
        final int alpha = info.getAlpha();
        if (alpha < 255) {
            // range between 0-255, 0 is 透明 , 255 is 不透明
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, ((float) alpha) / 255));
        }

        /* 圖片旋轉角度 介於0~360 */
        g.rotate(info.getRotateAngle() * Math.PI / 180.0, after.getWidth() / 2.0, after.getHeight() / 2.0);

        /* 圖片放大縮小倍數 */
        g.scale(info.getScaleRatio(), info.getScaleRatio());

        g.drawImage(before, 0, 0, null);
        g.dispose();
        return after;
    }

    //####################################################################
    //## [Method] sub-block : 
    //####################################################################

    /**
     * 切割出7種不同比率的騎縫章，共14張圖
     * 
     * @param sealV
     * @param sealH
     * @param ratio
     * @param originalSealImage
     * @param vertical
     * @param fileComeFromCognos
     * @throws IOException
     * @throws BadElementException
     * @throws Exception
     */
    private static Image[] cutSeal(final BufferedImage originalSealImage//
                                   , final boolean vertical//
                                   , final ImageCacheKey key //
                                   , final SealInfo info // 
    ) throws BadElementException, IOException {

        final int pageW = (int) key.getWidth();
        final int pageH = (int) key.getHeight();
        LOGGER.debug("page w:{}, h:{}", pageW, pageH);

        final int cutting = info.getCutting(); /* 預設切對半 */
        final int position = info.getPosition(); /* 預設切對半 */

        final int orgSealImgW = originalSealImage.getWidth(null);
        final int orgSealImgH = originalSealImage.getHeight(null);
        //        BufferedImage halfSeal;
        //        BufferedImage theOtherHalfSeal;

        int seal1W, seal1H;
        int Hrange, Wrange;
        int x1, y1, x2, y2;

        int i = cutting * 2;
        final Image[] img = new Image[2];
        if (vertical) {
            final int newHeight = (int) (orgSealImgH * ratio[i / 2]);
            final BufferedImage halfSeal = new BufferedImage(orgSealImgW, newHeight, BufferedImage.TYPE_INT_ARGB);
            final BufferedImage theOtherHalfSeal = new BufferedImage(orgSealImgW, orgSealImgH - newHeight, BufferedImage.TYPE_INT_ARGB);
            halfSeal.getGraphics()//
                    .drawImage(originalSealImage, 0, 0, orgSealImgW, newHeight, 0, 0, orgSealImgW, newHeight, null);
            theOtherHalfSeal.getGraphics()//
                    .drawImage(originalSealImage, 0, 0, orgSealImgW, orgSealImgH - newHeight, 0, newHeight, orgSealImgW, orgSealImgH, null);
            img[0] = Image.getInstance(halfSeal, null);
            img[1] = Image.getInstance(theOtherHalfSeal, null);

            seal1W = (int) img[0].getWidth();
            seal1H = (int) img[0].getHeight();

            Wrange = pageW - 2 * PAGE_MARGIN - seal1W;
            if (Wrange < 0) {
                Wrange = 0;
            }
            final int positionX = PAGE_MARGIN + (int) (ratio[position] * Wrange);

            x1 = positionX;
            x2 = positionX;
            if (key.isCognos()) {
                /* cognos基準點是左上角 */
                y1 = SEAL_MARGIN - pageH;
                y2 = -seal1H - SEAL_MARGIN;
            } else {
                /* 一般pdf基準點是左下角 */
                y1 = SEAL_MARGIN_BOTTOM; //SEAL_MARGIN;
                y2 = pageH - seal1H - SEAL_MARGIN_BOTTOM; // 
            }

            img[0].setAbsolutePosition(x1, y1);// 上半
            img[1].setAbsolutePosition(x2, y2);// 下半
            LOGGER.debug("騎縫章上 設定 位置 x: {}, y: {} img1:" + img[0].getWidth() + ":" + img[0].getHeight(), x1, y1);
            LOGGER.debug("騎縫章下 設定 位置 x: {}, y: {} img2:" + img[1].getWidth() + ":" + img[1].getHeight(), x2, y2);

        } else {
            final int newWidth = (int) (orgSealImgW * SealMarker.ratio[i / 2]);
            final BufferedImage halfSeal = new BufferedImage(newWidth, orgSealImgH, BufferedImage.TYPE_INT_ARGB);
            final BufferedImage theOtherHalfSeal = new BufferedImage(orgSealImgW - newWidth, orgSealImgH, BufferedImage.TYPE_INT_ARGB);
            halfSeal.getGraphics()//
                    .drawImage(originalSealImage, 0, 0, newWidth, orgSealImgH, 0, 0, newWidth, orgSealImgH, null);
            theOtherHalfSeal.getGraphics()//
                    .drawImage(originalSealImage, 0, 0, orgSealImgW - newWidth, orgSealImgH, newWidth, 0, orgSealImgW, orgSealImgH, null);
            img[0] = Image.getInstance(halfSeal, null);
            img[1] = Image.getInstance(theOtherHalfSeal, null);

            seal1W = (int) img[0].getWidth();
            seal1H = (int) img[0].getHeight();

            Hrange = pageH - 2 * PAGE_MARGIN - seal1H;
            if (Hrange < 0) {
                Hrange = 0;
            }
            final int positionY = PAGE_MARGIN + (int) (ratio[position] * Hrange);

            x1 = pageW - seal1W - SEAL_MARGIN;
            x2 = SEAL_MARGIN;
            if (key.isCognos()) {
                y1 = positionY - pageH;
                y2 = positionY - pageH;
            } else {
                y1 = positionY;
                y2 = positionY;
            }

            img[0].setAbsolutePosition(x1, y1);// 左半
            img[1].setAbsolutePosition(x2, y2);// 右半
            LOGGER.debug("騎縫章左 設定 位置 x: {}, y: {}", x1, y1);
            LOGGER.debug("騎縫章右 設定 位置 x: {}, y: {}", x2, y2);

        }

        return img;
    }

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

        final SealInfo info = this.infos;
        final String bindingType = info.getBinding();
        final int currentPageNumber = pageInfo.getCurrentPageNumber();
        final int totalPageNumber = pageInfo.getTotalPageNumber();

        LOGGER.debug("totalPageNumber:{}", totalPageNumber);
        LOGGER.debug("isBinding:{} positionType:{}", info.isBinding(), info.getPositionType());
        if (!info.isBinding() && info.getPositionType() == PositionType.NO_FIRST && totalPageNumber == 1) {
            // 只有一頁...
            // 第一頁且沒裝訂但第一頁不印，不做事
            return;
        }
        if (info.isBinding() && currentPageNumber == 1) {
            // 裝訂且第一頁，跳過
            return;
        }

        LOGGER.debug("final img1 w:{}, h:{}", img[0].getWidth(), img[0].getHeight());
        LOGGER.debug("final img2 w:{}, h:{}", img[1].getWidth(), img[1].getHeight());

        // 不裝訂
        if (!info.isBinding()) {

            // 第一頁
            if (currentPageNumber == 1) {
                if (info.getPositionType().equals(PositionType.CONTINUE)) {
                    // 由左至右=左半;由上至下=上半
                    contentByte.addImage(img[1]);
                }
                contentByte.addImage(img[0]);

                // 最後一頁
            } else if (currentPageNumber == totalPageNumber) {
                if (info.getPositionType().equals(PositionType.CONTINUE)) {
                    // 由左至右=右半;由上至下=下半
                    contentByte.addImage(img[0]);
                }
                contentByte.addImage(img[1]);

                // 其他頁
            } else {
                contentByte.addImage(img[1]);
                contentByte.addImage(img[0]);
            }

            // 要裝訂
        } else {

            if (currentPageNumber % 2 == 0) {
                /* 右、下裝訂，則雙數頁要放img[1](左半、上半) */
                if ("E".equalsIgnoreCase(bindingType) || "S".equalsIgnoreCase(bindingType)) {
                    contentByte.addImage(img[1]);
                } else {
                    contentByte.addImage(img[0]);
                }

            } else {
                /* 單數頁相反 */
                if ("E".equalsIgnoreCase(bindingType) || "S".equalsIgnoreCase(bindingType)) {
                    contentByte.addImage(img[0]);
                } else {
                    contentByte.addImage(img[1]);
                }

            }

        }

    }

    private Image[] imgs = null;

    protected Image[] getSealImage(DocumentUtils docUtils, PDFPageInfo pageInfo) throws IOException, BadElementException {
        final SealInfo info = this.infos;
        if (info.getTypeImg() != 1 && info.getTypeImg() != 2) {
            return null;
        }
        if (this.imgs == null) {
            final ImageCacheKey key = new ImageCacheKey(pageInfo.getPageSize() //
                    , new MarkerSetting[] { this.infos } //
                    , docUtils == DocumentUtils.COGNOS //
                );
            this.imgs = ImageCacheUtil.get(this, key);
        }
        return this.imgs;
    }

    @Override
    protected Image[] genImages(ImageCacheKey key) throws IOException, BadElementException {
        LOGGER.debug("create SEAL IMAGES");
        boolean isVertical = true;
        final SealInfo info = this.infos;
        final String bindingType = info.getBinding();
        final BufferedImage bimgSeal = getSealImg(info);
        if (info.getTypeImg() == 1) {// 圖
            if ("E".equalsIgnoreCase(bindingType) || "W".equalsIgnoreCase(bindingType)) {
                isVertical = false;
            }
            return cutSeal(bimgSeal, isVertical, key, info);
        } else if (info.getTypeImg() == 2) {// 字
            return cutSeal(bimgSeal, isVertical, key, info);
        }
        return new Image[0];
    }

    //====
    //== [Method] Block Stop 
    //================================================

}
