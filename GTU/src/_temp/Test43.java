package _temp;

import java.io.IOException;

import gtu.binary.Base64JdkUtil;

public class Test43 {

    public static void main(String[] args) throws InterruptedException, IOException {
        
        String val1 = org.springframework.web.util.HtmlUtils.htmlUnescape("&lt;&gt;");
        String val2 = org.springframework.web.util.JavaScriptUtils.javaScriptEscape("&lt;&gt;");
        
        System.out.println("--" + val1);
        System.out.println("--" + val2);
        System.out.println("done...");
    }
}
