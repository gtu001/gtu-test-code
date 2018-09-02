package gtu.string;

import java.util.ArrayList;
import java.util.List;

/**
 * 處理中文長度(以byte計)
 */
public class ChineseStringUtil {
    /**
     * 計算字串長度
     */
    public static int length(String str) {
        if (str == null) {
            return 0;
        }
        int counts = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).matches("[^\\x00-\\xff]")) {
                counts += 2;
            } else {
                counts++;
            }
        }
        return counts;
    }

    /**
     * 擷取字串
     */
    public static String substring(String str, int begin) {
        return substring(str, begin, Integer.MAX_VALUE);
    }

    /**
     * 擷取字串
     */
    public static String substring(String str, int begin, int end) {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, counts = 0; i < str.length(); i++) {
            int countClone = counts;
            if (str.substring(i, i + 1).matches("[^\\x00-\\xff]")) {
                counts += 2;
            } else {
                counts++;
            }
            if (countClone >= begin && counts <= end) {
                sb.append(str.substring(i, i + 1));
            }
        }
        return sb.toString();
    }

    /**
     * 將字串補足為固定長度
     */
    public static String fixValue(String value, int length, boolean rightPad) {
        if (value == null) {
            value = "";
        }
        int currentLen = length(value);
        if (currentLen < length) {
            StringBuilder sb = new StringBuilder();
            if (rightPad) {
                sb.append(value);
            }
            for (int ii = 0; ii < (length - currentLen); ii++) {
                sb.append(" ");
            }
            if (!rightPad) {
                sb.append(value);
            }
            return sb.toString();
        } else if (currentLen == length) {
            return value;
        } else {
            return substring(value, 0, length);
        }
    }

    /**
     * 將字串已指定長度切成List
     */
    public static List<String> fixLength(String value, int length) {
        char[] array = value.toCharArray();
        List<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, count = 0, lastTime = 0; i < array.length; i++) {
            if (String.valueOf(array[i]).matches("[^\\x00-\\xff]")) {
                count += 2;
                lastTime = 2;
            } else {
                count++;
                lastTime = 1;
            }
            if (count > length) {
                list.add(sb.toString());
                sb = new StringBuilder();
                sb.append(array[i]);
                count = lastTime;
            } else if (count == length) {
                sb.append(array[i]);
                list.add(sb.toString());
                sb = new StringBuilder();
                count = 0;
            } else {
                sb.append(array[i]);
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
        }
        return list;
    }
}