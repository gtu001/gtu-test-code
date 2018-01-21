package gtu.itext.iisi;

import gtu.itext.iisi.data.BarcodeImage;
import gtu.itext.iisi.table.AbstractNormalTableTransfer;
import gtu.itext.iisi.table.CellFormat;
import gtu.itext.iisi.table.ColumnMetadata;
import gtu.itext.iisi.table.GroupingInfo;
import gtu.itext.iisi.table.GroupingInfo.GroupLevel;
import gtu.itext.iisi.table.GroupingRowProcessor;
import gtu.itext.iisi.table.GroupingRowProcessor.ProcessInfo;
import gtu.itext.iisi.table.NormalTableMetadata;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import tw.gov.moi.util.DateUtil;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

public class PDFNormalTableTransfer extends AbstractNormalTableTransfer {

    public abstract static class EventHandler {
        public void onTableEnd(TableiText table) {

        }
    }

    //================================================
    //== [static variables] Block Start
    //====

    public static int GMODE_NORMAL = 0;

    public static int GMODE_MASTER_DETAIL = 1;

    final static private SimpleDateFormat formatterYYMMDD = new SimpleDateFormat("yyyy/MM/dd");

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====
    private PDFDocument pdfDocument;

    private TableiText myTable;

    /** 統計分組呈現模式. */
    private int groupingDisplayMode = GMODE_NORMAL;

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

    /** 資料開始橫列位置 */
    @SuppressWarnings("unused")
    private int datarowStartIndex = 0;

    //####################################################################
    //## [instance variables] sub-block : 標題列排版處理.
    //####################################################################

    final private Set<Point> usedHeaderCell = new HashSet<Point>();

    //####################################################################
    //## [instance variables] sub-block : 自訂事件處理.
    //####################################################################

    /** */
    private EventHandler eventHandler;

    //====
    //== [instance variables] Block Stop 
    //================================================
    //== [static Constructor] Block Start
    //====
    final private static NumberFormat moneyFormat = new DecimalFormat("#,##0");

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
    public PDFNormalTableTransfer(PDFDocument pdfDocument, NormalTableMetadata tableMeta) throws DocumentException {
        super(tableMeta);
        this.pdfDocument = pdfDocument;
    }

