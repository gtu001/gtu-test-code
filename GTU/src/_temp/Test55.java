package _temp;

import java.io.IOException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import gtu.reflect.sun.ReflectionTest;

public class Test55 {

    public static void main(String[] args) throws IOException {
        Test55 t = new Test55();
        for (int ii = 0; ii < 10; ii++) {
            TTT tt = t.new TTT();
            tt.aaa = "xx" + ii;
            tt.bbb = "bb" + ii;
            System.out.println(ReflectionToStringBuilder.toString(tt));
        }
        System.out.println("done...");
    }

    private class TTT {
        String aaa;
        String bbb;
    }
}
