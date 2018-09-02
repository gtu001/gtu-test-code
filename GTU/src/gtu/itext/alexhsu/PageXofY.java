package gtu.itext.alexhsu;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Alex
 */
public class PageXofY extends PdfPageEventHelper {

    protected PdfTemplate total;
    protected BaseFont helv;
    private String fontFilePath = "C:\\WINDOWS\\Fonts\\KAIU.TTF";

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            if (helv == null) {
                helv = BaseFont.createFont(fontFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
        } catch (DocumentException ex) {
            Logger.getLogger(PageXofY.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PageXofY.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        String text = "第" + writer.getPageNumber() + "頁，共";
        float textBase = 570;
        float textSize = helv.getWidthPoint(text, 12);
        cb.beginText();
        cb.setFontAndSize(helv, 12);
        float adjust = helv.getWidthPoint("000頁", 12);
        cb.setTextMatrix(document.right() - textSize - adjust, textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(total, document.right() - adjust, textBase);
        cb.restoreState();
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        total.beginText();
        total.setFontAndSize(helv, 12);
        total.setTextMatrix(0, 0);
        total.showText(String.valueOf(writer.getPageNumber() - 1) + "頁");
        total.endText();

    }

    /**
     * 設定字型檔
     * 
     * @param samplefont
     *            字型檔物件
     */
    public void setFont(BaseFont sampleFont) {
        this.helv = sampleFont;
    }
}
