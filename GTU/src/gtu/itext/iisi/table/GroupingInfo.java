package gtu.itext.iisi.table;

import java.awt.Color;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 */
public class GroupingInfo {

    //================================================
    //== [static variables] Block Start
    //====

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====
    /**
     * 記錄各層級分組資訊.
     */
    public class GroupLevel {

        String postfix;

        ColumnMetadata dataColumn;

        //TreeSet levelItemRows = new TreeSet();

        private GroupLevel(String postfix) {
            this.postfix = postfix;
        }

        /**
         * @return 傳回 dataColumn。
         */
        public ColumnMetadata getDataColumn() {
            return this.dataColumn;
        }

        /**
         * @return 傳回 postfix。
         */
        public String getPostfix() {
            return this.postfix;
        }
    }

    ///** */
    //private int excelRow = 0;

    /** 現有分組層級. */
    private GroupLevel[] groups = { new GroupLevel("總合計：") };

    /** 標題格式. 用於 Master-Detail 形式，顯示分組資訊. */
    private CellFormat headerFormat = createDefaultHeaderFormat();

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
     * 所有資料總計列的文字說明, 預設為 "總合計："
     */
    public GroupingInfo() {
    }

    /**
     * @param titleALL
     *            String 所有資料總計列的文字說明.
     */
    public GroupingInfo(String titleALL) {
        super();
        this.groups[0].postfix = titleALL;
    }

    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (toString/equals+hashCode)
    //====
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < this.groups.length; i++) {
            s += this.groups[i].dataColumn + "," + this.groups[i].postfix + "\n";
        }
        return s;
    }

    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Accessor] Block Start
    //====
    /**
     * @return 傳回 groups。
     */
    public GroupLevel[] getGroups() {
        return this.groups;
    }

    /**
     * @return 傳回 headerFormat。
     */
    public final CellFormat getHeaderFormat() {
        return this.headerFormat;
    }

    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    /**
     * 預設標題格式, 靠左, 無格線 
     * @return
     */
    private static CellFormat createDefaultHeaderFormat() {
        CellFormat cf = new CellFormat();
        cf.setBorder(CellFormat.RECTANGLE_NO_BORDER);
        cf.setAlignH(CellFormat.ALIGN_LEFT);
        cf.setAlignV(CellFormat.ALIGN_MIDDLE);
        cf.setBackgroundColor(Color.WHITE);
        cf.setFontSize(14);
        return cf;
    }

    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====

    /** 取得指定層級的 group 方式. */
    public GroupLevel getGroupLevel(int lv) {
        if (ArrayUtils.getLength(this.groups) > lv) {
            return this.groups[lv];
        }
        return null;
    }

    //####################################################################
    //## [Method] sub-block : 建立新層級物件.   
    //####################################################################

    public GroupLevel addGroupLevel(String titlePostfix, ColumnMetadata dataColumn) {
        GroupLevel[] tmp = new GroupLevel[this.groups.length + 1];
        System.arraycopy(this.groups, 0, tmp, 0, this.groups.length);
        GroupLevel gl = new GroupLevel(titlePostfix);
        gl.dataColumn = dataColumn;
        tmp[this.groups.length] = gl;
        this.groups = tmp;
        return gl;
    }

    //====
    //== [Method] Block Stop 
    //================================================
    //== [Main Method] Block Start
    //====
    //====
    //== [Main Method] Block Stop 
    //================================================

}
