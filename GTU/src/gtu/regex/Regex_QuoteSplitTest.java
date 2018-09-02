package gtu.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex_QuoteSplitTest {

    public static void main(String[] args) {
        String content = "green,\"yellow,green\",white,orange,\"blue,black\"";
        {
            Pattern ptn = Pattern.compile("(?<=\\\")\b[a-z,]+\b(?=\")|[a-z]+", Pattern.CASE_INSENSITIVE);
            Matcher mth = ptn.matcher(content);
            while(mth.find()){
                System.out.println(mth.group());
            }
        }
        System.out.println("----------------------------------------------------");
        {
            Pattern ptn = Pattern.compile("(\"\"?)\b[a-z,]+\b(\"\"?)|[a-z]+", Pattern.CASE_INSENSITIVE);
            Matcher mth = ptn.matcher(content);
            while(mth.find()){
                System.out.println(mth.group());
            }
        }
        System.out.println("----------------------------------------------------");
        {
            Pattern ptn = Pattern.compile("(?<=\")[a-z,\\s]+(?=\")|[a-z\\s]+", Pattern.CASE_INSENSITIVE);
            Matcher mth = ptn.matcher(content);
            while(mth.find()){
                System.out.println(mth.group());
            }
        }
    }

}
