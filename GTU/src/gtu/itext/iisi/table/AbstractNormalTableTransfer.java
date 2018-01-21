/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.table;

import gtu.itext.iisi.data.CellDataSource;
import gtu.itext.iisi.data.HorizontalExpression;
import gtu.itext.iisi.table.GroupingInfo.GroupLevel;
import gtu.itext.iisi.table.GroupingRowProcessor.ProcessInfo;

import java.util.List;

import com.lowagie.text.DocumentException;

/**
 * 處理直欄式表格.
 * 
 * @author 920111 在 2008/3/13 建立
 */
public abstract class AbstractNormalTableTransfer {

    //================================================
    //== [static variables] Block Start
    //====
    //====
    //== [static variables] Block Stop
    //================================================
    //== [instance variables] Block Start
    //====
    protected NormalTableMetadata tableMeta;

    protected int colsNumber;

    protected ColumnMetadata[] colsMeta;

    protected GroupingRowProcessor grProcessor;

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
     * @param tableMeta
     */
    public AbstractNormalTableTransfer(NormalTableMetadata tableMeta) {
        super();
        this.tableMeta = tableMeta;
        initial();
    }

    private void initial() {

        final List<ColumnMetadata> dataColumns = this.tableMeta.getDataColumns(); // 取得所有資料欄位定義.
        //(暫用)
        this.colsNumber = dataColumns.size();
        this.colsMeta = dataColumns.toArray(new ColumnMetadata[0]);
        //分組資訊處理.
        this.grProcessor = new GroupingRowProcessor(this, //
                this.colsMeta, this.tableMeta.getGroupingInfo());
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
    //####################################################################
    //## [Method] sub-block : transXXX : 事先定義之 Template Method.
    //####################################################################

    private boolean flagHasData;

    /**
     * Template Method.
     * 
     * @throws DocumentException
     */
    final public void transTable(Iterable<?> values) throws DocumentException {
        this.flagHasData = false;
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
            this.flagHasData = true;
            for (int i = 0; i < valuesArray.length; i++) {
                Object object = valuesArray[i];
                @SuppressWarnings("unused")
                Object[] objects = this.writeRow(object);
            }
        }
        transFinalGrouping();
    }

    /**
     * @param valuesArray
     * @throws DocumentException
     */
    final protected void transBody(Iterable<?> values) throws DocumentException {
        if (values != null) {
            for (Object object : values) {
                this.flagHasData = true;
                this.writeRow(object);
            }
        }
        transFinalGrouping();
    }

    final protected void transHeader() throws DocumentException {

        //所有欄位資訊
        final List<ColumnMetadata> allColumns = this.tableMeta.getAllColumns();
        final int maxLV = this.tableMeta.getCaption().getMaxHeaderLevel(true);
        final int colsNumber = this.tableMeta.getColsNumber();

        //前置欄位資訊
        final int totalWidth = colsNumber;
        //final List headers = new ArrayList(allColumns.size());

        //大標題
        ColumnMetadata colInfo = (ColumnMetadata) allColumns.get(0);
        this.processTableCaption(totalWidth, colInfo);
        this.processHeaderStart();

        int nowLevel = 2;
        for (int i = 1; i < allColumns.size(); i++) {
            colInfo = (ColumnMetadata) allColumns.get(i);
            //System.out.println(colInfo);
            if (colInfo.getHeaderLevel() != nowLevel) {
                this.processRowEnd();
                this.processRowStart();
                nowLevel = colInfo.getHeaderLevel();
            }
            final int rs = maxLV - colInfo.getMaxHeaderLevel(false) + 1;
            final int cs = colInfo.getColsNumber();
            processCreateHeaderCell(colInfo, rs, cs);
        }
        this.processHeaderEnd();

        this.grProcessor.doFirstRow();
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
        //2. 根據現有資料與先前資料比對，決定是否需輸出統計列.
        //
        //	2.a 決定群組處理層級.
        final int lv = this.grProcessor.decideGroupLv(this.colsMeta, nowData);
        //-1 不處理, 0為總計, 1, 2, ...
        if (lv >= 0) {
            final GroupingInfo groupingInfo = this.tableMeta.getGroupingInfo();
            final GroupLevel[] groups = groupingInfo.getGroups();
            for (int i = groups.length - 1; i >= lv; i--) { //	從最後一層, 回溯到目前應處理的最上層.
                GroupingRowProcessor.ProcessInfo pi = this.grProcessor.getProcessInfo(i);
                this.processGroupRow(this.colsMeta, i, groupingInfo.getGroupLevel(i), pi);
                this.grProcessor.clearProcessInfo(i, nowData);

            }
        }
        //
        //3. 處理本列實際輸出.
        //
        processSingleRowOutput(this.colsMeta, nowData);
        return nowData;
    }

    /**
     * 處理最後一列的統計資訊.
     * 
     * @throws DocumentException
     */
    final private void transFinalGrouping() throws DocumentException {
        final GroupingInfo groupingInfo = this.tableMeta.getGroupingInfo();
        if (groupingInfo == null) {
            return;
        }
        if (!this.flagHasData) {
            return;
        }
        final GroupLevel[] groups = groupingInfo.getGroups();
        for (int i = groups.length - 1; i >= 0; i--) {

            GroupingRowProcessor.ProcessInfo pi = this.grProcessor.getProcessInfo(i);
            if (pi != null) {
                pi.lastRow = this.getLastRowNum();
            }
            this.processGroupRow(this.colsMeta, i, groups[i], pi);
            this.grProcessor.clearProcessInfo(i, null);
        }
    }

    /**
     * 處理統計列方式.
     * 
     * @throws DocumentException
     */
    abstract protected void processGroupRow(ColumnMetadata[] metadatas, int lv, GroupLevel groupLevel, ProcessInfo pi) throws DocumentException;

    //####################################################################
    //## [Method] sub-block :
    //####################################################################

    abstract protected void processCreateHeaderCell(ColumnMetadata colInfo, final int rs, final int cs) throws DocumentException;

    abstract protected void processTableEnd();

    abstract protected void processTableStart();

    abstract protected void processTableCaption(int totalWidth, ColumnMetadata colInfo) throws DocumentException;

    protected void processHeaderStart() {
        this.processRowStart();
    }

    protected void processHeaderEnd() throws DocumentException {
        this.processRowEnd();
    }

    //####################################################################
    //## [Method] sub-block : 單行資料處理.
    //####################################################################

    abstract protected void processRowStart();

    abstract protected void processRowEnd();

    abstract protected void processSingleRowOutput(ColumnMetadata[] metadatas, Object[] nowData) throws DocumentException;

    abstract protected int getLastRowNum();

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
