/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.table.func;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 處理分組總和
 *
 *  @author 920111 
 *         在 2008/5/19 建立
 */
public class FuncSUM implements GroupingFunctionProcessor {
    long sum;

    /**
     * @see nca.util.doc.table.func.GroupingFunctionProcessor#addItem(java.lang.Number)
     */
    @Override
    public void addItem(Object value) {
        if (value instanceof Number) {
            this.sum += ((Number) value).longValue();
        } else {
            final String strValue = ObjectUtils.toString(value);
            if (NumberUtils.isDigits(strValue)) {
                this.sum += NumberUtils.toLong(strValue);
            }
        }
    }

    /**
     * @see nca.util.doc.table.func.GroupingFunctionProcessor#getResult()
     */
    @Override
    public Object getResult() {
        return new Long(this.sum);
    }

    @Override
    public void clear() {
        this.sum = 0;
    }
}
