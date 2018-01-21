/*
 * Copyright (c) 2011. 資拓科技. All right reserved.
 */
package gtu.itext.iisi.data.op;

import gtu.itext.iisi.data.HorizontalExpression;

import java.util.ArrayList;

public abstract class BooleanOP extends HorizontalExpression<Boolean> {

    public BooleanOP(String operate) {
        super(operate);
    }

    @SuppressWarnings("unchecked")
    public BooleanOP(String operate, HorizontalExpression<? extends  Object> p) {
        super(operate);
        this.children = new ArrayList<HorizontalExpression<Object>>(1);
        this.children.add((HorizontalExpression<Object>) p);
    }

    @SuppressWarnings("unchecked")
    public BooleanOP(String operate, HorizontalExpression<? extends  Object> rp, HorizontalExpression<? extends  Object> lp) {
        super(operate);
        this.children = new ArrayList<HorizontalExpression<Object>>(2);
        this.children.add((HorizontalExpression<Object>) rp);
        this.children.add((HorizontalExpression<Object>) lp);
    }
}
