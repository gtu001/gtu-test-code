/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.table.func;

/**
 * 處理分組總和
 *
 *  @author 920111 
 *         在 2008/5/19 建立
 */
public class FuncCOUNT implements GroupingFunctionProcessor {

    int count;

    /**
     * @see nca.util.doc.table.func.GroupingFunctionProcessor#addItem(java.lang.Number)
     */
    @Override
    public void addItem(Object value) {
        this.count++;
    }

    /**
     * @see nca.util.doc.table.func.GroupingFunctionProcessor#getResult()
     */
    @Override
    public Object getResult() {
        return new Integer(this.count);
    }

    /**
     * @see nca.util.doc.table.func.GroupingFunctionProcessor#clear()
     */
    @Override
    public void clear() {
        this.count = 0;
    }
}
