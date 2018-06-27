package _temp;

import java.io.IOException;

import gtu.binary.CipherBase64;

public class Test43 {

    public static void main(String[] args) throws InterruptedException, IOException {
        
        String from = "5aSn5pCW5aSn5pO6LOWQueeJmyh2aS4p6Jmb5by16IGy5Yui5ZqH5Lq6LOWkp+aQluWkp+aTuizl\nkLnniZsodnQuKeWah+WUrCzmgavlmocoYS4p5ryC5Lqu55qE\n";
        
//        String v = Base64JdkUtil.decode(from);
        String v = new CipherBase64().decode(from);
        System.out.println("--" + v);
        System.out.println("done...");
    }
}
