/*
 * Copyright (c) 2009. 資拓科技. All right reserved.
 */
package gtu.itext.iisi.data;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/***
 * 輸出條碼資料，目前僅適用於 PDF 格式檔案.
 * 
 * @author 920111 在 2009/9/29 建立
 */
public class BarcodeImage extends CellBeanProperty implements CellDataSource {

    public static enum CodeType {
        /** Barcode39 */
        CODE39;
    }

    private CodeType codeType;

    /** 圖片高度 */
    private float height;

    /** 圖片寬度 */
    private float width;

    public BarcodeImage(final CodeType codeType, final String propertyName) {
        super(propertyName);
        this.codeType = codeType;
    }

    public CodeType getCodeType() {
        return this.codeType;
    }

    public final Image toImage(final PdfWriter pdfWriter, final String text) {
        if (this.codeType == CodeType.CODE39) {
            final PdfContentByte cb = pdfWriter.getDirectContent();

            final Barcode39 code39 = new Barcode39();
            code39.setCode(text);
            code39.setStartStopText(false);

            final Image image39 = code39.createImageWithBarcode(cb, null, null);
            if (this.height > 0) {
                image39.scaleAbsoluteHeight(this.height);
            }
            if (this.width > 0) {
                image39.scaleAbsoluteWidth(this.width);
            }

            return image39;
        }
        return null;
    }

    /**
     * 設定 圖片高度 和 圖片寬度
     * 
     * @param height 圖片高度
     * @param width 圖片寬度
     */
    public void setHeightAndWidth(float height, float width) {
        this.height = height;
        this.width = width;
    }

    /**
     * 設定 圖片高度
     * 
     * @param height 圖片高度
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * 設定 圖片寬度
     * 
     * @param width 圖片寬度
     */
    public void setWidth(float width) {
        this.width = width;
    }

}
