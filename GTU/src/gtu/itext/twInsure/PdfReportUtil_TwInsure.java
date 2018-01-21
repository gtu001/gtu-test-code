/*
 * 在 2006/12/19 建立
 *
 * 若要變更這個產生的檔案的範本，請移至
 * 視窗 > 喜好設定 > Java > 程式碼產生 > 程式碼和註解
 */
package gtu.itext.twInsure;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author 台灣人壽
 */
public class PdfReportUtil_TwInsure {

    public PdfReportUtil_TwInsure() {
    }

    public static void main(String[] args) {
        PdfReportUtil_TwInsure util = new PdfReportUtil_TwInsure();
        System.out.println("done...");
    }

    private static final BaseFont bfChinese;
    static {
        try {
            bfChinese = BaseFont.createFont("/resource/fonts/kaiu.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 回傳由PdfWriter產生的ContentByte
     * 
     * @return
     */
    public PdfContentByte getPdfContetByte(PdfWriter writer) {
        return writer.getDirectContent();
    }

    /**
     * 取得PdfTemplate
     * 
     * @param width
     *            : 欲建立之template 的寬
     * @param height
     *            : 欲建立之template 的高
     * @return
     */
    public PdfTemplate getPdfTemplate(PdfContentByte contentByte, float width, float height) {
        return contentByte.createTemplate(width, height);
    }

    public void createPageText(PdfWriter writer, PdfTemplate template, PdfContentByte contentByte, float pageX, float pageY, String pageText1, String pageText2, int fontsize, float twidth, float theight) {
        if (contentByte == null)
            contentByte = getPdfContetByte(writer);
        if (template == null)
            template = getPdfTemplate(contentByte, twidth, theight);

        String text = pageText1 + writer.getPageNumber() + pageText2;

        float len = bfChinese.getWidthPoint(text, fontsize);
        contentByte.beginText();
        contentByte.setFontAndSize(bfChinese, fontsize);
        contentByte.setTextMatrix(pageX, pageY);
        contentByte.showText(pageText1);
        contentByte.showText(String.valueOf(writer.getPageNumber()));
        contentByte.showText(pageText2);
        contentByte.endText();
        contentByte.addTemplate(template, pageX + len, pageY - 10f);
    }

    /**
     * 產生 (第幾頁共幾頁的東東) 要配合createPageText 最後會將總頁數填回 *****注意**** 若無
     * document.newpage會有無法取得總頁數的情況
     * 
     * @param pageText
     * @param fontsize
     */
    public void closePageText(PdfTemplate template, String pageText, float fontsize) {
        template.beginText();
        /* init Font set Font Type */
        template.setFontAndSize(bfChinese, fontsize);
        template.setTextMatrix(0f, 10f);
        template.showText(pageText);
        template.endText();
    }


    /**
     * 換頁
     */
    public void newPage(Document document) throws DocumentException {
        document.newPage();
    }
    
    //=================================================================================================================================

    /**
     * 建立 Table
     * 
     * @param columnSize
     *            欄位數目
     * @param vAlignment
     *            垂直對齊方式
     * @return 所建立之 Table
     * @throws Exception
     */
    public Table createTable(int columnSize, int vAlignment) throws Exception {
        Table table = new Table(columnSize);
        table.setOffset(-12);
        table.setSpacing(3f);
        table.setPadding(0.5f);
        table.setBorderWidth(0);
        // table.setDefaultVerticalAlignment(vAlignment);
        table.setWidth(100);
        return table;
    }
    
    public enum FontType {
        NORMAL(Font.NORMAL), //
        BOLD(Font.BOLD), //
        ITALIC(Font.ITALIC), //
        UNDERLINE(Font.UNDERLINE),//
        ;
        final int val;

        FontType(int val) {
            this.val = val;
        }
    }

    public static class PColumnAppender {
        private PColumnAppender() {
        }

        public static PColumnAppender newInstance() {
            return new PColumnAppender();
        }

        private PdfPTable table;
        private int icolspan;
        private int padding;
        private String strValue;
        private Font font;
        private int borderWidth;
        private int hAlign;
        private Phrase phrase;
        private int vAlign;
        private int rectangle;

        public PColumnAppender table(PdfPTable table) {
            this.table = table;
            return this;
        }

        public PColumnAppender icolspan(int icolspan) {
            this.icolspan = icolspan;
            return this;
        }

        public PColumnAppender padding(int padding) {
            this.padding = padding;
            return this;
        }

        public PColumnAppender strValue(String strValue) {
            this.strValue = strValue;
            return this;
        }

        public PColumnAppender font(Font font) {
            this.font = font;
            return this;
        }

        public PColumnAppender borderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
            return this;
        }

        public PColumnAppender hAlign(int hAlign) {
            this.hAlign = hAlign;
            return this;
        }

        public PColumnAppender phrase(Phrase phrase) {
            this.phrase = phrase;
            return this;
        }

        public PColumnAppender vAlign(int vAlign) {
            this.vAlign = vAlign;
            return this;
        }

        public PColumnAppender rectangle(int rectangle) {
            this.rectangle = rectangle;
            return this;
        }

        public void build() {
            try {
                if (phrase == null) {
                    PdfPCell pcell = new PdfPCell(new Phrase(font.getSize(), strValue, font));
                    pcell.setPadding(padding);
                    pcell.setBorderWidth(borderWidth);
                    pcell.setHorizontalAlignment(hAlign);
                    pcell.setVerticalAlignment(vAlign);
                    pcell.setBorder(rectangle);
                    if (icolspan > 1) {
                        pcell.setColspan(icolspan);
                    }
                    table.addCell(pcell);
                } else {
                    int horizontalAlignment = table.getDefaultCell().getHorizontalAlignment();
                    int verticalAlignment = table.getDefaultCell().getVerticalAlignment();
                    if (icolspan > 1) {
                        table.getDefaultCell().setColspan(icolspan);
                    }
                    table.getDefaultCell().setHorizontalAlignment(hAlign);
                    table.getDefaultCell().setVerticalAlignment(vAlign);
                    table.getDefaultCell().setBorder(rectangle);
                    table.addCell(phrase);
                    table.getDefaultCell().setHorizontalAlignment(horizontalAlignment);
                    table.getDefaultCell().setVerticalAlignment(verticalAlignment);
                    table.getDefaultCell().setColspan(1);
                    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static class ColumnAppender {

        private ColumnAppender() {
        }

        public static ColumnAppender newInstance() {
            return new ColumnAppender();
        }

        private Table table;
        private int icolspan;
        private int irowspan;
        private String strValue;
        private Font font;
        private int borderWidth;
        private int hAlignment;
        private int vAlignment;
        private int rectangle;

        public ColumnAppender table(Table table) {
            this.table = table;
            return this;
        }

        public ColumnAppender icolspan(int icolspan) {
            this.icolspan = icolspan;
            return this;
        }

        public ColumnAppender irowspan(int irowspan) {
            this.irowspan = irowspan;
            return this;
        }

        public ColumnAppender strValue(String strValue) {
            this.strValue = strValue;
            return this;
        }

        public ColumnAppender font(Font font) {
            this.font = font;
            return this;
        }

        public ColumnAppender borderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
            return this;
        }

        public ColumnAppender hAlignment(int hAlignment) {
            this.hAlignment = hAlignment;
            return this;
        }

        public ColumnAppender vAlignment(int vAlignment) {
            this.vAlignment = vAlignment;
            return this;
        }

        public ColumnAppender rectangle(int rectangle) {
            this.rectangle = rectangle;
            return this;
        }

        public void apply() {
            try {
                Cell cell = new Cell(new Phrase(font.getSize(), strValue, font));
                cell.setBorderWidth(borderWidth);
                cell.setHorizontalAlignment(hAlignment);
                cell.setVerticalAlignment(vAlignment);
                cell.setBorder(rectangle);
                if (icolspan > 1) {
                    cell.setColspan(icolspan);
                }
                if (irowspan > 1) {
                    cell.setRowspan(irowspan);
                }
                table.addCell(cell);
            } catch (BadElementException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class FontCreater {
        private FontCreater() {
        }

        public static FontCreater newInstance() {
            return new FontCreater();
        }

        int size;
        FontType fontType = FontType.NORMAL;

        public FontCreater size(int size) {
            this.size = size;
            return this;
        }

        public FontCreater fontType(FontType fontType) {
            this.fontType = fontType;
            return this;
        }

        public Font build() {
            return new Font(bfChinese, size, fontType.val);
        }
    }
}