package gtu.binary;

import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;

public class AsciiCode {

    public static void main(String[] args) throws UnsupportedEncodingException {
        t1();//ASCII转换为字符串
        
        t2();//字符串转换为ASCII码
    }

    public static void t1() {//ASCII转换为字符串

        String s = "22307 35806 24555 20048";//ASCII码

        String[] chars = s.split(" ");
        System.out.println("ASCII 汉字 \n----------------------");
        for (int i = 0; i < chars.length; i++) {
            System.out.println(chars[i] + " " + (char) Integer.parseInt(chars[i]));
        }
    }

    public static void t2() {//字符串转换为ASCII码

        String s = "新年快乐！";//字符串

        char[] chars = s.toCharArray(); //把字符中转换为字符数组

        System.out.println("\n\n汉字 ASCII\n----------------------");
        for (int i = 0; i < chars.length; i++) {//输出结果

            System.out.println(" " + chars[i] + " " + (int) chars[i]);
        }
    }
}
