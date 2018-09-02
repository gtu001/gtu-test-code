/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.data.op;

import gtu.itext.iisi.data.HorizontalExpression;
import gtu.itext.iisi.table.ColumnMetadata;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

public class ColValue extends HorizontalExpression<Object> {

    /** 列差，預設取得本列值為 0. */
    final private int rowDiff;

    /** 對象欄位. */
    private ColumnMetadata cm;

    public ColValue(ColumnMetadata cm) {
        this(cm, 0);
    }

    public ColValue(ColumnMetadata cm, int rowDiff) {
        super("");
        this.cm = cm;
        this.rowDiff = rowDiff;
    }

    public void setCm(ColumnMetadata cm) {
        this.cm = cm;
    }

    /**
     * 
     */
    @Override
    public BigDecimal evalAsValue(ColumnMetadata[] colsMeta, Object[] values) {
        if (this.rowDiff != 0) {
            //throw new UnsupportedOperationException("未支援跨列取值(僅用於 Excel)");
            return null;
        }
        int index = ArrayUtils.indexOf(colsMeta, this.cm);
        if (index < 0) {
            return null;
        }
        if (values[index] instanceof Number) {
            return new BigDecimal(((Number) values[index]).toString());
        }
        return null;
    }

    @Override
    public String showAsExcel(Map<ColumnMetadata, String> colsIndexMapping, ColumnMetadata nowColumn, int rowIndex) {
        if (this.cm == null) {
            final String ci = colsIndexMapping.get(nowColumn);
            return ci + (rowIndex + 1 + this.rowDiff);
        } else {
            final String ci = colsIndexMapping.get(this.cm);
            return ci + (rowIndex + 1 + this.rowDiff);

        }
    }

    @Override
    public String showMe(ColumnMetadata nowColumn) {
        return (this.cm == null) ? nowColumn.getName() : this.cm.getName();
    }

    @Override
    protected Object myEval(List<Object> results) {
        throw new UnsupportedOperationException("取值類別不應有子項結果，也不需執行至計算函式.");
    }
}
