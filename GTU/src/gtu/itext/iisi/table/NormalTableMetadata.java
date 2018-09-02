package gtu.itext.iisi.table;

import gtu.itext.iisi.data.CellDataSource;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.Element;

/**
 * 
 * @author Tsai Chi-Feng
 * @version 1.0
 */
public class NormalTableMetadata {

    public static interface ColumnDefine {
        String getTitle();

        CellDataSource getField();

        float getWidth();
    }

    //================================================
    //== [static variables] Block Start
    //====
    //====
    //== [static variables] Block Stop
    //================================================
    //== [instance variables] Block Start
    //====

    /**
     * 用以表示標題列的 ColumnMetadata (root).
     * 
     * 在 iTextTable 的應用中，不呈現任何資訊.
     */
    private ColumnMetadata caption = null;

    private Object tag = null;

    private GroupingInfo groupingInfo = null;

    // ###################################################################
    // ## [instance variables] sub-block : 資料相依資訊
    // ####################################################################

    //private int maxHeaderLevel = 0;

    protected String name = null;

    // ###################################################################
    // ## [instance variables] sub-block : 外觀設定
    // ####################################################################

    private float[] partialWidths = new float[0];

    protected float width = 85;

    protected float padding = 4;

    protected float spacing = 0;

    protected float offset = 1;

    protected int horizontalAlignment = Element.ALIGN_CENTER;

    /** 表格外框. */
    protected int borderWidth = 1;

    /** 表格外框. */
    protected int border = CellFormat.RECTANGLE_BOX;

    /** 表格標題預設外觀. */
    protected CellFormat defaultHeaderFormat = createDefaultHeaderFormat();

    /** 表格欄位預設外觀. */
    protected CellFormat defaultContentFormat = new CellFormat();

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

    public NormalTableMetadata(String name) {
        this.name = name;
        this.setCaption(name);
    }

    /**
     * @param name Caption名稱
     * @param captionFontSize Caption Size
     */
    public NormalTableMetadata(String name, int captionFontSize) {
        this.name = name;
        this.setCaption(name);
        this.caption.getHeaderFormat().setFontSize(captionFontSize);
    }

    public NormalTableMetadata(String name, float width, float padding, float spacing) {
        this.name = name;
        this.width = width;
        this.padding = padding;
        this.spacing = spacing;
        this.setCaption(name);
    }

    //====
    //== [Constructors] Block Stop
    //================================================
    //== [Overrided Method] Block Start (toString/equals+hashCode)
    //====

    @Override
    public String toString() {
        StringBuffer tmpS = new StringBuffer();
        List<ColumnMetadata> v = getAllColumns();
        for (int i = 0; i < v.size(); i++) {
            Object item = v.get(i);
            tmpS.append(item).append("\n");
        }
        return tmpS.toString();
    }

    //====
    //== [Overrided Method] Block Stop
    //================================================
    //== [Accessor] Block Start
    //====

    /**
     * @return 傳回 defaultContentFormat。
     */
    public final CellFormat getDefaultContentFormat() {
        return this.defaultContentFormat;
    }

    /**
     * @param defaultContentFormat 要設定的 defaultContentFormat。
     */
    public final void setDefaultContentFormat(CellFormat defaultContentFormat) {
        this.defaultContentFormat = defaultContentFormat;
    }

    /**
     * @return 傳回 defaultHeaderFormat。
     */
    public final CellFormat getDefaultHeaderFormat() {
        return this.defaultHeaderFormat;
    }

    /**
     * @param defaultHeaderFormat 要設定的 defaultHeaderFormat。
     */
    public final void setDefaultHeaderFormat(CellFormat defaultHeaderFormat) {
        this.defaultHeaderFormat = defaultHeaderFormat;
    }

    public float[] getPartialWidths() {
        return this.partialWidths;
    }

    public void setPartialWidths(float[] partialWidths) {
        this.partialWidths = partialWidths;
    }

