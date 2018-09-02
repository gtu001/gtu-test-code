package gtu.binary;

import java.util.ArrayList;
import java.util.List;

/**
 * 全形字元長度算2的切字串工具正確版本
 * 
 * @author gtu001
 */
public class StringUtil4FullChar {
    public static void main(String[] args) {
        String str = "公司名稱 : 聯陽半導體 統一編號 : 84149717 本次出口物品為非保稅品之正貨交易,需正式出口報關 商標 :型號IT開頭為ITE，UT開頭為USBest，CAT、CP開 頭為CAT, AT開頭為AFA 請於103年09月29日09:00至ITE成品庫提貨(新竹 科學園區創新一路9號1樓) 報關日期與提單日期需與INVOICE 日期同一天，或晚於INVOICE 日期";
        for (String s : fixLength(str, 70)) {
            System.out.println(s);
        }
        System.out.println("done...");
    }

    private static EncodeTypeLength ENCODE_TYPE = EncodeTypeLength.UTF8;

    private enum EncodeTypeLength {
        BIG5(2), //
        UTF8(3),//
        ;
        int length;

        EncodeTypeLength(int length) {
            this.length = length;
        }
    }

    public static List<String> fixLength(String value, int length) {
        char[] array = value.toCharArray();
        List<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, count = 0, lastTime = 0; i < array.length; i++) {
            if (String.valueOf(array[i]).matches("[^\\x00-\\xff]")) {
                count += ENCODE_TYPE.length;
                lastTime = ENCODE_TYPE.length;
            } else {
                count++;
                lastTime = 1;
            }
            if (count > length) {
                list.add(sb.toString());
                sb = new StringBuilder();
                // sb.append(count + "" + array[i]);
                sb.append(array[i]);
                count = lastTime;
            } else if (count == length) {
                // sb.append(count + "" + array[i]);
                sb.append(array[i]);
                list.add(sb.toString());
                sb = new StringBuilder();
                count = 0;
            } else {
                // sb.append(count + "" + array[i]);
                sb.append(array[i]);
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
        }
        return list;
    }

    public static int length(String str) {
        if (str == null) {
            return 0;
        }
        int counts = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).matches("[^\\x00-\\xff]")) {
                counts += ENCODE_TYPE.length;
            } else {
                counts++;
            }
        }
        return counts;
    }

    public static String substring(String str, int begin) {
        return substring(str, begin, Integer.MAX_VALUE);
    }

    public static String substring(String str, int begin, int end) {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, counts = 0; i < str.length(); i++) {
            int countClone = counts;
            if (str.substring(i, i + 1).matches("[^\\x00-\\xff]")) {
                counts += ENCODE_TYPE.length;
            } else {
                counts++;
            }
            if (countClone >= begin && counts <= end) {
                sb.append(str.substring(i, i + 1));
            }
        }
        return sb.toString();
    }

    public static String rightPad(String value, int length, char padChar) {
        return pad(value, length, padChar, true);
    }

    public static String leftPad(String value, int length, char padChar) {
        return pad(value, length, padChar, false);
    }

    private static String pad(String value, int length, char padChar, boolean rightPad) {
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
                sb.append(padChar);
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
}