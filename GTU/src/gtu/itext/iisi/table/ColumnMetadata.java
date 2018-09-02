package gtu.itext.iisi.table;

import gtu.itext.iisi.data.CellDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

public class ColumnMetadata {

    // ================================================
    // == [static variables] Block Start
    // ====
    // ====
    // == [static variables] Block Stop
    // ================================================
    // == [instance variables] Block Start
    // ====

    /** 此欄位標題的層級. */
    protected int headerLevel = 1;

    /** 上層欄位標題. */
    protected ColumnMetadata parent = null;

    /** 表格中所顯示的欄位名稱 */
    protected String name = "";

    /** 對應欄位. */
    private CellDataSource field;

    /** 執行統計或小計時，所使用的公式類別 */
    private GroupingFunction groupFunction;

    /** 執行統計或小計時，在統計列所輸出的前置文字. */
    private String groupingPrefix = "";

    /** 執行統計或小計時，在統計列所輸出的後綴文字. */
    private String groupingPostfix = "";

    // protected String fullPath = "";

    /** 本欄寬度比重. 但只有位於最下層欄位的這個值會產生作用, 上層的欄位自動被視為下層的總合 */
    protected float width = 10;

    /** 標題格式. */
    private CellFormat headerFormat;

    /** 內文格式. */
    private CellFormat contentFormat;

    /** 可修改 之 標題格式. */
    private CellFormat mutableHeaderFormat;

    /** 可修改 之 內文格式. */
    private CellFormat mutableContentFormat;

    /** 標題層數計數. */
    private int maxHeaderLevel = 0;

    /**
     * 所佔空間的開頭索引
     */
    private int startIndex = 0;

    /** 子欄位標題. */
    protected List<ColumnMetadata> subColumns = null;

    private boolean newLine = false;

    protected int merge = 0;

    // ====
    // == [instance variables] Block Stop
    // ================================================
    // == [static Constructor] Block Start
    // ====
    // ====
    // == [static Constructor] Block Stop
    // ================================================
    // == [Constructors] Block Start (含init method)
    // ====

    //    /** 只可使用 newSubColumn 新增. */
    //    protected ColumnMetadata(CellFormat headerFormat, CellFormat contentFormat) {
    //        this.headerFormat = headerFormat;
    //        this.contentFormat = contentFormat;
    //    }

    /** 只可使用 newSubColumn 新增. */
    protected ColumnMetadata(CellFormat headerFormat, CellFormat contentFormat, String name) {
        this.headerFormat = headerFormat;
        this.contentFormat = contentFormat;
        this.name = name;
    }

    /** 只可使用 newSubColumn 新增. */
    protected ColumnMetadata(CellFormat headerFormat, CellFormat contentFormat, String name, CellDataSource field) {
        this.headerFormat = headerFormat;
        this.contentFormat = contentFormat;
        this.name = name;
        this.field = field;
    }

    /** 只可使用 newSubColumn 新增. */
    protected ColumnMetadata(CellFormat headerFormat, CellFormat contentFormat, String name, CellDataSource field, float width) {
        this.headerFormat = headerFormat;
        this.contentFormat = contentFormat;
        this.name = name;
        this.field = field;
        this.width = width;
    }

    // ====
    // == [Constructors] Block Stop
    // ================================================
    // == [Overrided Method] Block Start (toString/equals+hashCode)
    // ====

    /**
     * 相關內容資訊.
     * 
     * @return 相關內容資訊
     */
    @Override
    public String toString() {
        StringBuffer tmpS = new StringBuffer();
        tmpS.append("ColumnMetadata { \n ");
        tmpS.append(" name(caption) = ").append(this.name).append("\n");
        tmpS.append("      field = ").append(this.field).append("\n");
        tmpS.append("   headerLevel = ").append(this.headerLevel).append("\n");
        tmpS.append("   start Index = ").append(this.startIndex).append("\n");
        if (this.subColumns != null) {
            tmpS.append("    subColumns : ").append(this.subColumns.size()).append("個\n");
            tmpS.append("           跨欄 : ").append(this.getColsNumber()).append("\n");
        }
        tmpS.append("} ColumnMetadata  \n ");
        return tmpS.toString();
    }

