package gtu.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTagTest {

    public static void main(String[] args) {
        Pattern ptn = Pattern.compile("(.+)\\s{1}?(.*)");
        Matcher mth = ptn.matcher("xxxxx bbbbbbb");
        while (mth.find()) {
            for (int ii = 0; ii <= mth.groupCount(); ii++) {
                System.out.println(ii + " -- " + mth.group(ii));
            }
        }
        System.out.println("done...");
    }

    private void tag() {
        Pattern ptn = Pattern.compile("\\<.*?\\>");
        Matcher mth = ptn.matcher("<xxxxxx> <yyyyyy>");
        while (mth.find()) {
            System.out.println("--->" + mth.group());
        }
    }
}
