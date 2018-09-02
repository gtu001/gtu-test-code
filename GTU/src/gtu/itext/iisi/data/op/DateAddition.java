/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.data.op;

import gtu.itext.iisi.data.HorizontalExpression;

import java.util.ArrayList;
import java.util.List;

public class DateAddition<T> extends HorizontalExpression<T> {

    @SuppressWarnings("unchecked")
    public DateAddition(HorizontalExpression<T> rp, HorizontalExpression<? extends Object> lp) {
        super("+");
        this.children = new ArrayList<HorizontalExpression<Object>>(2);
        this.children.add((HorizontalExpression<Object>) rp);
        this.children.add((HorizontalExpression<Object>) lp);
    }



    @Override
    protected T myEval(List<Object> results) {
        // 用於 PDF 時有誤.
        return null;  
    }
}
