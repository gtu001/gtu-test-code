package _temp;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Test55 {

    public static void main(String[] args) throws IOException {
        // List<String> lst1 = new ArrayList<String>();
        // List<String> lst2 = new ArrayList<String>();
        //
        // lst1.add("111");
        // lst1.add("333");
        // lst2.add("111");
        // lst2.add("333");
        //
        // boolean test = lst1.retainAll(lst2);
        // System.out.println(test);
        // System.out.println(lst1);

        String str = "取得本日";
        // char[] ars = str.toCharArray();
        // for (char c : ars) {
        // System.out.println((int) c);
        // }

        byte[] bs = str.getBytes("UTF8");
        for (byte b : bs) {
            System.out.println(b);
        }

        String str1 = URLDecoder.decode("\\345\\217\\226\\345\\276\\227\\346\\234\\254\\346\\227\\245");
        System.out.println(str1);

        System.out.println("done...");
    }
}
