package gtu.itext.iisi.data.op;

import gtu.itext.iisi.data.HorizontalExpression;
import gtu.itext.iisi.table.ColumnMetadata;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

/**
 * 常數資料內容.
 * @author 920111 在 2011/3/18 建立
 */
public class Const<R> extends HorizontalExpression<R> {

    final public static Const<String> EMPTY_STRING = new Const<String>("\"\"");

    final public static Const<String> FALSE = new Const<String>("FALSE");

    final public static Const<String> TRUE = new Const<String>("TRUE");

    final public static Const<BigDecimal> ZERO = new Const<BigDecimal>(BigDecimal.ZERO);

    final public static Const<BigDecimal> ONE = new Const<BigDecimal>(BigDecimal.ONE);

    R value;

    public Const(R value) {
        super("");
        this.value = value;
    }

    @Override
    protected R myEval(List<Object> results) {
        return this.value;
    }

    //####################################################################
    //## [Method] sub-block : 運算式顯示
    //####################################################################

    /**
     * 取得用於 Excel 中的.
     * 
     * @param colsIndexMapping
     * @param rowIndex
     * @param values
     */
    @Override
    public String showAsExcel(Map<ColumnMetadata, String> colsIndexMapping, ColumnMetadata nowColumn, int rowIndex) {
        return ObjectUtils.toString(this.value);
    }

    /**
     * @return
     */
    @Override
    public String showMe(ColumnMetadata nowColumn) {
        return ObjectUtils.toString(this.value);
    }

}
