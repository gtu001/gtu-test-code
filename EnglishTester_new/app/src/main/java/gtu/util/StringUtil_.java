package gtu.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2017/11/8.
 */

public class StringUtil_ {

    public static boolean hasChinese(String str){
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
}