    /**
     * 初始表格轉換物件.
     * 
     * 建立 PdfPTable
     * 
     * @throws DocumentException
     */
    public PDFNormalTableTransfer(PDFDocument pdfDocument, NormalTableMetadata tableMeta, int groupingDisplayMode) throws DocumentException {
        super(tableMeta);
        this.groupingDisplayMode = groupingDisplayMode;
        this.pdfDocument = pdfDocument;
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
     * @return 傳回 eventHandler。
     */
    public final EventHandler getEventHandler() {
        return this.eventHandler;
    }

    /**
     * @param eventHandler 要設定的 eventHandler。
     */
    public final void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====

    @Override
    protected int getLastRowNum() {
        return this.rowIndex;
    }

    /**
     * 
     * @param metadatas
     * @param lv 目前處理統計層級.
     * @param groupLevel 統計列群組項目定義.
     * @param pi 統計列群組項目處理器.
     * @throws DocumentException
     * @see nca.util.doc.table.AbstractNormalTableTransfer#processGroupRow(nca.util.doc.table.ColumnMetadata[], int,
     *      nca.util.doc.table.GroupingInfo.GroupLevel, nca.util.doc.table.GroupingRowProcessor.ProcessInfo)
     */
    @Override
    protected void processGroupRow(ColumnMetadata[] metadatas, int lv, GroupLevel groupLevel, ProcessInfo pi) throws DocumentException {

        // 0. 列開始.
        processRowStart();

        int firstIndex = 0; // TODO [無期限][時機：(公用函式) 重構] 找出此值作業可能可以移至 initial() 操作.
        for (int i = 0; i < metadatas.length; i++) {
            final ColumnMetadata metadata = metadatas[i];
            if (metadata.getGroupFunction() != null) {
                firstIndex = i;
                break;
            }
        }

        if (firstIndex > 0) {
            final String text;
            if (lv == 0) { // 最後總計標題欄位.
                text = groupLevel.getPostfix();
            } else { // 統計標題欄位.
                text = pi.getPreviousValue() + " " + groupLevel.getPostfix();
            }
            if (this.groupingDisplayMode == GMODE_MASTER_DETAIL) {
                final GroupingInfo groupingInfo = this.tableMeta.getGroupingInfo();
                this.myTable.addCell(text, firstIndex, groupingInfo.getHeaderFormat());
            } else {
                this.myTable.addCell(text, firstIndex, this.tableMeta.getDefaultContentFormat());
            }
        }

        // 1. 產出統計列.
        for (int i = firstIndex; i < metadatas.length; i++) {
            final ColumnMetadata metadata = metadatas[i];
            final String text;

            if (i == 0 && lv == 0) { // 最後總計標題欄位.
                text = groupLevel.getPostfix();
            } else if (pi != null && groupLevel.getDataColumn() == metadata) { // 統計標題欄位.
                text = pi.getPreviousValue() + " " + groupLevel.getPostfix();
            } else if (pi != null && pi.getFuncProcessor(i) != null) { // 一般統計列.

                Object result = pi.getFuncProcessor(i).getResult();
                if (result instanceof Number //
                        && "#,##0".equals(metadata.getContentFormat().getTextFormat()) //
                ) {
                    text = metadata.getGroupingPrefix() + moneyFormat.format(result) + metadata.getGroupingPostfix();
                } else {
                    text = metadata.getGroupingPrefix() + ObjectUtils.toString(result) + metadata.getGroupingPostfix();
                }
            } else { // 無定義欄位
                text = "";
            }

            this.myTable.addContentCell(text, metadata);
            this.colIndex++;
        }
        // 2. 列結尾.
        processRowEnd();

    }

    //####################################################################
    //## [Method] sub-block : 表格開始與結束 
    //####################################################################

    @Override
    protected void processTableEnd() {
        try {
            if (this.eventHandler != null) {
                this.eventHandler.onTableEnd(this.myTable);
            }
            this.myTable.appendMe();
        } catch (DocumentException e) {
            throw new RuntimeException("結束表格輸出錯誤!", e);
        }
    }

    @Override
    protected void processTableStart() {
        try {
            this.colStartIndex = 0;
            this.rowStartIndex = 0;
            this.colIndex = 0;
            this.rowIndex = 0;
            this.datarowStartIndex = 0;
            this.usedHeaderCell.clear();
            this.myTable = new TableiText(this.pdfDocument, this.tableMeta);
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
    @Override
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

    //####################################################################
    //## [Method] sub-block : 表格標題列. 
    //####################################################################

    @Override
    protected void processCreateHeaderCell(ColumnMetadata colInfo, int rowSpan, int colSpan) throws DocumentException {
        // 1. 找出因前列跨列因素佔用外, 
        // 		本列目前第一個可用位置
        // 		被佔用欄位補"空欄"
        while (this.usedHeaderCell.contains(new Point(this.colIndex, this.rowIndex))) {
            this.colIndex++;
            final PdfPCell cell = TableiText.createHeaderCell(this.pdfDocument, "", colInfo);
            // 若跨列，則無上下邊框.
            cell.setBorder(colInfo.getHeaderFormat().getBorder() & (~Rectangle.TOP) & (~Rectangle.BOTTOM));
            this.myTable.getTable().addCell(cell);
        }

        // 2. 輸出標題欄位(含跨欄設定)
        final PdfPCell cell = TableiText.createHeaderCell(this.pdfDocument, colInfo.getName(), colInfo);
        cell.setColspan(colSpan);
        if (rowSpan > 1) {
            // 初次跨列，無下邊框.
            cell.setBorder(colInfo.getHeaderFormat().getBorder() & (~Rectangle.BOTTOM));
        }
        this.myTable.getTable().addCell(cell);

        // 3. 欄位使用註記.
        for (int y = this.rowIndex; y < (this.rowIndex + rowSpan); y++) {
            for (int x = this.colIndex; x < (this.colIndex + colSpan); x++) {
                this.usedHeaderCell.add(new Point(x, y));
            }
        }

        // 4. 次欄起始位置. 
        this.colIndex += colSpan;
    }

    /**
     * 標題列結尾，補齊欄位, 使該列得以輸出.
     * 
     * @throws DocumentException
     * @see nca.util.doc.table.AbstractNormalTableTransfer#processHeaderEnd()
     */
    @Override
    protected void processHeaderEnd() throws DocumentException {
        for (int i = this.colIndex; i < this.colsMeta.length; i++) {
            final PdfPCell cell = TableiText.createHeaderCell(this.pdfDocument, "", this.colsMeta[i]);
            // 跨列無上下邊框.
            cell.setBorder(this.colsMeta[i].getHeaderFormat().getBorder() & (~Rectangle.TOP) & (~Rectangle.BOTTOM));
            this.myTable.getTable().addCell(cell);
        }
        super.processHeaderEnd();
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
    @Override
    protected void processRowStart() {
        this.colIndex = this.colStartIndex;
    }

    /**
     * 資料列結尾. 增加 rowIndex;
     * 
     * @see nca.util.doc.table.AbstractNormalTableTransfer#processRowEnd()
     */
    @Override
    protected void processRowEnd() {
        this.rowIndex++;
        this.colIndex = this.colStartIndex;
    }

    @Override
    protected void processSingleRowOutput(ColumnMetadata[] metadatas, Object[] nowData) throws DocumentException {

        processRowStart();

        if (this.groupingDisplayMode == GMODE_MASTER_DETAIL) {

            final GroupingInfo groupingInfo = this.tableMeta.getGroupingInfo();
            final GroupLevel[] groups = groupingInfo.getGroups();
            final CellFormat masterHeaderFormat = groupingInfo.getHeaderFormat();

            for (int i = 1; i < groups.length; i++) { //
                GroupingRowProcessor.ProcessInfo pi = this.grProcessor.getProcessInfo(i);
                if (pi.getFirstRow() == pi.getLastRow()) {

                    if (i > 1) {

                        final PdfPCell emptyCell = TableiText.createCell( //
                                this.pdfDocument, "", masterHeaderFormat);
                        emptyCell.setColspan(i - 1);
                        this.myTable.getTable().addCell(emptyCell);
                    }

                    final PdfPCell cell = TableiText.createCell( //
                            this.pdfDocument, //
                            ObjectUtils.toString(pi.getPreviousValue()), masterHeaderFormat //
                            );
                    cell.setColspan(metadatas.length - i + 1);
                    this.myTable.getTable().addCell(cell);
                    processRowEnd();
                    processRowStart();
                }
            }
        }

        for (int i = 0; i < metadatas.length; i++) {
            ColumnMetadata metadata = metadatas[i];
            Image imgContent = null;
            final Object object = nowData[i];
            final String text;
            final String textFormat = metadata.getContentFormat().getTextFormat();

            if (this.groupingDisplayMode == GMODE_MASTER_DETAIL && this.grProcessor.isGroupingColumn(i)) { // 資料列不顯示 master key.
                text = "";
            } else if (metadata.getField() instanceof BarcodeImage) {
                BarcodeImage barcode = (BarcodeImage) metadata.getField();
                imgContent = barcode.toImage(this.pdfDocument.getPdfWriter(), ObjectUtils.toString(object));
                text = "";
            } else if (object instanceof Date) {
                // 顯示日期  
                final Date javaDate = (Date) object;
                if (org.apache.commons.lang3.StringUtils.isNotBlank(textFormat)) {
                    text = DateUtil.formatDate(javaDate, textFormat);
                } else {
                    text = formatterYYMMDD.format(javaDate);
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

    //    static class MyTableEvents implements PdfPTableEvent {
    //        /**
    //         * @see com.lowagie.text.pdf.PdfPTableEvent#tableLayout(com.lowagie.text.pdf.PdfPTable, float[][], float[], int, int, com.lowagie.text.pdf.PdfContentByte[])
    //         */
    //        public void tableLayout(PdfPTable table, float[][] width, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
    //        }
    //    }

}
