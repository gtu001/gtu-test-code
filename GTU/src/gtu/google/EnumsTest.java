package gtu.google;

import com.google.common.base.Enums;
import com.google.common.base.Function;
import com.google.common.base.Optional;



public class EnumsTest {
    
    enum E {
        A,B,C;
    }

    public static void main(String[] args) {
        System.out.println("1=" + Enums.getField(E.A));
        
        Optional<E> op = Enums.getIfPresent(E.class, "C");
        System.out.println("2=" + op + "->" + (op.isPresent()?op.get():"absent"));
        
        Function<String, E> fun = Enums.valueOfFunction(E.class);
        System.out.println("3=" + fun + "->" + fun.apply("A"));
        System.out.println();
    }
}
