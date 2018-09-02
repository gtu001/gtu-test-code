package gtu.itext.iisi.data.op;

import gtu.itext.iisi.data.HorizontalExpression;
import gtu.itext.iisi.table.ColumnMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ExcelFunction<R> extends HorizontalExpression<R> {

    @SuppressWarnings("unchecked")
    public ExcelFunction(String operate, HorizontalExpression<? extends Object> p) {
        super(operate);
        this.children = new ArrayList<HorizontalExpression<Object>>(1);
        this.children.add((HorizontalExpression<Object>) p);
    }

    @SuppressWarnings("unchecked")
    public ExcelFunction(String operate, HorizontalExpression<? extends Object> p1, HorizontalExpression<? extends Object> p2) {
        super(operate);
        this.children = new ArrayList<HorizontalExpression<Object>>(2);
        this.children.add((HorizontalExpression<Object>) p1);
        this.children.add((HorizontalExpression<Object>) p2);
    }

    @SuppressWarnings("unchecked")
    public ExcelFunction(String operate, HorizontalExpression<? extends Object> p1, HorizontalExpression<? extends Object> p2,
            HorizontalExpression<? extends Object> p3) {
        super(operate);
        this.children = new ArrayList<HorizontalExpression<Object>>(2);
        this.children.add((HorizontalExpression<Object>) p1);
        this.children.add((HorizontalExpression<Object>) p2);
        this.children.add((HorizontalExpression<Object>) p3);
    }

    @Override
    protected R myEval(List<Object> results) {
        // ########  暫時不支援各種 Function 直接運算待結果 for PDF/CSV....
        return null;
    }

    //####################################################################
    //## [Method] sub-block : 運算式顯示
    //####################################################################

    /**
     * 取得用於 Excel 中的 function 表示式.
     * 
     * @param colsIndexMapping
     * @param rowIndex
     * @param values
     */
    @Override
    public String showAsExcel(Map<ColumnMetadata, String> colsIndexMapping, ColumnMetadata nowColumn, int rowIndex) {
        if (this.children == null || this.children.size() == 0) {
            return ""; // 取值運算/Function運算應自行實作.
        }
        final String[] ch = new String[this.children.size()];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = this.children.get(i).showAsExcel(colsIndexMapping, nowColumn, rowIndex);
        }
        return this.operate + "(" + StringUtils.join(ch, ",") + ")";
    }

    /**
     * 
     * @return
     */
    @Override
    public String showMe(ColumnMetadata nowColumn) {
        if (this.children == null || this.children.size() == 0) {
            return ""; // 取值運算/Function運算應自行實作.
        }
        final String[] ch = new String[this.children.size()];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = this.children.get(i).showMe(nowColumn);
        }
        return this.operate + "(" + StringUtils.join(ch, ",") + ")";
    }

    //    public static void main(String[] args) {
    //        NormalTableMetadata table = new NormalTableMetadata("測試表格1");
    //        //第一層
    //        ColumnMetadata col_1 = table.newSubColumn("Data1", new CellBeanProperty("Data1"), 10);
    //        ColumnMetadata col_2 = table.newSubColumn("Data2", new CellBeanProperty("Data2"), 10);
    //        ColumnMetadata col_3 = table.newSubColumn("Data3", new CellBeanProperty("Data3"), 10);
    //        ColumnMetadata col_4 = table.newSubColumn("Data4", new CellBeanProperty("Data4"), 10);
    //        ColumnMetadata col_5 = table.newSubColumn("Data5", new CellBeanProperty("Data5"), 10);
    //        Map<ColumnMetadata, String> colsIndexMapping = new HashMap<ColumnMetadata, String>();
    //        colsIndexMapping.put(col_1, "A");
    //        colsIndexMapping.put(col_2, "B");
    //        colsIndexMapping.put(col_3, "C");
    //        colsIndexMapping.put(col_4, "D");
    //        colsIndexMapping.put(col_5, "E");
    //
    //        //        @SuppressWarnings({ "unchecked", "rawtypes" })
    //        NumberOP a = new ColNumber(col_1)//
    //                .add(col_2)//
    //                .add(col_3)//
    //                .subtract(col_4);
    //        ;
    //        ColValue v4 = new ColValue(col_4);
    //
    //        ExcelFunction<Object> excelFunction = new ExcelFunction<Object>("DIFF", a, v4);
    //        //
    //        //
    //        //        //        HorizontalExpression<Object, ?> b;
    //        //
    //        //        List<HorizontalExpression<Object, ? extends Object>> clildren = new ArrayList<HorizontalExpression<Object, ? extends Object>>();
    //        //
    //        //
    //        //System.out.println(excelFunction.showMe(col_5));
    //        //System.out.println(excelFunction.showAsExcel(colsIndexMapping, col_5, 5));
    //    }

}
