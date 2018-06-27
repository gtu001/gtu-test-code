package _temp;

import java.io.IOException;

import gtu.binary.Base64JdkUtil;

public class Test43 {

    public static void main(String[] args) throws InterruptedException, IOException {
        String from = "bi4gKFdhc2hpKeS6uuWQje+8myjml6Up6bmrKOWnkynvvJso5pelKeWSjOW/lyjlkI0p";
        String v = Base64JdkUtil.decode(from);
        System.out.println("--" + v);
        System.out.println("done...");
    }
}
