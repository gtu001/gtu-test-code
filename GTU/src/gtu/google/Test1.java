package gtu.google;

import com.google.common.base.Equivalence;
import com.google.common.base.Equivalences;



public class Test1 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Equivalence e = Equivalences.equals();
        System.out.println(e.equivalent("a", "a"));
    }

}