    // ====
    // == [Overrided Method] Block Stop
    // ================================================
    // == [Accessor] Block Start
    // ====

    public float getWidth() {
        return this.width;
    }

    /**
     * @return 傳回 headerLevel。
     */
    public final int getHeaderLevel() {
        return this.headerLevel;
    }

    public String getName() {
        return this.name;
    }

    /**
     * @param textFormat 要設定的 textFormat。
     */
    public ColumnMetadata setTextFormat(String textFormat) {
        this.getContentFormat().setTextFormat(textFormat);
        return this;
    }

    public CellDataSource getField() {
        return this.field;
    }

    // ====
    // == [Accessor] Block Stop
    // ================================================
    // == [Static Method] Block Start
    // ====
    // ====
    // == [Static Method] Block Stop
    // ================================================
    // == [Method] Block Start
    // ====

    /**
     * 提供在指定欄位中的字串輸出形式. 只用於 xxxx...DataStringGetter/
     */
    final public String convertValue(Object obj) {
        return ObjectUtils.toString(obj);
    }

    // ####################################################################
    // ## sub-block : XXXX(可依功能區分)
    // ####################################################################

    /**
     * 在此標題欄位之下, 新增一個子標題.
     * 
     * @param name 此欄位標題列所顯示的文字.
     * @param width 此欄位所佔的寬度比重.
     * @return 所產生的子標題.
     */
    public ColumnMetadata newSubColumn(String name, CellDataSource field, float width) {
        ColumnMetadata cm = new ColumnMetadata(this.headerFormat, this.contentFormat, name, field, width);
        this.addSubColumn(cm);
        return cm;
    }

    /**
     * 在此標題欄位之下, 新增一個子標題 (使用預設寬度比重 10) .
     * 
     * @param name 此欄位標題列所顯示的文字.
     * @return 所產生的子標題.
     */
    public ColumnMetadata newSubColumn(String name, CellDataSource field) {
        ColumnMetadata cm = new ColumnMetadata(this.headerFormat, this.contentFormat, name, field);
        this.addSubColumn(cm);
        return cm;
    }

    public ColumnMetadata newSubColumn(String name, float width) {
        ColumnMetadata cm = new ColumnMetadata(this.headerFormat, this.contentFormat, name, null, width);
        this.addSubColumn(cm);
        return cm;
    }

    /**
     * 在此標題欄位之下, 新增一個子標題 (無對應xml欄位,使用預設寬度比重 10) .
     * 
     * @param name 此欄位標題列所顯示的文字.
     * @return 所產生的子標題.
     */
    public ColumnMetadata newSubColumn(String name) {
        ColumnMetadata cm = new ColumnMetadata(this.headerFormat, this.contentFormat, name);
        this.addSubColumn(cm);
        return cm;
    }

    protected boolean addSubColumn(ColumnMetadata col) {
        if (this.subColumns == null) {
            this.subColumns = new ArrayList<ColumnMetadata>();
        }

        if (col.parent == null) {
            col.parent = this;
            this.subColumns.add(col);
            col.resetMetadata();
            // col.resetStartIndex();
            return true;
        } else {
            return false;
        }
    }

