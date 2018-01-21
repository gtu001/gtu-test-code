/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi;

import gtu.itext.iisi.PDFNormalTableTransfer.EventHandler;
import gtu.itext.iisi.TableiText.InnerTable;
import gtu.itext.iisi.data.BarcodeImage;
import gtu.itext.iisi.data.CellDataSource;
import gtu.itext.iisi.data.HorizontalExpression;
import gtu.itext.iisi.table.ColumnMetadata;
import gtu.itext.iisi.table.NormalTableMetadata;

import java.awt.Color;
import java.awt.geom.Point2D.Float;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import tw.gov.moi.util.DateUtil;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;

/**
 * 處理直欄式表格.
 * 
 * @author 920111 在 2008/3/13 建立
 */
public class PDFSimpleTableTransfer {

    /**
     * 
     * @author tsaicf
     */
    public class MyTableEvent implements PdfPTableEvent {
        @Override
        public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
            if (PDFSimpleTableTransfer.this.outerBorder) {
                drawOuterBorder(widths, heights, headerRows, canvases);
            }
        }

        private void drawOuterBorder(float[][] widths, float[] heights, int headerRows, PdfContentByte[] canvases) {
            int rows = widths.length;
            int columns = widths[rows - 1].length - 1;
            PdfContentByte cb = canvases[PdfPTable.BASECANVAS];
            final float lx = widths[0][0];
            final float uy = heights[0];
            final float rx = widths[rows - 1][columns];
            final float ly = heights[rows];
            Rectangle rect = new Rectangle(lx, uy, rx, ly);

            cb.setLineWidth(0.5f);
            float dx = 1;
            if (headerRows > 0) {
                // 有 Header///
                rect.setBackgroundColor(Color.YELLOW);
                DocumentUtils.Default.drawLines(cb//
                        , new Float(lx - dx, ly)//
                        , new Float(lx - dx, uy + dx)//
                        , new Float(rx + dx, uy + dx)//
                        , new Float(rx + dx, ly)//
                        );

            } else {

                float maxBottom = PDFSimpleTableTransfer.this.pdfDocument.getDocument().bottom() + 12;

                if (ly < maxBottom) {
                    rect.setBackgroundColor(Color.GREEN);
                    DocumentUtils.Default.drawLines(cb//
                            , new Float(lx - dx, uy)//
                            , new Float(lx - dx, ly - dx)//
                            , new Float(rx + dx, ly - dx)//
                            , new Float(rx + dx, uy)//
                            );
                } else {
                    DocumentUtils.Default.drawLines(cb//
                            , new Float(lx - dx, uy)//
                            , new Float(lx - dx, ly)//
                            );
                    DocumentUtils.Default.drawLines(cb//
                            , new Float(rx + dx, ly)//
                            , new Float(rx + dx, uy)//
                            );
                }
            }

            if (PDFSimpleTableTransfer.this.toBeClosed) {
                DocumentUtils.Default.drawLines(cb//
                        , new Float(lx - dx, uy)//
                        , new Float(lx - dx, ly - dx)//
                        , new Float(rx + dx, ly - dx)//
                        , new Float(rx + dx, uy)//
                        );
            }
        }
    }

    //================================================
    //== [static variables] Block Start
    //====

    final private static NumberFormat moneyFormat = new DecimalFormat("#,##0");

    //====
    //== [static variables] Block Stop
    //================================================
    //== [instance variables] Block Start
    //====
    protected NormalTableMetadata tableMeta;

    protected int colsNumber;

    protected ColumnMetadata[] colsMeta;

    private PDFDocument pdfDocument;

    private TableiText myTable;

    //####################################################################
    //## [instance variables] sub-block : 輸出位置相關資料
    //####################################################################

    /** 表格左上位置. */
    private short colStartIndex = 0;

    /** 表格左上位置. */
    @SuppressWarnings("unused")
    private int rowStartIndex = 0;

    /** 目前處理直行位置 */
    private short colIndex = 0;

    /** 目前處理橫列位置 */
    private int rowIndex = 0;

    /** 目前資料數 */
    private int dataCount = 0;

    /** 最大資料數 */
    private int maxDataCount = 10000;

    private boolean toBeClosed = false;

    private boolean outerBorder = false;

    //####################################################################
    //## [instance variables] sub-block : 自訂事件處理.
    //####################################################################

    final private SimpleDateFormat formatterYYMMDD = new SimpleDateFormat("yyyy/MM/dd");

    /** */
    private EventHandler eventHandler;

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
     * 初始表格轉換物件.
     * 
     * 建立 PdfPTable
     * 
     * @throws DocumentException
     */
    public PDFSimpleTableTransfer(PDFDocument pdfDocument, NormalTableMetadata tableMeta) throws DocumentException {
        this.pdfDocument = pdfDocument;
        this.tableMeta = tableMeta;
        initial();
    }

    private void initial() {
        final List<ColumnMetadata> dataColumns = this.tableMeta.getDataColumns(); // 取得所有資料欄位定義.
        this.colsNumber = dataColumns.size();
        this.colsMeta = dataColumns.toArray(new ColumnMetadata[0]);
    }

    //====
    //== [Constructors] Block Stop
    //================================================
    //== [Overrided Method] Block Start (toString/equals+hashCode)
    //====
    //====
    //== [Overrided Method] Block Stop
    //================================================
    //== [Accessor] Block Start
    //====

    /**
     * @param outerBorder the outerBorder to set
     */
    public void setOuterBorder(boolean outerBorder) {
        this.outerBorder = outerBorder;
    }

    /**
     * @param maxDataCount the maxDataCount to set
     */
    public void setMaxDataCount(int maxDataCount) {
        this.maxDataCount = maxDataCount;
    }

    //====
    //== [Accessor] Block Stop
    //================================================
    //== [Static Method] Block Start
    //====

    /**
     * Template Method.
     * 
     * @return
     * 
     * @throws DocumentException
     */
    final public static TableiText createTable(PDFDocument pdfDocument, NormalTableMetadata tableMeta) throws DocumentException {

        PDFSimpleTableTransfer transfer = new PDFSimpleTableTransfer(pdfDocument, tableMeta);
        final List<ColumnMetadata> allColumns = transfer.tableMeta.getAllColumns();
        for (ColumnMetadata columnMetadata : allColumns) {
            if (columnMetadata.getField() != null) {
                columnMetadata.getField().reset();
            }
        }
        transfer.colStartIndex = 0;
        transfer.rowStartIndex = 0;
        transfer.colIndex = 0;
        transfer.rowIndex = 0;
        transfer.myTable = new TableiText(transfer.pdfDocument, transfer.tableMeta);
        transfer.transHeader();
        return transfer.myTable;
    }

    //====
    //== [Static Method] Block Stop
    //================================================
    //== [Method] Block Start
    //====
    //####################################################################
    //## [Method] sub-block : transXXX : 事先定義之 Template Method.
    //####################################################################

    /**
     * Template Method.
     * 
     * @throws DocumentException
     */
    final public void transTable(Iterable<?> values) throws DocumentException {
        final List<ColumnMetadata> allColumns = this.tableMeta.getAllColumns();
        for (ColumnMetadata columnMetadata : allColumns) {
            if (columnMetadata.getField() != null) {
                columnMetadata.getField().reset();
            }
        }
        this.processTableStart();
        this.transHeader();
        this.transBody(values);
        this.processTableEnd();
    }

    /**
     * Template Method.
     * 
     * @throws DocumentException
     */
    final public void transTable(Object[] valuesArray) throws DocumentException {
        final List<ColumnMetadata> allColumns = this.tableMeta.getAllColumns();
        for (ColumnMetadata columnMetadata : allColumns) {
            if (columnMetadata.getField() != null) {
                columnMetadata.getField().reset();
            }
        }
        this.processTableStart();
        this.transHeader();
        this.transBody(valuesArray);
        this.processTableEnd();
    }

    /**
     * @param valuesArray
     * @throws DocumentException
     */
    final protected void transBody(Object[] valuesArray) throws DocumentException {
        if (valuesArray != null && valuesArray.length > 0) {
            for (int i = 0; i < valuesArray.length; i++) {
                this.writeRow(valuesArray[i]);
                this.dataCount++;
                if (this.dataCount >= this.maxDataCount) {
                    return;
                }
            }
        }
    }

    /**
     * @param valuesArray
     * @throws DocumentException
     */
    final protected void transBody(Iterable<?> values) throws DocumentException {
        if (values != null) {
            for (Object object : values) {
                this.writeRow(object);
                this.dataCount++;
                if (this.dataCount >= this.maxDataCount) {
                    return;
                }
            }
        }
    }

    final protected void transHeader() throws DocumentException {
        //所有欄位資訊
        final List<ColumnMetadata> allColumns = this.tableMeta.getAllColumns();
        final int colsNumber = this.tableMeta.getColsNumber();
        //前置欄位資訊
        final int totalWidth = colsNumber;

        //大標題
        ColumnMetadata colInfo = (ColumnMetadata) allColumns.get(0);
        this.processTableCaption(totalWidth, colInfo);
        this.processHeaderStart();
        final ColumnMetadata root = this.tableMeta.getCaption();
        final List<ColumnMetadata> subColumns = root.getSubColumns();
        final int subColsNumber = root.getColsNumber();
        int leftCount = 0;
        for (ColumnMetadata headerColumn : subColumns) {
            drawHeader(this.myTable, headerColumn, subColsNumber, leftCount);
            leftCount += headerColumn.getColsNumber();
        }
        this.rowIndex++;
        this.myTable.getTable().setHeaderRows(this.rowIndex);
    }

    /**
     * @param myTable2
     * @param subColumn
     * @param columns
     * @param i
     * @throws DocumentException
     */
    private void drawHeader(TableiText table, ColumnMetadata headerColumn, int columns, int leftCount) throws DocumentException {

        final List<ColumnMetadata> subColumns = headerColumn.getSubColumns();
        if (subColumns == null || subColumns.isEmpty()) {
            final PdfPCell cell = TableiText.createHeaderCell(this.pdfDocument, headerColumn.getName(), headerColumn);
            table.addCell(cell);
        } else {
            final int myColsNumber = headerColumn.getColsNumber();
            final int rightCols = columns - leftCount - myColsNumber;

            final InnerTable innerTable = table.createInnerTable(leftCount, rightCols);
            {
                final PdfPCell cell = TableiText.createHeaderCell(this.pdfDocument, headerColumn.getName(), headerColumn);
                cell.setColspan(myColsNumber);
                innerTable.addCell(cell);
            }
            int myLeftCount = 0;
            boolean flagDrawSub = false;
            for (ColumnMetadata subColumn : subColumns) {
                flagDrawSub |= (subColumn.getName() != null);
            }
            if (flagDrawSub) {
                for (ColumnMetadata subColumn : subColumns) {
                    drawHeader(innerTable, subColumn, myColsNumber, myLeftCount);
                    myLeftCount += subColumn.getColsNumber();
                }
            }
            innerTable.appendMe();
        }

    }

    /**
     * 處理單列資料,
     * 
     * Map/JavaBean/XmlNode.
     * 
     * @throws DocumentException
     */
    final protected Object[] writeRow(Object data) throws DocumentException {

        //1.a 轉換本列原始資料為可輸出資料物件.
        //    i.  處理：取值(property of bean)/常數
        //    ii. HorizontalExpression.eval() 固定為NULL, 實際值在「1.b」呼叫 evalAsValue() 處理. 
        final Object[] nowData = new Object[this.colsNumber]; // 本列輸出資料文字.
        for (int i = 0; i < this.colsMeta.length; i++) {
            final ColumnMetadata colMeta = this.colsMeta[i];
            final CellDataSource field = colMeta.getField();
            nowData[i] = field.eval(data);
        }
        //
        //1.b 計算水平相依欄位值(HorizontalExpression). 
        //
        for (int i = 0; i < this.colsMeta.length; i++) {
            final ColumnMetadata colMeta = this.colsMeta[i];
            final CellDataSource field = colMeta.getField();
            if (field instanceof HorizontalExpression) { // 
                Object decimal = ((HorizontalExpression<?>) field).evalAsValue(this.colsMeta, nowData);
                nowData[i] = decimal;
            }
        }
        //
        //3. 處理本列實際輸出.
        //
        processSingleRowOutput(this.colsMeta, nowData);
        return nowData;
    }

    //####################################################################
    //## [Method] sub-block : 表格開始與結束 
    //####################################################################

    protected void processTableEnd() {

        try {
            this.toBeClosed = true;
            if (this.eventHandler != null) {
                this.eventHandler.onTableEnd(this.myTable);
            }
            //this.pdfDocument.getPdfWriter()
            //this.pdfDocument.getDocument().
            //this.myTable.getTable().is

            this.myTable.appendMe();
        } catch (DocumentException e) {
            throw new RuntimeException("結束表格輸出錯誤!", e);
        } finally {
            this.eventHandler = null;
            this.tableMeta = null;
            this.myTable.getTable().setHeaderRows(0);
            this.myTable.getTable().deleteBodyRows();
            this.myTable = null;
        }

    }

    protected void processTableStart() {
        try {
            this.colStartIndex = 0;
            this.rowStartIndex = 0;
            this.colIndex = 0;
            this.rowIndex = 0;
            this.myTable = new TableiText(this.pdfDocument, this.tableMeta);
            this.myTable.getTable().setTableEvent(new MyTableEvent());
        } catch (DocumentException e) {
            throw new RuntimeException("建立表格物件錯誤!", e);
        }
    }

    /**
     * 處理表格主標題列.
     * 
     * @throws DocumentException
     * @see nca.util.doc.table.AbstractNormalTableTransfer#processTableCaption(int, nca.util.doc.table.ColumnMetadata)
     */

    protected void processTableCaption(int totalWidth, ColumnMetadata colInfo) throws DocumentException {

        if (StringUtils.isNotBlank(colInfo.getName())) {
            processRowStart();
            final PdfPCell cell = TableiText.createHeaderCell(this.pdfDocument, colInfo.getName(), colInfo);
            cell.setPaddingBottom(10);
            cell.setColspan(this.colsNumber);
            this.myTable.getTable().addCell(cell);
            processRowEnd();
        }

    }

    protected void processHeaderStart() {
        this.processRowStart();
    }

    //####################################################################
    //## [Method] sub-block : 表格標題列. 
    //####################################################################

    /**
     * 標題列結尾，補齊欄位, 使該列得以輸出.
     * 
     * @throws DocumentException
     * @see nca.util.doc.table.AbstractNormalTableTransfer#processHeaderEnd()
     */
    protected void processHeaderEnd() throws DocumentException {
        for (int i = this.colIndex; i < this.colsMeta.length; i++) {
            final PdfPCell cell = TableiText.createHeaderCell(this.pdfDocument, "", this.colsMeta[i]);
            // 跨列無上下邊框.
            cell.setBorder(this.colsMeta[i].getHeaderFormat().getBorder() & (~Rectangle.TOP) & (~Rectangle.BOTTOM));
            this.myTable.getTable().addCell(cell);
        }
        //super.processHeaderEnd();
        this.processRowEnd();
        this.myTable.getTable().setHeaderRows(this.rowIndex);
    }

    //####################################################################
    //## [Method] sub-block : 表格資料列. 
    //####################################################################

    /**
     * 資料列開始，新建物件.
     * 
     * @see nca.util.doc.table.AbstractNormalTableTransfer#processRowStart()
     */

    protected void processRowStart() {
        this.colIndex = this.colStartIndex;
    }

    /**
     * 資料列結尾. 增加 rowIndex;
     * 
     * @see nca.util.doc.table.AbstractNormalTableTransfer#processRowEnd()
     */

    protected void processRowEnd() {

        final PdfPTable table = this.myTable.getTable();

        this.rowIndex++;
        this.colIndex = this.colStartIndex;

        //int rows = table.getRows().size();

        if (this.rowIndex % 30 == 0) {
            //float calculateHeights = table.calculateHeights(true);
            //System.out.println(rows + ":" + calculateHeights);
            try {
                this.myTable.appendMe();
            } catch (DocumentException e) {
            }
            table.deleteBodyRows();
            table.setSkipFirstHeader(true);
            table.setSpacingBefore(0);
            //            if (this.rowIndex % 100 == 0) {
            //                //System.out.println(this.rowIndex);
            //                //MoniterTools.showMemory();
            //            }
        }

    }

    protected void processSingleRowOutput(ColumnMetadata[] metadatas, Object[] nowData) throws DocumentException {

        processRowStart();

        for (int i = 0; i < metadatas.length; i++) {
            ColumnMetadata metadata = metadatas[i];
            Image imgContent = null;
            final Object object = nowData[i];
            final String text;
            final String textFormat = metadata.getContentFormat().getTextFormat();

            if (metadata.getField() instanceof BarcodeImage) {
                BarcodeImage barcode = (BarcodeImage) metadata.getField();
                imgContent = barcode.toImage(this.pdfDocument.getPdfWriter(), ObjectUtils.toString(object));
                text = "";
            } else if (object instanceof Date) {
                // 顯示日期  
                final Date javaDate = (Date) object;
                if (org.apache.commons.lang3.StringUtils.isNotBlank(textFormat)) {
                    text = DateUtil.formatDate(javaDate, textFormat);
                } else {
                    text = this.formatterYYMMDD.format(javaDate);
                }
            } else if (object instanceof Number) {
                if ("#,##0".equals(textFormat)) {
                    text = moneyFormat.format(object);
                } else {
                    text = ObjectUtils.toString(object);
                }
            } else if (object == null) {
                if ("#,##0".equals(textFormat)) {
                    text = "0";
                } else {
                    text = "";
                }
            } else {
                text = ObjectUtils.toString(object);
            }

            final PdfPCell cell;

            if (imgContent == null) {
                cell = TableiText.createContentCell(this.pdfDocument, text, this.colsMeta[i]);
            } else {
                cell = new PdfPCell(imgContent, false);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            }

            this.myTable.getTable().addCell(cell);
            this.colIndex++;
        }
        processRowEnd();

    }

    protected int getLastRowNum() {
        return this.rowIndex;
    }

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
