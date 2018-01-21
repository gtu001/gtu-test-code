package gtu.collection;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class EnumSetTest {

    private enum Test {
        AAAA, BBBB, CCCC
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //隨便加入會按照Enum本身的順序顯示
        Set<Test> set1 = EnumSet.of(Test.CCCC, Test.BBBB);
        System.out.println(set1);
        
        Set<Test> set2 = EnumSet.complementOf(EnumSet.of(Test.CCCC, Test.BBBB));
        System.out.println(set2);
        
        Set<Test> set3 = EnumSet.allOf(Test.class);
        System.out.println(set3);
    }
}
