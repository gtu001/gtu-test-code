/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.data;

import gtu.itext.iisi.data.op.NumberOP;
import gtu.itext.iisi.data.op.number.ColNumber;
import gtu.itext.iisi.table.ColumnMetadata;
import gtu.itext.iisi.table.NormalTableMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 用於表示水平列公式，如 (=A1+B1)
 *
 * @param R 回傳值類別.
 * @param O 參數值類別
 * @author 920111 在 2011/3/18 建立
 */
public abstract class HorizontalExpression<R> implements CellDataSource {

    /***/
    protected String operate;

    /**
     * 子項應回傳 {@code <O>} ，以提供本身進行運算.
     */
    @SuppressWarnings("unchecked")
    protected List<HorizontalExpression<Object>> children = (List<HorizontalExpression<Object>>) Collections.EMPTY_LIST;

    //====
    //== [instance variables] Block Stop
    //================================================
    //== [static Constructor] Block Start
    //====

    public HorizontalExpression(String operate) {
        this.operate = operate;
    }

    //====
    //== [static Constructor] Block Stop
    //================================================
    //== [Constructors] Block Start (含init method)
    //====
    //====
    //== [Constructors] Block Stop
    //================================================
    //== [Overrided Method] Block Start (toString/equals+hashCode)
    //====

    @Override
    public void reset() {
    };
    
    /**
     * @see nca.util.doc.data.HExpression#eval(java.lang.Object)
     */
    @Override
    public Object eval(Object dataObj) {
        return null;
    }

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

    /**
     * 取得計算資料中的第一個值，但若其中有任一項目無法被計算(null)，則也回傳 NULL 
     * @return
     */
    final protected Object lookupFirstOp(List<Object> values) {
        if (values == null || values.size() == 0) {
            return null;
        }
        for (Object value : values) {
            if (value == null) {
                return null;
            }
        }
        return values.get(0);
    }

    /**
     * @see nca.util.doc.data.HExpression#evalAsValue(nca.util.doc.table.ColumnMetadata[], java.lang.Object[])
     */
    public R evalAsValue(ColumnMetadata[] colsMeta, Object[] rawValues) {
        List<Object> results = new ArrayList<Object>();
        for (HorizontalExpression<? extends Object> child : this.children) {
            results.add(child.evalAsValue(colsMeta, rawValues));
        }
        return myEval(results);
    }

    /**
     * 執行實際運算.
     */
    abstract protected R myEval(List<Object> results);

    //####################################################################
    //## [Method] sub-block : 運算式顯示
    //####################################################################

    /**
     * @see nca.util.doc.data.HExpression#showAsExcel(java.util.Map, nca.util.doc.table.ColumnMetadata, int)
     */
    public String showAsExcel(Map<ColumnMetadata, String> colsIndexMapping, ColumnMetadata nowColumn, int rowIndex) {
        if (this.children == null || this.children.size() == 0) {
            return ""; // 取值運算/Function運算應自行實作.
        }
        final String[] ch = new String[this.children.size()];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = this.children.get(i).showAsExcel(colsIndexMapping, nowColumn, rowIndex);
        }
        return "(" + StringUtils.join(ch, this.operate) + ")";
    }

    /**
     * @see nca.util.doc.data.HExpression#showMe(nca.util.doc.table.ColumnMetadata)
     */
    public String showMe(ColumnMetadata nowColumn) {
        if (this.children == null || this.children.size() == 0) {
            return ""; // 取值運算/Function運算應自行實作.
        }
        final String[] ch = new String[this.children.size()];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = this.children.get(i).showMe(nowColumn);
        }
        return "(" + StringUtils.join(ch, this.operate) + ")";
    }

    //====
    //== [Method] Block Stop
    //================================================
    //== [Main Method] Block Start
    //====
    @SuppressWarnings("unused")
    public static void main(String[] args) {

        NormalTableMetadata table = new NormalTableMetadata("測試表格1");
        //第一層
        ColumnMetadata col_1 = table.newSubColumn("Data1", new CellBeanProperty("Data1"), 10);
        ColumnMetadata col_2 = table.newSubColumn("Data2", new CellBeanProperty("Data2"), 10);
        ColumnMetadata col_3 = table.newSubColumn("Data3", new CellBeanProperty("Data3"), 10);
        ColumnMetadata col_4 = table.newSubColumn("Data4", new CellBeanProperty("Data4"), 10);
        ColumnMetadata col_5 = table.newSubColumn("Data5", new CellBeanProperty("Data5"), 10);

        //        HorizontalExpression a = new ColMeta(col_1)//
        //                .add(col_2)//
        //                .add(col_3)//
        //                .subtract(col_4);
        {
            NumberOP numberOP = new ColNumber(col_1)//
                    .add(col_2)//
                    .add(col_3)//
                    .subtract(col_4);
            
            //System.out.println(numberOP.showMe(col_5));
            //System.out.println(numberOP.showAsExcel(colsIndexMapping, nowColumn, rowIndex))(col_5));
        }

    }
    //====
    //== [Main Method] Block Stop
    //================================================

}
