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
        String aaa1;
        String bbb1;
        String aaa2;
        String bbb2;
        String aaa3;
        String bbb3;
        String aaa4;
        String bbb4;
        String aaa5;
        String bbb5;
        String aaa6;
        String bbb6;
        String aaa7;
        String bbb7;
        String aaa8;
        String bbb8;
        String aaa9;
        String bbb9;
        String aaa10;
        String bbb10;
        String aaa11;
        String bbb11;
        String aaa12;
        String bbb12;
        String aaa13;
        String bbb13;
        String aaa14;
        String bbb14;
    }
}
