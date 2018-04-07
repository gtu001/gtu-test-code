package _temp;

public class Test_CharToAscii {

    public static void main(String[] args) {
        char a = '哈';
        
        System.out.println((int)a);
        
        System.out.println(Integer.toHexString(a));
        
        System.out.println((char)0x54c8);
//        \u54C8
//        System.out.println( "\\u" + Integer.toHexString('÷' | ).substring(1) );
    }

}
