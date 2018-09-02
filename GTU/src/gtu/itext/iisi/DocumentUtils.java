package gtu.itext.iisi;

import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tw.gov.moi.common.SystemConfig;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class DocumentUtils {

    //================================================
    //== [static variables] Block Start
    //====

    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DocumentUtils.class);

    final static public DocumentUtils CM_TL = new DocumentUtils(Coordinate.TOP_LEFT_CM);

    final static public DocumentUtils CM_BL = new DocumentUtils(Coordinate.CM);

    final static public DocumentUtils Default = new DocumentUtils(Coordinate.Default);

    final static public DocumentUtils COGNOS = new DocumentUtils(Coordinate.COGNOS);

    final static public DocumentUtils COGNOS_90 = new DocumentUtils(Coordinate.COGNOS_90);

    public static class ScaleStrategy {
        static interface IStrategy {
            public void processImage(Image image, float w, float h);
        }

        enum Strategy implements IStrategy {
            Actual {
                @Override
                public void processImage(Image image, float w, float h) {
                };
            }//
            ,
            FIXED {
                @Override
                public void processImage(Image image, float w, float h) {
                    image.scaleAbsolute(w, h);
                };
            }//
            ,
            FIT {
                @Override
                public void processImage(Image image, float w, float h) {
                    image.scaleToFit(w, h);
                };
            }//
            ,
            FIT_ReduceOnly {
                @Override
                public void processImage(Image image, float w, float h) {
                    if (image.getWidth() > w || image.getHeight() > h) {
                        image.scaleToFit(w, h);
                    }
                };
            }//
            ;
        }

        final Strategy stragegy;

        final float w;

        final float h;

        /** 原始圖檔大小. */
        final public static ScaleStrategy ACTUAL = new ScaleStrategy(Strategy.Actual, -1, -1);

        private ScaleStrategy(Strategy stragegy, float w, float h) {
            super();
            this.stragegy = stragegy;
            this.w = w;
            this.h = h;
        }

        /** 固定為指定大小 */
        public static ScaleStrategy fixed(float w, float h) {
            final ScaleStrategy scaleStrategy = new ScaleStrategy(Strategy.FIXED, w, h);
            return scaleStrategy;
        }

        /** 依比例縮放為指定大小 */
        public static ScaleStrategy fit(float w, float h) {
            final ScaleStrategy scaleStrategy = new ScaleStrategy(Strategy.FIT, w, h);
            return scaleStrategy;
        }

        /** 依比例縮小為指定大小 (不放大) */
        public static ScaleStrategy fitReduceOnly(float maxWidth, float maxHeight) {
            final ScaleStrategy scaleStrategy = new ScaleStrategy(Strategy.FIT_ReduceOnly, maxWidth, maxHeight);
            return scaleStrategy;
        }

        public void processScale(Image image, Coordinate coordinate) {
            this.stragegy.processImage(image, coordinate.transWidth(this.w), coordinate.transHeight(this.h));
        }
    }

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    /** 座標系轉換. */
    public final Coordinate coordinate;

    @Autowired
    private transient SystemConfig systemConfig;

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

    public DocumentUtils(Coordinate coord) {
        super();
        this.coordinate = coord;
        this.systemConfig = AeBean.INSTANCE.getSystemConfig();
    }

    public CHTFontFactory getFontFactory(SystemConfig systemConfig) {
        if (this.systemConfig == null) {
            this.systemConfig = systemConfig;
        }
        final CHTFontFactory factory = CHTFontFactory.RISFont.SUNG.getFactory(this.systemConfig);
        return ObjectUtils.defaultIfNull(factory, CHTFontFactory.Default);
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

    /**
     * Returns the height of the rectangle.
     */
    public float getPageHeight(Rectangle pageSize) {
        return pageSize.getHeight();
    }

    /**
     * Returns the height of the rectangle.
     */
    public float getPageWidth(Rectangle pageSize) {
        return pageSize.getWidth();
    }

    /**
     * @return the coordinate
     */
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

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
    //## [Method] sub-block : 形狀大小設定
    //####################################################################

    /**
     * 設定圖片大小;
     */
    public void setAbsoluteSize(com.lowagie.text.Image image, float w, float h) {
        final float nw = this.coordinate.transWidth(w);
        final float nh = this.coordinate.transHeight(h);
        image.scaleAbsolute(nw, nh);
    }

    //####################################################################
    //## [Method] sub-block : 線條、方框
    //####################################################################

    /**
     * 在指定位置輸出方框, 用於測試輸出定位正確性.
     */
    public void drawBlock(PdfContentByte contentByte, float x1, float y1, float x2, float y2) {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float float1 = this.coordinate.transPosition(pageSize, x1, y1);
        Float float2 = this.coordinate.transPosition(pageSize, x2, y2);
        //System.out.println(pageSize);
        //System.out.println(float1);
        //System.out.println(float2);
        contentByte.moveTo(float1.x, float1.y);
        contentByte.lineTo(float1.x, float2.y);
        contentByte.lineTo(float2.x, float2.y);
        contentByte.lineTo(float2.x, float1.y);
        contentByte.lineTo(float1.x, float1.y);
        contentByte.stroke();
    }

    /**
     * 連續線段
     * 
     * @param contentByte
     * @param pageSize
     * @param point0
     * @param points
     */
    public void drawLines(PdfContentByte contentByte, Float point0, Float... points) {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float f0 = this.coordinate.transPosition(pageSize, point0.x, point0.y);
        contentByte.moveTo(f0.x, f0.y);
        for (Float point : points) {
            Float f1 = this.coordinate.transPosition(pageSize, point.x, point.y);
            contentByte.lineTo(f1.x, f1.y);
        }
        contentByte.stroke();
    }

    //####################################################################
    //## [Method] sub-block : 文字輸出. 
    //####################################################################

    /**
     * 在指定位置輸出文字, 參考點:文字區塊左下角
     * 
     * 以下語法應於外部自行呼叫使用.
     * 
     * contentByte.beginText(); contentByte.setFontAndSize(fontFactory.getChineseBaseFont(), 14); contentByte.endText();
     * 
     */
    public void drawTextAlignLeft(PdfContentByte contentByte, String text, float x, float y) {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float p = this.coordinate.transPosition(pageSize, x, y);
        contentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, text, p.x, p.y, 0);
    }

    /**
     * 在指定位置輸出文字, 參考點:文字區塊左下角 以下語法應於外部自行呼叫使用. contentByte.beginText();
     * contentByte.setFontAndSize(fontFactory.getChineseBaseFont(), 14); contentByte.endText();
     * 
     * @param rotation
     * 
     */
    public void drawTextAlignLeft(PdfContentByte contentByte, String text, float x, float y, float rotation) {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float p = this.coordinate.transPosition(pageSize, x, y);
        contentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, text, p.x, p.y, rotation);
    }

    /**
     * 在指定位置輸出文字, 參考點:文字區塊中央下方 以下語法應於外部自行呼叫使用. contentByte.beginText();
     * contentByte.setFontAndSize(fontFactory.getChineseBaseFont(), 14); contentByte.endText();
     */
    public void drawText(PdfContentByte contentByte, String text, float x, float y) {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float p = this.coordinate.transPosition(pageSize, x, y);
        contentByte.showTextAligned(PdfContentByte.ALIGN_CENTER, text, p.x, p.y, 0);
    }

    /**
     * 在指定位置輸出文字, 參考點:文字區塊中央下方 以下語法應於外部自行呼叫使用. contentByte.beginText();
     * contentByte.setFontAndSize(fontFactory.getChineseBaseFont(), 14); contentByte.endText();
     */
    public void drawText(PdfContentByte contentByte, String text, float x, float y, float rotation) {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float p = this.coordinate.transPosition(pageSize, x, y);
        contentByte.showTextAligned(PdfContentByte.ALIGN_CENTER, text, p.x, p.y, rotation);
    }

    /**
     * 在指定區塊輸出文字.
     * 
     * @param contentByte
     * @param chineseFont
     * @param text
     * @param x1 區塊左上座標，以頁面左上角為原點
     * @param y1 區塊左上座標，以頁面左上角為原點
     * @param x2 區塊右下座標，以頁面左上角為原點
     * @param y2 區塊右下座標，以頁面左上角為原點
     * @throws DocumentException
     */
    public void drawTextBlock(PdfContentByte contentByte, Font chineseFont, String text, float x1, float y1, float x2, float y2)
            throws DocumentException {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float p1 = this.coordinate.transPosition(pageSize, x1, y1);
        Float p2 = this.coordinate.transPosition(pageSize, x2, y2);
        final ColumnText columnText = new ColumnText(contentByte);
        columnText.setSimpleColumn(p1.x, p1.y, p2.x, p2.y, 10, Element.ALIGN_MIDDLE);
        columnText.setLeading(2, 1.0f);
        columnText.addText(new Chunk(text, chineseFont));
        columnText.go();
    }

    public void drawTextBlock(PdfContentByte contentByte, Phrase phrase, float x1, float y1, float x2, float y2) throws DocumentException {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float p1 = this.coordinate.transPosition(pageSize, x1, y1);
        Float p2 = this.coordinate.transPosition(pageSize, x2, y2);
        final ColumnText columnText = new ColumnText(contentByte);
        columnText.setSimpleColumn(p1.x, p1.y, p2.x, p2.y, 10, Element.ALIGN_MIDDLE);
        columnText.setLeading(2, 1.0f);
        columnText.addText(phrase);
        columnText.go();
    }

    //####################################################################
    //## [Method] sub-block : table 輸出. 
    //####################################################################

    /**
     * 在指定位置輸出文字, 參考點:文字區塊中央下方 以下語法應於外部自行呼叫使用. contentByte.beginText();
     * contentByte.setFontAndSize(fontFactory.getChineseBaseFont(), 14); contentByte.endText();
     */
    public void drawTable(PdfContentByte contentByte, TableiText table, float x, float y) {
        Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        Float p = this.coordinate.transPosition(pageSize, x, y);
        table.getTable().writeSelectedRows(0, -1, p.x, p.y, contentByte);
    }

    //####################################################################
    //## [Method] sub-block : Barcode 輸出. 
    //####################################################################

    private Image createBarcode128(final PdfContentByte contentByte, final String text, float w, float h, boolean showText) {
        Barcode128 barcode = new Barcode128();
        barcode.setCode(text);
        barcode.setStartStopText(false);
        if (showText) {
            barcode.setFont(CHTFontFactory.Default.getBaseFont());
            barcode.setSize(10);
            barcode.setBaseline(12);
        } else {
            barcode.setFont(null);
        }
        Image imgBarCode = barcode.createImageWithBarcode(contentByte, null, null);
        if (w < 0 || h < 0) {
        } else {
            this.setAbsoluteSize(imgBarCode, w, h);
        }
        return imgBarCode;
    }

    public void drawBarcode128(final PdfContentByte contentByte, final String text, float x, float y, float w, float h) throws IOException,
            DocumentException {
        drawBarcode128(contentByte, text, x, y, w, h, false);
    }

    public void drawBarcode128(final PdfContentByte contentByte, final String text, float x, float y, float w, float h, boolean showText)
            throws IOException, DocumentException {

        LOGGER.debug("draw Barcode 128");
        final Image imgBarcode = createBarcode128(contentByte, text, w, h, showText);
        final Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        final Float p = this.coordinate.transPosition(pageSize, x, y);
        imgBarcode.setAbsolutePosition(p.x, p.y);
        contentByte.addImage(imgBarcode);
    }

    //####################################################################
    //## [Method] sub-block : Barcode 輸出. 
    //####################################################################

    private Image createBarcode39(final PdfContentByte contentByte, final String text, float w, float h, boolean showText) {
        Barcode39 code39 = new Barcode39();
        code39.setExtended(true);
        code39.setCode(text);
        code39.setStartStopText(false);
        if (showText) {
            code39.setFont(CHTFontFactory.Default.getBaseFont());
            code39.setSize(10);
            code39.setBaseline(12);
        } else {
            code39.setFont(null);
        }
        Image image39 = code39.createImageWithBarcode(contentByte, null, null);
        if (w > 0 && h > 0) {
            this.setAbsoluteSize(image39, w, h);
        }
        return image39;
    }

    @SuppressWarnings("unused")
    private void addBarcode39(PDFDocument pdfDoc, String value, float w, float h) throws DocumentException {
        PdfWriter pdfWriter = pdfDoc.getPdfWriter();
        PdfContentByte cb = pdfWriter.getDirectContent();
        Image image39 = createBarcode39(cb, value, w, h, false);
        pdfDoc.getDocument().add(image39);
    }

    public void drawBarcode39(final PdfContentByte contentByte, final String text, float x, float y, float w, float h) throws IOException,
            DocumentException {
        drawBarcode39(contentByte, text, x, y, w, h, false);
    }

    public void drawBarcode39(final PdfContentByte contentByte, final String text, float x, float y, float w, float h, boolean showText)
            throws IOException, DocumentException {
        LOGGER.debug("draw Barcode 39");
        final Image image39 = createBarcode39(contentByte, text, w, h, showText);
        final Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        final Float p = this.coordinate.transPosition(pageSize, x, y);
        image39.setAbsolutePosition(p.x, p.y);
        contentByte.addImage(image39);
    }

    //####################################################################
    //## [Method] sub-block : 圖片輸出. 
    //####################################################################

    public void drawImageAlignLeft(PdfContentByte contentByte, final File imgFile, float x, float y) throws IOException, DocumentException {
        drawImageAlignLeft(contentByte, imgFile, x, y, ScaleStrategy.ACTUAL);
    }

    /**
     * 座標原點為頁面左上， 在指定位置輸出圖片，圖片以左下為起點。
     * 
     * @param stragegy 圖片縮放模式
     * @see ScaleStrategy
     */
    public void drawImageAlignLeft(PdfContentByte contentByte, final File imgFile, float x, float y, final ScaleStrategy stragegy)
            throws IOException, DocumentException {

        LOGGER.debug("load img:{}", imgFile);
        final BufferedImage before = javax.imageio.ImageIO.read(imgFile);
        final Image image = Image.getInstance(before, null);
        final Rectangle pageSize = contentByte.getPdfWriter().getPageSize();
        final Float p = this.coordinate.transPosition(pageSize, x, y);
        image.setAbsolutePosition(p.x, p.y);
        stragegy.processScale(image, this.coordinate);
        contentByte.addImage(image);
    }

    //====
    //== [Method] Block Stop 
    //================================================
}
