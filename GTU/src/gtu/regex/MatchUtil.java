package gtu.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtil {

    public static boolean findMatch(String ptnStr, String line) {
        try {
            Pattern ptn = Pattern.compile(ptnStr, Pattern.MULTILINE | Pattern.DOTALL);
            Matcher mth = ptn.matcher(line);
            return mth.find();
        } catch (Exception ex) {
            throw new RuntimeException("err : " + line + ", ptn : " + ptnStr, ex);
        }
    }

    public static String getMatch(String ptnStr, String line) {
        try {
            Pattern ptn = Pattern.compile(ptnStr, Pattern.MULTILINE | Pattern.DOTALL);
            Matcher mth = ptn.matcher(line);
            mth.find();
            return mth.group(1);
        } catch (Exception ex) {
            throw new RuntimeException("err : " + line + ", ptn : " + ptnStr, ex);
        }
    }

}
