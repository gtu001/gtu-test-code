package gtu.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2017/11/8.
 */

public class StringUtil_ {

    public static boolean hasChinese(String str) {
        return StringUtils.isNotBlank(getChineseWord(str, true));
    }

    /**
     * 抓中文字(注音不抓)
     *
     * @param str
     * @param isChineseBool
     * @return
     */
    public static String getChineseWord(String str, boolean isChineseBool) {
        String isChinese = isChineseBool ? "" : "^";
        Pattern chinesePattern = Pattern.compile("[" + isChinese + "\u4e00-\u9fa5]");
        StringBuilder sb = new StringBuilder();
        Matcher mth = chinesePattern.matcher(str);
        while (mth.find()) {
            sb.append(mth.group());
        }
        return sb.toString();
    }


    public static String appendReplacementEscape(String content) {
        try {
            return new AppendReplacementEscaper(content).getResult();
        } catch (Exception ex) {
            throw new RuntimeException(String.format("[appendReplacementEscape][content]\n ERR : %s \n %s", //
                    ex.getMessage(), content), ex);
        }
    }

    private static class AppendReplacementEscaper {
        String content;
        String result;

        AppendReplacementEscaper(String content) {
            this.content = content;
            result = StringUtils.trimToEmpty(content).toString();
            result = replaceChar(result, '$');
            result = replaceChar(result, '/');
        }

        private String replaceChar(String content, char from) {
            StringBuffer sb = new StringBuffer();
            char[] arry = StringUtils.trimToEmpty(content).toCharArray();
            for (int ii = 0; ii < arry.length; ii++) {
                char a = arry[ii];
                if (a == from && (ii - 1) >= 0 && arry[ii - 1] != '\\') {
                    sb.append("\\" + a);
                } else {
                    sb.append(a);
                }
            }
            return sb.toString();
        }

        private String getResult() {
            return result;
        }
    }
}
