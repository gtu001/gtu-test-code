package _temp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test59 {

    public static void main(String[] args) {
        Pattern ptn = Pattern.compile("\\_pct$");
        Matcher mth = ptn.matcher("Larry_pct");
        while (mth.find()) {
            System.out.println("FIND_OK");
        }
        System.out.println("done..");
    }
}
