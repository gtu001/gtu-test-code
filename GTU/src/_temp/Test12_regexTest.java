package _temp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test12_regexTest {

    public static void main(String args[]) throws Exception {
        Test12_regexTest test = new Test12_regexTest();

        String text = "";
        String pattern = "";

        Pattern ptn = Pattern.compile(pattern);

        Matcher mth = ptn.matcher(text);
        while (mth.find()) {
            for (int ii = 0; ii < mth.groupCount(); ii++) {
                System.out.println(ii + " -- " + mth.group(ii));
            }
        }

        System.out.println("done...");
    }
}
