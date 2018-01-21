/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.table.func;

/**
 * 分組公式計算處理器.
 * 
 * @author 920111 
 *         在 2008/5/19 建立
 */
public interface GroupingFunctionProcessor {
    
    public void addItem(Object object);

    public Object getResult();
    
    public void clear();
}
