/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.table.func;

/**
 * 輸出固定值.
 * 
 * @author 920111 
 *         在 2008/8/13 建立
 */
public class FuncCONST  implements GroupingFunctionProcessor {

    private Object defaultValue;
    
    /**
     * @param defaultValue
     */
    public FuncCONST(Object defaultValue) {
        super();
        this.defaultValue = defaultValue;
    }
    /**
     * @see nca.util.doc.table.func.GroupingFunctionProcessor#addItem(java.lang.Object)
     */
    @Override
    public void addItem(Object object) {
    }

    /**
     * @see nca.util.doc.table.func.GroupingFunctionProcessor#getResult()
     */
    @Override
    public Object getResult() {
        return this.defaultValue;
    }

    /**
     * @see nca.util.doc.table.func.GroupingFunctionProcessor#clear()
     */
    @Override
    public void clear() {
    }

}