    public ColumnMetadata getParent() {
        try {
            return this.parent;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the subColumns
     */
    public List<ColumnMetadata> getSubColumns() {
        return this.subColumns;
    }

    /** . */
    protected boolean createdByFunctor = false;

    protected void setCreatedByFunctor() {
        this.createdByFunctor = true;
    }

    // ====
    // == [Method] Block Stop
    // ================================================
    // == [Main Method] Block Start
    // ====
    // public static void main(String[] args) {
    //	    
    // }
    // ====
    // == [Main Method] Block Stop
    // ================================================

    /*
     * 設定此欄位的資料格在資料相同時, 是否會與上下列合併為同一儲存格, 目前此功能尚未實作. private void setSpan(boolean
     * s){ this.span = s; }
     */

    /** 傳回此群組中, 位於第 level 層的欄位標題. */
    protected void getAllColumns(int lv, List<ColumnMetadata> v) {

        if (lv == this.headerLevel) {// 到達相符的lv時,
            v.add(this);
        } else if (lv > this.headerLevel) {
            if (!this.hasChildren()) {
                return;
            } // 如果有子欄位的話, 還要再往下找
            Iterator<ColumnMetadata> i = this.subColumns.iterator();
            while (i.hasNext()) {
                ((ColumnMetadata) i.next()).getAllColumns(lv, v);
            }
        } else {
            // 不應該會執行到此
        }
    }

    /**
     * 取得此欄位標題所在的分支的最大層數.
     * 
     * @see NormalTableMetadata
     */
    public int getMaxHeaderLevel(boolean reCount) {

        if ((this.maxHeaderLevel > 0) && !reCount) {
            return this.maxHeaderLevel;
        }

        if (!this.hasChildren()) {
            return this.headerLevel;
        }
        this.maxHeaderLevel = this.headerLevel;
        Iterator<ColumnMetadata> i = this.subColumns.iterator();
        while (i.hasNext()) {
            int tmp = ((ColumnMetadata) i.next()).getMaxHeaderLevel(reCount);
            if (tmp > this.maxHeaderLevel)
                this.maxHeaderLevel = tmp;
        }
        return this.maxHeaderLevel;
    }

    /**
     * 取得此標題所佔用的欄位總數
     */
    public int getColsNumber() {

        if (this.hasChildren()) { // 有子欄位, 直接使用子欄位的寬度和
            int w = 0;
            for (Iterator<ColumnMetadata> i = this.subColumns.iterator(); i.hasNext();) {
                w += ((ColumnMetadata) (i.next())).getColsNumber();
            }
            return w;
        } else {
            return 1;
        }
    }

    /**
     * 取得最下層的所有 column (以最下層的所有欄位計算).
     */
    protected void getDataCols(List<ColumnMetadata> v) {

        if (this.hasChildren()) { // 有子欄位,
            for (Iterator<ColumnMetadata> i = this.subColumns.iterator(); i.hasNext();) {
                i.next().getDataCols(v);
            }
        } else {
            v.add(this);
        }
    };

    protected int getWidths(float[] widths, int index) {

        if (this.hasChildren()) { // 有子欄位, 直接使用子欄位的寬度和
            for (Iterator<ColumnMetadata> i = this.subColumns.iterator(); i.hasNext();) {
                index = i.next().getWidths(widths, index);
            }
            return index;
        } else {
            widths[index] = this.width;
            return index + 1; // 計算下一個欄位寬
        }
    }

    /**
     * 同時更新層數及完整路徑名稱.
     * 
     * 其實, 把加入子欄位的方式改為呼叫newsubcolumn以後, 此函式似乎便只會各使用一次了. 而其它是 getMaxLevel 之類的函式, 也可以在建構時順便計算其值, 但, 實際效益應該是沒有太大的差別. 因為,
     * 在PDFDocBuilder中, 那些method被呼叫的次數也都僅有一次而已.
     */
    protected void resetMetadata() {
        this.headerLevel = this.getParent().headerLevel + 1;
        if (!this.hasChildren()) {
            return;
        }

        for (Iterator<ColumnMetadata> iter = this.subColumns.iterator(); iter.hasNext();) {
            ColumnMetadata cm = (ColumnMetadata) iter.next();
            cm.resetMetadata();
        }

    }

    /*
     * protected ColumnMetadata getParent(){ try { return ( ColumnMetadata
     * )this.parent; }catch (Exception e){ return null; } }
     */

    private boolean hasChildren() {
        if (this.subColumns == null)
            return false;
        if (this.subColumns.size() == 0)
            return false;
        return true;
    }

    // ====================================================================

    /**
     * 
     * @param contentAlignV
     * @see com.lowagie.text.Element
     */
    /*
     * public boolean isFirst() { return (startIndex==0); }
     * 
     * public int getStartIndex() { return startIndex; }
     * 
     * public void resetStartIndex(){ ColumnMetadata root = this; while (
     * root.parent!=null) { root = ( ColumnMetadata ) root.getParent(); } // 取得
     * root 元素 root.resetStartIndex(0); }
     * 
     * private int resetStartIndex(int sIndex){ this.startIndex = sIndex; int si =
     * 0; if ( this.hasChildren() ) { for ( int i = 0; i < subColumns.size();
     * i++ ) { ColumnMetadata item = ( ColumnMetadata ) subColumns.get( i ); si =
     * item.resetStartIndex(si); } return sIndex + si; } else { return sIndex +
     * 1; } }
     */

    public void setNewLine(boolean newLine) {
        this.newLine = newLine;
        ColumnMetadata root = this;
        while (root.parent != null) {
            root = root.getParent();
        } // 取得 root 元素
        root.resetMultiLine();
    }

    private void resetMultiLine() {

    }

    // ================================================
    // == [static variables] Block Start
    // ====
    // ====
    // == [static variables] Block Stop
    // ================================================
    // == [instance variables] Block Start
    // ====

    // protected boolean span = false;

    // ====
    // == [instance variables] Block Stop
    // ================================================
    // == [static Constructor] Block Start
    // ====
    // ====
    // == [static Constructor] Block Stop
    // ================================================
    // == [Constructors] Block Start (含init method)
    // ====
    // ====
    // == [Constructors] Block Stop
    // ================================================
    // == [Overrided Method] Block Start (toString/equals+hashCode)
    // ====
    // ====
    // == [Overrided Method] Block Stop
    // ================================================
    // == [Accessor] Block Start
    // ====

    /**
     * @return 傳回 contentFormat。
     */
    public CellFormat getContentFormat() {
        if (this.mutableContentFormat == null) {
            this.mutableContentFormat = (CellFormat) this.contentFormat.clone();
        }
        return this.mutableContentFormat;
    }

    /**
     * @return 傳回 headerFormat。
     */
    public CellFormat getHeaderFormat() {
        if (this.mutableHeaderFormat == null) {
            this.mutableHeaderFormat = (CellFormat) this.headerFormat.clone();
        }
        return this.mutableHeaderFormat;
    }

    public int getMerge() {
        return this.merge;
    }

    public boolean isNewLine() {
        return this.newLine;
    }

    /**
     * 設定此欄位的資料是否會與上下列合併. 傳入值表示在欄位中, 一行文字將顯示幾筆資料, 例如資料包含a~g, 此值設定為3, 則顯示 :
     * 
     * <pre>
     *          a, b, c,
     *          d, e, f
     *          g
     * </pre>
     * 
     * @param m 設定值, 此值大於0 即會對欄位資料進行合併.
     */
    public void setMerge(int m) {
        this.merge = m;
    }

    public GroupingFunction getGroupFunction() {
        return this.groupFunction;
    }

    public void setGroupFunction(GroupingFunction groupFunction) {
        this.groupFunction = groupFunction;
    }

    public void setGroupFunction(GroupingFunction groupFunction, String groupingPrefix, String groupingPostfix) {
        this.groupFunction = groupFunction;
        this.groupingPrefix = groupingPrefix;
        this.groupingPostfix = groupingPostfix;
    }

    /**
     * @return 傳回 groupingPostfix。
     */
    public final String getGroupingPostfix() {
        return this.groupingPostfix;
    }

    /**
     * @return 傳回 groupingPrefix。
     */
    public final String getGroupingPrefix() {
        return this.groupingPrefix;
    }

    public void split(int n) {
        if (this.subColumns == null || this.subColumns.isEmpty()) {
            final float w = this.width / (float) n;
            for (int i = 0; i < n; i++) {
                this.newSubColumn(null, null, w);
            }
        }
    }
    // ====
    // == [Accessor] Block Stop
    // ================================================
    // == [Static Method] Block Start
    // ====
    // ====
    // == [Static Method] Block Stop
    // ================================================
    // == [Method] Block Start
    // ====
    // ####################################################################
    // ## sub-block : XXXX(可依功能區分)
    // ####################################################################
    // ====
    // == [Method] Block Stop
    // ================================================
    // == [Main Method] Block Start
    // ====
    // public static void main(String[] args) {
    //	    
    // }
    // ====
    // == [Main Method] Block Stop
    // ================================================

}
