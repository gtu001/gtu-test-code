/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.data.op.number;

import gtu.itext.iisi.data.op.NumberOP;

import java.math.BigDecimal;
import java.util.List;

public class Multiplication extends NumberOP {
    public Multiplication(NumberOP rp, NumberOP lp) {
        super("*", rp, lp);
    }

    @Override
    protected BigDecimal myEval(List<Object> results) {
        BigDecimal db = (BigDecimal) super.lookupFirstOp(results);
        if (db == null) {
            return null;
        }
        for (Object obj : results) {
            BigDecimal number = (BigDecimal) obj;
            db = db.multiply(number);
        }
        return db;
    }

}
