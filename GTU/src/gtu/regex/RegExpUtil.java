package gtu.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {

    public static String match(String ptnStr, String line, int groupIndex, boolean ignoreCase) {
        try {
            int flag = Pattern.MULTILINE | Pattern.DOTALL;
            if (ignoreCase) {
                flag |= Pattern.CASE_INSENSITIVE;
            }
            Pattern ptn = Pattern.compile(ptnStr, flag);
            Matcher mth = ptn.matcher(line);
            if (mth.matches()) {
                return mth.group(groupIndex);
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException("err : " + line + ", ptn : " + ptnStr, ex);
        }
    }

    public static String find(String ptnStr, String line, int groupIndex, boolean ignoreCase) {
        try {
            int flag = Pattern.MULTILINE | Pattern.DOTALL;
            if (ignoreCase) {
                flag |= Pattern.CASE_INSENSITIVE;
            }
            Pattern ptn = Pattern.compile(ptnStr, flag);
            Matcher mth = ptn.matcher(line);
            if (mth.find()) {
                return mth.group(groupIndex);
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException("err : " + line + ", ptn : " + ptnStr, ex);
        }
    }

}