    public int getBorder() {
        return this.border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public int getBorderWidth() {
        return this.borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * 設定表格在頁面中的寬度比例.
     */
    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return this.width;
    }

    /**
     * set padding.
     * 
     * @param padding
     */
    public void setPadding(float padding) {
        this.padding = padding;
    }

    /**
     * set spacing.
     * 
     * @param padding
     */
    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public float getOffset() {
        return this.offset;
    }

    public float getPadding() {
        return this.padding;
    }

    public float getSpacing() {
        return this.spacing;
    }

    public Object getTag() {
        return this.tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public GroupingInfo getGroupingInfo() {
        return this.groupingInfo;
    }

    public void setGroupingInfo(GroupingInfo groupingInfo) {
        this.groupingInfo = groupingInfo;
    }

    /**
     * @return the horizontalAlignment
     */
    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    /**
     * @param horizontalAlignment the horizontalAlignment to set
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    //====
    //== [Accessor] Block Stop
    //================================================
    //== [Static Method] Block Start
    //====

    private static CellFormat createDefaultHeaderFormat() {
        CellFormat cf = new CellFormat();
        cf.setAlignH(CellFormat.ALIGN_CENTER);
        cf.setAlignV(CellFormat.ALIGN_MIDDLE);
        cf.setBackgroundColor(Color.GRAY);
        return cf;
    }

    //====
    //== [Static Method] Block Stop
    //================================================
    //== [Method] Block Start
    //====
    // ####################################################################
    // ## sub-block : XXXX(可依功能區分)
    // ####################################################################
    //====
    //== [Method] Block Stop
    //================================================
    //== [Main Method] Block Start
    //====
    //    public static void main(String[] args) {
    //        NormalTableMetadata table = TEST_TMs.getTM2();
    //        List v = table.getAllColumns();
    //        System.out.println(table.getMaxHeaderLevel());
    //        System.out.println(table.getColsNumber());
    //        float[] ws = table.getWidths();
    //        for (int i = 0; i < ws.length; i++) {
    //            System.out.print(ws[i]);
    //            System.out.print(",");
    //        }
    //        System.out.println();
    //        for (Iterator i = v.iterator(); i.hasNext();) {
    //            System.out.println(i.next());
    //        }
    //    }
    //====
    //== [Main Method] Block Stop
    //================================================

    public ColumnMetadata getCaption() {
        return this.caption;
    }

    public List<ColumnMetadata> getAllColumns() {
        List<ColumnMetadata> v = new ArrayList<ColumnMetadata>(10);
        int maxLV = this.getMaxHeaderLevel();
        for (int lv = 1; lv <= maxLV; lv++) {
            this.caption.getAllColumns(lv, v);
        }
        return v;
    }

    public float[] getWidths() {
        int n = this.getColsNumber();
        float[] widths = new float[n];
        this.caption.getWidths(widths, 0);
        return widths;
    }

    /**
     * 取得 column 總數 (以最下層的所有欄位計算).
     */
    public int getColsNumber() {
        return this.caption.getColsNumber();
    };

    /**
     * 取得應輸出資料的所有 column (以最下層的所有欄位計算).
     */
    public List<ColumnMetadata> getDataColumns() {
        List<ColumnMetadata> v = new ArrayList<ColumnMetadata>(10);
        this.caption.getDataCols(v);
        return v;
    };

    protected int getMaxHeaderLevel() {
        return this.caption.getMaxHeaderLevel(true);
    }

    public ColumnMetadata newSubColumn(String name, CellDataSource filed, float width, CellFormat format) {
        return this.newSubColumn(name, filed, width, format, format);
    }

    public ColumnMetadata newSubColumn(String name, CellDataSource filed, float width //
                                       , CellFormat headerFormat //
                                       , CellFormat contentFormat//
    ) {
        CellFormat hf = (headerFormat == null) ? this.defaultHeaderFormat : (CellFormat) headerFormat.clone();
        CellFormat cf = (contentFormat == null) ? this.defaultContentFormat : (CellFormat) contentFormat.clone();
        ColumnMetadata cm = new ColumnMetadata(hf, cf, name, filed, width);
        this.caption.addSubColumn(cm);
        return cm;
    }

    /**
     * @param columnDefine
     */
    public ColumnMetadata newSubColumn(ColumnDefine columnDefine) {
        return this.newSubColumn(columnDefine.getTitle(), columnDefine.getField(), columnDefine.getWidth());
    }

    public ColumnMetadata newSubColumn(String name, float width) {
        ColumnMetadata cm = new ColumnMetadata(this.defaultHeaderFormat, this.defaultContentFormat, name, null, width);
        this.caption.addSubColumn(cm);
        return cm;
    }

    public ColumnMetadata newSubColumn(String name, CellDataSource filed, float width) {
        ColumnMetadata cm = new ColumnMetadata(this.defaultHeaderFormat, this.defaultContentFormat, name, filed, width);
        this.caption.addSubColumn(cm);
        return cm;
    }

    public ColumnMetadata newSubColumn(String name, CellDataSource filed) {
        ColumnMetadata cm = new ColumnMetadata(this.defaultHeaderFormat, this.defaultContentFormat, name, filed);
        this.caption.addSubColumn(cm);
        return cm;
    }

    /** 只顯示名稱，無任何內容. */
    public ColumnMetadata newSubColumn(String name) {
        ColumnMetadata cm = new ColumnMetadata(this.defaultHeaderFormat, this.defaultContentFormat, name);
        this.caption.addSubColumn(cm);
        return cm;
    }

    private void setCaption(String name) {
        this.caption = new ColumnMetadata(this.defaultHeaderFormat, this.defaultContentFormat, name);
        final CellFormat format = this.caption.getHeaderFormat();
        format.setBorder(CellFormat.RECTANGLE_NO_BORDER);
        format.setBorderWidth(0);
        format.setFontSize(18);
        format.setBackgroundColor(null);
        format.setAlignH(CellFormat.ALIGN_CENTER);
        format.setAlignV(CellFormat.ALIGN_MIDDLE);

    }

    //    @Override
    //    protected void finalize() throws Throwable {
    //        super.finalize();
    //    }

    //============================================================
    //
    //
    public void showMeForDebug() {
        List<ColumnMetadata> v = getAllColumns();
        //System.out.println(getMaxHeaderLevel());
        //System.out.println(getColsNumber());
        float[] ws = getWidths();
        for (int i = 0; i < ws.length; i++) {
            //System.out.print(ws[i]);
            //System.out.print(",");
        }
        //System.out.println();
        for (Iterator<ColumnMetadata> i = v.iterator(); i.hasNext();) {
            //System.out.println(i.next());
        }
    }

}
