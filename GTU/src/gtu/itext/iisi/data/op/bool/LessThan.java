/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.data.op.bool;

import gtu.itext.iisi.data.HorizontalExpression;
import gtu.itext.iisi.data.op.BooleanOP;

import java.util.List;

public class LessThan extends BooleanOP {

    public LessThan(HorizontalExpression<? extends Object> rp, HorizontalExpression<? extends Object> lp) {
        super("<", rp, lp);
    }

    @Override
    protected Boolean myEval(List<Object> results) {
        return Boolean.FALSE;
    }
}
