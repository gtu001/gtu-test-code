package _temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;

public class Test40 {

    public static void main(String[] args) throws InterruptedException, IOException {
        String text = SystemInUtil.readContent();
        String result = URLDecoder.decode(text, "utf-8");
        System.out.println(result);
        System.out.println("done...");
    }

}
