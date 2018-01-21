/*
 * Copyright (c) 2011. 資拓科技. All right reserved.
 */
package gtu.itext.iisi.data.op;

import gtu.itext.iisi.data.HorizontalExpression;
import gtu.itext.iisi.data.op.number.Addition;
import gtu.itext.iisi.data.op.number.ColNumber;
import gtu.itext.iisi.data.op.number.Division;
import gtu.itext.iisi.data.op.number.Multiplication;
import gtu.itext.iisi.data.op.number.Subtraction;
import gtu.itext.iisi.table.ColumnMetadata;

import java.math.BigDecimal;
import java.util.ArrayList;

public abstract class NumberOP extends HorizontalExpression<BigDecimal> {

    public NumberOP(String operate) {
        super(operate);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public NumberOP(String operate, HorizontalExpression<BigDecimal> rp, HorizontalExpression<BigDecimal> lp) {
        super(operate);
        this.children = new ArrayList<HorizontalExpression<Object>>(2);
        this.children.add((HorizontalExpression) rp);
        this.children.add((HorizontalExpression) lp);
    }

    public NumberOP add(ColumnMetadata cm) {
        NumberOP op = new Addition(this, new ColNumber(cm));
        return op;
    }

    public NumberOP subtract(ColumnMetadata cm) {
        NumberOP op = new Subtraction(this, new ColNumber(cm));
        return op;
    }

    public NumberOP multiply(ColumnMetadata cm) {
        NumberOP op = new Multiplication(this, new ColNumber(cm));
        return op;
    }

    public NumberOP division(ColumnMetadata cm) {
        NumberOP op = new Division(this, new ColNumber(cm));
        return op;
    }

}
