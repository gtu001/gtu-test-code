package gtu.itext.iisi.table;

import gtu.itext.iisi.table.func.FuncCONST;
import gtu.itext.iisi.table.func.FuncCOUNT;
import gtu.itext.iisi.table.func.FuncSUM;
import gtu.itext.iisi.table.func.GroupingFunctionProcessor;

public abstract class GroupingFunction {

    final public static GroupingFunction SUM = new GroupingFunction() {
        @Override
        public GroupingFunctionProcessor create() {
            return new FuncSUM();
        };
    };

    final public static GroupingFunction COUNT = new GroupingFunction() {
        @Override
        public GroupingFunctionProcessor create() {
            return new FuncCOUNT();
        };
    };

    final public static GroupingFunction BLANK =  new GroupingFunction() {
        @Override
        public GroupingFunctionProcessor create() {
            return new FuncCONST("");
        };
    };
    
    private GroupingFunction() {
    }

    // 
    // 建立計算過程所用處理器. 
    //
    public abstract GroupingFunctionProcessor create();
}
