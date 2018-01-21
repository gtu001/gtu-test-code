package gtu.string;
import java.net.IDN;

public class IDNDemo {
    public static void main(String[] args) {
        String input = "www.yourName.com";
        String ascii = IDN.toASCII(input);
        String unicode = IDN.toUnicode(input);

        System.out.println("Input:" + input);
        System.out.println("toAscii (input):" + ascii);
        System.out.println("toUnicode (input):" + unicode);
    }
}