package gtu.iflogic;

import java.io.Serializable;

public class PairOperatorCheck implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    String left;
    String right;
    String middle;

    void clean() {
        left = null;
        right = null;
        middle = null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PairOperatorCheck [left=" + left + ", right=" + right + ", middle=" + middle + "]";
    }
}