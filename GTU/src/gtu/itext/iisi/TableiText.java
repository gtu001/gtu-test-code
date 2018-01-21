package gtu.itext.iisi;

import gtu.itext.iisi.CHTFontFactory.RISFont;
import gtu.itext.iisi.table.CellFormat;
import gtu.itext.iisi.table.CellFormat.FontType;
import gtu.itext.iisi.table.ColumnMetadata;
import gtu.itext.iisi.table.NormalTableMetadata;

import java.awt.Color;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class TableiText {

    public static class InnerTable extends TableiText {

        final TableiText parentTable;

        /**  */
        final int numColumns;

        public InnerTable(PDFDocument docBuilder, TableiText parentTable, float[] widths) throws DocumentException {
            super(docBuilder, widths);
            this.numColumns = widths.length;
            this.parentTable = parentTable;
        }

        public InnerTable(PDFDocument docBuilder, TableiText parentTable, int numColumns) {
            super(docBuilder, numColumns);
            PdfPCell cell = new PdfPCell(this.realTable);
            cell.setColspan(numColumns);
            this.numColumns = numColumns;
            this.parentTable = parentTable;
        }

        /**
         * @see tw.gov.moi.ae.report.itext2.TableiText#appendMe()
         */
        @Override
        public void appendMe() throws DocumentException {
            this.append(this.numColumns);
        }

        /**
         * @see tw.gov.moi.ae.report.itext2.TableiText#appendMe()
         */
        public void appendMe(int colspan) throws DocumentException {
            this.append(colspan);
        }

        private void append(int colspan) {
            PdfPCell cell = new PdfPCell(this.realTable);
            cell.setColspan(colspan);
            if (this.parentTable.defaultCellFormat != null) {
                CellFormat cellFormat = this.parentTable.defaultCellFormat;
                if (cellFormat.getMinimumHeight() != null) {
                    cell.setMinimumHeight(Coordinate.CM.transHeight(cellFormat.getMinimumHeight()));
                }
                if (cellFormat.getBorder() >= 0) {
                    final int border = cellFormat.getBorder();
                    cell.setBorder(border);
                    if (border == 0) {
                        cell.setBorderWidth(0);
                    }
                }
            } else {
                cell.setBorder(CellFormat.RECTANGLE_NO_BORDER);
            }

            this.parentTable.realTable.addCell(cell);
        }

    }

    //================================================
    //== [static variables] Block Start
    //====
    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    private boolean modified = false;

    final protected PDFDocument docBuilder;

    private CellFormat defaultCellFormat;

    protected PdfPTable realTable;

    protected float[] widths;

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

    /**
     * 建立表格, 僅定義欄數.
     */
    public TableiText(PDFDocument docBuilder, int numColumns) {
        this.realTable = new PdfPTable(numColumns);
        this.docBuilder = docBuilder;
        this.defaultCellFormat = new CellFormat().setFontSize(12);
        this.realTable.setSpacingBefore(3);
        this.widths = new float[numColumns];
        Arrays.fill(this.widths, 1);
    }

    /**
     * 建立表格, 僅定義欄數.
     * 
     * @throws DocumentException
     */
    public TableiText(PDFDocument docBuilder, float[] widths) throws DocumentException {
        this.realTable = new PdfPTable(ArrayUtils.getLength(widths));
        this.docBuilder = docBuilder;
        this.defaultCellFormat = new CellFormat().setFontSize(12);
        this.realTable.setWidths(widths);
        this.widths = widths;
        // 2. 上方留白 
        this.realTable.setSpacingBefore(1);
    }

    /**
     * 建立表格, 使用TableMetadata格式設定.
     */
    public TableiText(PDFDocument docBuilder, NormalTableMetadata tableMeta) throws DocumentException {
        this(docBuilder, tableMeta.getColsNumber());
        initiateTable(null, tableMeta);
        this.defaultCellFormat = tableMeta.getDefaultContentFormat();
    }

    /**
     * 初始化表格當中的外觀參數.
     * 
     * @param table
     * @param tableMeta
     * @throws DocumentException
     */
    private void initiateTable(PDFDocument pdfDocBuilder, NormalTableMetadata tableMeta) throws DocumentException {
        // 0. 對齊：
        this.realTable.setHorizontalAlignment(tableMeta.getHorizontalAlignment());

        // 1. 表格寬度比例設定 : 
        this.realTable.setWidthPercentage(tableMeta.getWidth());
        this.realTable.setWidths(tableMeta.getWidths());
        this.widths = tableMeta.getWidths();

        // 2. 上方留白 
        this.realTable.setSpacingBefore(tableMeta.getOffset());
    }

    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (toString/equals+hashCode)
    //====

    /**
     * 僅新增文字時，強制使用預訯中文字型.
     * 
     * @throws DocumentException
     */
    public void addCell(String text) throws DocumentException {
        addCell(text, 1);
    }

    /**
     * 僅新增文字時，強制使用預訯中文字型，並可設定 colspan.
     * 
     * @see com.lowagie.text.pdf.PdfPTable#addCell(java.lang.String)
     */
    public void addCell(String text, int colspan) throws DocumentException {
        this.addCell(text, colspan, this.defaultCellFormat);
    }

    public void addCell(PdfPCell cell) {
        this.realTable.addCell(cell);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPTable#addCell(com.lowagie.text.Paragraph)
     */
    public void addCell(Paragraph p, int colspan, boolean isNoBorder) {
        PdfPCell ncell = new PdfPCell(p);
        if (isNoBorder) {
            ncell.setBorder(0);
            ncell.setBorderWidth(0);
        }
        ncell.setColspan(colspan);
        this.realTable.addCell(ncell);
    }

    /**
     * 僅新增文字時，強制使用預訯中文字型/或標楷體(cellFormat.setFontName("標楷體"))，並可設定colspan.
     * 
     * @throws DocumentException
     * @see com.lowagie.text.pdf.PdfPTable#addCell(java.lang.String)
     */
    public void addCell(String text, int colspan, CellFormat cellFormat, SubPhrase... subPhrases) throws DocumentException {
        PdfPCell cell = createCell(this.docBuilder, text, cellFormat, subPhrases);
        cell.setColspan(colspan);
        this.realTable.addCell(cell);
    }

    

    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Accessor] Block Start
    //====

    public void setModified() {
        this.modified = true;
    }

    public boolean isModified() {
        return this.modified;
    }

    /**
     * @return the realTable
     */
    public PdfPTable getTable() {
        return this.realTable;
    }

    /**
     * @param defaultCellFormat the defaultCellFormat to set
     */
    public void setDefaultFormat(CellFormat cellFormat) {
        this.defaultCellFormat = cellFormat;
    }

    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====

    //####################################################################
    //## [Static Methods] sub-block : Cell 生成. 
    //####################################################################

    /**
     * 以指定的格式產式標題儲存格.
     * 
     * @param value
     * @param cm
     * @return
     * @throws DocumentException
     */
    static public PdfPCell createHeaderCell(PDFDocument pdfDocBuilder, String value, ColumnMetadata cm) throws DocumentException {
        return createCell(pdfDocBuilder, value, cm.getHeaderFormat());
    }

    /**
     * 以指定的格式產式內容儲存格.
     * 
     * @param value
     * @param cm
     * @return
     * @throws DocumentException
     */
    static public PdfPCell createContentCell(PDFDocument pdfDocBuilder, String value, ColumnMetadata cm) throws DocumentException {
        return createCell(pdfDocBuilder, value, cm.getContentFormat());
    }

    /**
     * 產生儲存格.
     * 
     * @param value
     * @param fontsize
     * @param border
     * @param borderWidth
     * @param alignH
     * @param alignV
     * @return
     * @throws DocumentException
     */
    static public PdfPCell createCell(PDFDocument pdfDocBuilder, String value, CellFormat cellFormat, SubPhrase... subPhrases)
            throws DocumentException {

        if (subPhrases == null || subPhrases.length == 0) {
            value = StringUtils.defaultIfEmpty(value, "　");
        }
        final int fontsizeBase = pdfDocBuilder.getFontInfo().size;
        final int fontSize = cellFormat.getFontSizeIfNotPositive(fontsizeBase); // 若未設定，使用基準大小 
        final CHTFontFactory fontFactory;
        final FontType fontName = cellFormat.getFontName();
        switch (fontName) {
            case 新細明體:
                fontFactory = CHTFontFactory.MINGLIU;
                break;
            case 標楷體:
                fontFactory = CHTFontFactory.KAIU;
                break;
            case 戶役政楷體:
                fontFactory = RISFont.KAI.getFactory(AeBean.INSTANCE.getSystemConfig());
                break;
            case Default:
            case 戶役政宋體:
            default:
                fontFactory = RISFont.SUNG.getFactory(AeBean.INSTANCE.getSystemConfig());
                break;
        }
        final Set<FontStyle> style;
        if (cellFormat.isFontBold()) {
            style = EnumSet.of(FontStyle.BOLD);
        } else {
            style = EnumSet.of(FontStyle.NORMAL);
        }

        //        final Paragraph phrase = new Paragraph(value, fontFactory.getFont(fontSize, style, Color.BLACK));
        final RisFontSelector fontSelector = fontFactory.getFontSelector(fontSize, style, Color.BLACK);
        final Paragraph paragraph = fontSelector.process(value, subPhrases);
        paragraph.setSpacingAfter(0);
        paragraph.setSpacingBefore(0);

        final PdfPCell c = new PdfPCell(paragraph);
        c.setLeading(2f, 1.0f);
        c.setBackgroundColor(cellFormat.getBackgroundColor());
        c.setPaddingTop(cellFormat.getPaddingV() - 3);
        c.setPaddingBottom(cellFormat.getPaddingV() + 3);
        c.setPaddingRight(cellFormat.getPaddingR());
        c.setPaddingLeft(cellFormat.getPaddingL());

        if (cellFormat.getMinimumHeight() != null) {
            final Float minimumHeight = cellFormat.getMinimumHeight();
            c.setMinimumHeight(Coordinate.TOP_LEFT_CM.transHeight(minimumHeight));
        }

        // TODO [無期限][時機：(公用函式) 調整輔助套件實作時] 
        // AlignH 等代碼目前直接採用iText中的定義值，但仍然應該執行轉換操作才可使用，或日後改為 enum 避免誤用。或

        if (cellFormat.getAlignH() >= 0) {
            c.setHorizontalAlignment(cellFormat.getAlignH());
        }
        if (cellFormat.getAlignV() >= 0) {
            c.setVerticalAlignment(cellFormat.getAlignV());
        }
        if (cellFormat.getBorderWidth() >= 0) {
            c.setBorderWidth(cellFormat.getBorderWidth());
        }
        if (cellFormat.getBorder() >= 0) {
            final int border = cellFormat.getBorder();
            c.setBorder(border);
            if (border == 0) {
                c.setBorderWidth(0);
            }
        }
        return c;
    }

    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====
    //####################################################################
    //## sub-block : XXXX(可依功能區分) 
    //####################################################################

    public void appendMe() throws DocumentException {
        this.docBuilder.add(this.realTable);
    }

    //####################################################################
    //## sub-block : XXXX(可依功能區分) 
    //####################################################################

    /**
     * 以指定格式，新增標題儲存格.
     * 
     * @param value
     * @param cm
     * @return
     * @throws DocumentException
     */
    public void addHeaderCell(String value, ColumnMetadata cm) throws DocumentException {
        this.realTable.addCell(createCell(this.docBuilder, value, cm.getHeaderFormat()));
    }

    /**
     * 以指定格式，新增內容儲存格.
     */
    public void addContentCell(String value, ColumnMetadata cm) throws DocumentException {
        this.realTable.addCell(createCell(this.docBuilder, value, cm.getContentFormat()));
    }

    //####################################################################
    //## sub-block : InnerTable
    //####################################################################

    /**
     * 建立子表格
     */
    public InnerTable createInnerTable(float[] subWidth) throws DocumentException {
        InnerTable innerTable = new TableiText.InnerTable(this.docBuilder, this, subWidth);
        return innerTable;
    }

    /**
     * 建立子表格
     */
    public InnerTable createInnerTable(int leftCols, int rightCols) throws DocumentException {
        float[] wid = this.widths;
        final int size = wid.length - leftCols - rightCols;
        final float[] subWidth = ArrayUtils.subarray(wid, leftCols, leftCols + size);
        InnerTable innerTable = new TableiText.InnerTable(this.docBuilder, this, subWidth);
        return innerTable;
    }

    //    private void addCell(TableiText innerTable, int colspan) {
    //        addCell(innerTable, colspan, true);
    //    }

    //====
    //== [Method] Block Stop 
    //================================================
    //== [Main Method] Block Start
    //====
    //public static void main(String[] args) {
    //    
    //}
    //====
    //== [Main Method] Block Stop 
    //================================================

}
