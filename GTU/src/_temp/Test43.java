package _temp;

import java.io.IOException;

import gtu.binary.Base64JdkUtil;

public class Test43 {

    public static void main(String[] args) throws InterruptedException, IOException {
        String from = "bi4g6L6p6K665pyv77yb6K666K+B5rOV77yIcG9sZW1pY+eahOWkjeaVsO+8iQ";
        String v = Base64JdkUtil.decode(from);
        System.out.println("--" + v);
        System.out.println("done...");
    }
}
