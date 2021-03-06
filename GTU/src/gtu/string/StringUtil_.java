package gtu.string;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

public class StringUtil_ {

    public static void main(String[] args) {
        String content = StringUtil_.appendReplacementEscape("aaa\\ \\bbb\nddd\t bbb");
        System.out.println(content);
        System.out.println("done...");
    }

    /**
     * 抓全形&中文字
     * 
     * @param str
     * @param isChineseBool
     * @return
     */
    public static String getChineseWord2(String str, boolean isChineseBool) {
        StringBuffer sb = new StringBuffer();
        char[] cs = str.toCharArray();
        for (char c : cs) {
            String x = new String(new char[] { c });
            if (isChineseBool && x.getBytes().length > 1) {
                sb.append(c);
            } else if (!isChineseBool && x.getBytes().length == 1) {
                sb.append(c);
            }
        }
        return sb.toString();
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

    /**
     * 抓中文字(注音不抓)
     * 
     * @param str
     * @param isChineseBool
     * @return
     */
    @Deprecated
    public static boolean hasChineseWord(String str) {
        Matcher mth = Pattern.compile("[\u4e00-\u9fa5]").matcher(str);
        if (mth.find()) {
            return true;
        }
        return false;
    }

    public static boolean hasChineseWord2(String str) {
        char[] cs = StringUtils.defaultString(str).toCharArray();
        for (char c : cs) {
            if (isFullChar(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ref: http://www.micmiu.com/lang/java/java-check-chinese/
     * 
     * @param ch
     * @return
     */
    public static boolean isFullChar(char ch) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * Printable characters
     * 
     * @param ch
     * @return
     */
    public static boolean isPrintableAsciiChar(char ch) {
        if (32 <= ch && ch <= 126)
            return true;
        return false;
    }

    public void testScanner() {
        String str = null;
        Scanner scn = new Scanner(str);
        while (scn.hasNextLine()) {
            System.out.println(scn.nextLine());
        }
        scn.close();
    }

    /**
     * 清除所有半形空白
     * 
     * @param values
     * @return
     */
    public static String trimAllSpace(String values) {
        char[] val = values.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int ii = 0; ii < val.length; ii++) {
            if (val[ii] != ' ') {
                sb.append(val[ii]);
            }
        }
        return sb.toString();
    }

    /**
     * 比較字串相似度
     * 
     * @param mime
     * @param theies
     * @return
     */
    public float compareString(String mime, String theies) {
        int flag;
        int sp = 0, icount = 0;
        int cp = 0;
        for (int i = 0; i < mime.length(); i++) {
            flag = 1;
            for (int j = 0; j < theies.length(); j++) {
                icount = 0;
                if (mime.charAt(i) == theies.charAt(j)) {
                    if (flag == 1) {
                        sp++;
                        flag = 0;
                    }
                    while (((i + icount) < mime.length()) && ((j + icount) < theies.length()) && (mime.charAt(i + icount) == theies.charAt(j + icount))) {
                        icount++;
                    }
                    if (icount > cp)
                        cp = icount;
                    icount = 0;
                }
            }
        }
        System.out.println(sp + "ff" + cp);
        return Math.min(sp, cp) / (float) mime.length();
    }

    public void testMD5() {
        try {
            System.out.println("1 -> " + StringUtil_.toHexString("A".getBytes()));
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update("A".getBytes());
            byte[] digest = md.digest();

            System.out.println("2 -> " + StringUtil_.toHexString(digest));

            System.out.println("3 -> " + StringUtil_.digest("A", "md5"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 將字串轉成UTF8編碼
     * 
     * @param str
     * @return
     */
    public static String toUTF8(String str) {
        try {
            return new String(str.getBytes(), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 中文轉換編碼
     * 
     * @param unicodeString
     *            字串
     * @param code
     *            欲轉換的編碼
     * @return
     */
    public static String exchangeCode(String unicodeString, String code) {
        String rtnString = "";
        try {
            if (unicodeString != null) {
                byte[] b = unicodeString.getBytes(code);
                rtnString = new String(b);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return rtnString;
    }

    /**
     * 傳回一個雜湊長度36的字串ID
     * 
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 將字串內容複製給另一個字串變數,不會傳參考值
     * 
     * @param copy
     *            欲複製的字串
     * @return
     */
    public static String copyString(String copy) {
        return copy.toString();
    }

    /**
     * 檢核是否為數值 <br>
     * Ex:134.23 or 452 return true <br>
     * Ex:323bd32 or 2334.3f return false
     * 
     * @param data
     * @return
     */
    public static boolean chkNumber(String data) {
        if (data == null || data.equals(""))
            return false;
        return Pattern.matches("\\-?(\\d+\\.\\d+|\\d+)", data);
    }

    /**
     * 將字串填補所需字元長度 Ex:fillChar("777","R",'x',5) rtn : "xx777"
     * 
     * @param str
     *            字串
     * @param putLR
     *            L靠左 / R靠右
     * @param c
     *            填補字元
     * @param len
     *            填補長度
     * @return
     */
    public static String fillChar(String str, char putLR, char c, int len) {
        try {
            if (str == null)
                str = "";
            else {
                str = str.trim();
                len = len - str.length();
                if (putLR == 'r' || putLR == 'R') {
                    str = fillChar(c, len) + str;
                } else {
                    str = str + fillChar(c, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 將字串填補所需字元長度(全形計算長度２) Ex:fillChar("呵呵","R",'x',10) rtn : "xxxxxx呵呵"
     * 
     * @param str
     *            字串
     * @param putLR
     *            Ｌl靠左 / Ｒr 靠右
     * @param c
     *            填補字元
     * @param len
     *            填補長度
     * @return
     */
    public static String fillCharForByte(String str, String putLR, char c, int len) {
        try {
            if (str == null)
                str = "";
            else {
                str = str.trim();
                int strlen = str.getBytes().length;
                System.out.println(strlen > len);
                if (strlen > len) {
                    byte[] tmp = new byte[len];
                    System.arraycopy(str.getBytes(), 0, tmp, 0, len);
                    str = new String(tmp);
                } else if (strlen < len) {
                    len = len - str.length();
                    if (putLR.equalsIgnoreCase("R")) {
                        str = fillChar(c, len) + str;
                    } else {
                        str = str + fillChar(c, len);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private static String fillChar(char c, int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 檢查字串若為null傳回空字串
     * 
     * @param str
     * @return
     */
    public static String chkNull(String str) {
        if (str == null)
            return "";
        return str;
    }

    /**
     * @param num
     *            數值字串 ex:"4347620043"
     * @param complicated
     *            true : 則中文為金融複雜用字 Ex:壹貳參肆伍陸柒捌玖<br>
     *            false : 一二三四五六七八九
     * @return 肆參肆柒陸貳零零肆參 / 四三四七六二００四三
     */
    public static String numToChnEasy(String num, boolean complicated) {
        String[][] number = { { "零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖" }, { "０", "一", "二", "三", "四", "五", "六", "七", "八", "九" } };
        int comp = complicated == true ? 0 : 1;
        char[] tmpnum = num.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int ii = 0; ii < tmpnum.length; ii++) {
            sb.append(number[comp][Integer.parseInt(String.valueOf(tmpnum[ii]))]);
        }
        return sb.toString();
    }

    /**
     * @param num
     *            數值字串 ex:"4347620043"
     * @param complicated
     *            true : 則中文為金融複雜用字 Ex:壹貳參肆伍陸柒捌玖<br>
     *            false : 一二三四五六七八九
     * @return 肆拾參億肆仟柒佰陸拾貳萬肆拾參 / 四十三億四千七百六十二萬四十三
     */
    public static String numToChn(String num, boolean complicated) {
        String[][] betweenNum = { { "", "" }, { "拾", "十" }, { "佰", "百" }, { "仟", "千" } };
        String[] betweenDef = { "", "萬", "億", "兆", "兆兆" };
        String[][] number = { { "", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖" }, { "", "一", "二", "三", "四", "五", "六", "七", "八", "九" } };

        ArrayList<String> numary = new ArrayList<String>();
        int comp = complicated == true ? 0 : 1;

        while (num.length() > 4) {
            char[] c = num.substring(num.length() - 4, num.length()).toCharArray();
            Integer pos = null;
            for (int ii = 0; c[ii] == '0'; c[ii] = ' ', pos = ii, ii++)
                ;
            if (pos != null)
                c[pos] = '0';
            for (int ii = c.length - 1; c[ii] == '0'; c[ii] = ' ', ii--)
                ;
            numary.add(String.valueOf(c));
            num = num.substring(0, num.length() - 4);
        }
        if (num != null)
            numary.add(num);

        StringBuffer sb = new StringBuffer();
        for (int ar = numary.size() - 1; ar >= 0; ar--) {
            char[] cha = numary.get(ar).trim().toCharArray();
            for (int ii = 0, jj = cha.length - 1; ii < cha.length; ii++, jj--) {
                String tmpnum = number[comp][Integer.parseInt(String.valueOf(cha[ii]))];
                String tmpbet = new String();
                if (cha[ii] == '0' && jj != 0) {
                    tmpbet = "零";
                } else {
                    tmpbet = betweenNum[jj][comp];
                }
                sb.append(tmpnum + tmpbet);
            }
            sb.append(betweenDef[ar]);
        }
        return sb.toString();
    }

    // 将金额整数部分转换为中文大写
    public static String integer2rmb(String integer) {
        char[] RMB_NUMS = "零壹貳參肆伍陸柒捌玖".toCharArray();
        String[] UNITS = { "元", "角", "分", "整" };
        String[] U1 = { "", "拾", "佰", "仟" };
        String[] U2 = { "", "萬", "億" };
        StringBuilder buffer = new StringBuilder();
        // 从个位数开始转换

        int dotPlace = integer.indexOf(".");
        if (dotPlace > 0) {
            integer = integer.substring(0, dotPlace);
        }

        int i, j;
        for (i = integer.length() - 1, j = 0; i >= 0; i--, j++) {
            char n = integer.charAt(i);
            if (n == '0') {
                // 当n是0且n的右边一位不是0时，插入[零]
                if (i < integer.length() - 1 && integer.charAt(i + 1) != '0') {
                    buffer.append(RMB_NUMS[0]);
                }
                // 插入[万]或者[亿]
                if (j % 4 == 0) {
                    if (i > 0 && integer.charAt(i - 1) != '0' || i > 1 && integer.charAt(i - 2) != '0' || i > 2 && integer.charAt(i - 3) != '0') {
                        buffer.append(U2[j / 4]);
                    }
                }
            } else {
                if (j % 4 == 0) {
                    buffer.append(U2[j / 4]); // 插入[万]或者[亿]
                }
                buffer.append(U1[j % 4]); // 插入[拾]、[佰]或[仟]
                buffer.append(RMB_NUMS[n - '0']); // 插入数字
            }
        }
        return buffer.reverse().toString() + "元整";
    }

    /**
     * 將字串分散 每個字間隔相當
     * 
     * @param str
     *            字串
     * @param len
     *            總長度
     * @return
     */
    public static String splitStringSpace(String str, int len) {
        if (str == null || str.equals(""))
            return "";
        int tmplen = (len - str.getBytes().length) / (str.length() + 1);
        StringBuffer sb = new StringBuffer();
        char[] cstr = str.toCharArray();
        for (int ii = 0; ii < cstr.length; ii++) {
            sb.append(fillChar(' ', tmplen) + cstr[ii]);
        }
        return sb.toString() + fillChar(' ', len - sb.toString().length());
    }

    /**
     * 將數值格式化 每三位加逗點 若有小數則顯示兩位
     * 
     * @param <T>
     *            可接受 字串 , double , long
     * @param ttt
     *            預備處理的數值
     * @return
     */
    public static <T> String formatNumber(T ttt) {
        return formatNumber("##,##0.##", ttt);
    }

    /**
     * 將數值格式化
     * 
     * @param <T>
     *            可接受 字串 , double , long
     * @param format
     *            format格式
     * @param ttt
     *            預備處理的數值
     * @return
     */
    public static <T> String formatNumber(String format, T ttt) {
        String rtn = new String();
        try {
            DecimalFormat df = new DecimalFormat();
            df.applyPattern(format);
            if (ttt instanceof String) {
                rtn = df.format(Double.parseDouble(String.valueOf(ttt)));
            } else {
                rtn = df.format(ttt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    /**
     * 字串轉16進位
     * 
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        String plainText;
        for (int ii = 0; ii < b.length; ii++) {
            plainText = Integer.toHexString(0xFF & b[ii]);
            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    /**
     * 加密
     * 
     * @param cleartext
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static String digest(String cleartext, String algorithm) throws Exception {
        final char hex[] = "0123456789abcdef".toCharArray();
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(cleartext.getBytes());
        byte digest[] = md.digest();
        StringBuffer sb = new StringBuffer(2 * digest.length);
        for (int i = 0; i < digest.length; i++) {
            int high = (digest[i] & 0xf0) >> 4;
            int low = digest[i] & 0xf;
            sb.append(hex[high]);
            sb.append(hex[low]);
        }
        return sb.toString();
    }

    /**
     * 將package path格式成檔案路徑 Ex : "aaa.bbb.ccc.ddd.eee" -> aaa\bbb\ccc\ddd\eee\
     * 
     * @param packageStr
     * @return
     */
    public static String parsePackageToPath(String packageStr) {
        if (StringUtils.isBlank(packageStr)) {
            return StringUtils.EMPTY;
        }
        String[] packages = packageStr.split("\\.", -1);
        StringBuilder sb = new StringBuilder();
        for (String tempP : packages) {
            sb.append(tempP + File.separator);
        }
        return sb.toString();
    }

    /**
     * 依照pattern切割字串 並取得分割得符合的MatchResult
     */
    public static abstract class ScannerWithDelimiter {
        public abstract void appendReplacement(String inputStr, MatchResult matcher);

        public abstract void appendTail(String inputStr);

        public void execute(Pattern pattern, String inputStr) {
            Matcher matcher = pattern.matcher(inputStr);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, "");
                appendReplacement(sb.toString(), matcher);
                sb = new StringBuffer();
            }
            matcher.appendTail(sb);
            appendTail(sb.toString());
        }
    }

    /**
     * 去掉全形空白
     */
    public static String trimFullSpace(String str) {
        StringBuilder sb = new StringBuilder();
        char[] arry = str.toCharArray();
        for (char c : arry) {
            if (c == (char) (161 + 64) || c == (char) (12288)) {
                // do nothing
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * DB限制長度時使用
     */
    public static String getDBLimitStr(String strValue, int maxLength) {
        byte[] bs = strValue.getBytes();
        int maxLen = Math.min(maxLength, bs.length);
        strValue = new String(bs, 0, maxLen);
        return strValue;
    }

    public static void halfCharToFullChar() {
        String outStr = "";
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        int tranTemp = 0;
        for (int i = 0; i < chars.length; i++) {
            tranTemp = (int) chars[i];
            if (tranTemp != 45) // ASCII碼:45 是減號 -
                tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差
            outStr += (char) tranTemp;
        }
        System.out.println("outStr : " + outStr);
    }

    public static boolean isUUID(String str) {
        final Pattern UUID_PATTERN = Pattern.compile("^\\w{8}\\-\\w{4}\\-\\w{4}\\-\\w{4}\\-\\w{12}$");
        return UUID_PATTERN.matcher(str).find();
    }

    /**
     * 字串轉成原始型別物件
     */
    public static <T> Object stringToPrimitive(String value, Class<T> targetClz) {
        if (value == null) {
            return null;
        }
        return ConvertUtils.convert(value, targetClz);
    }

    public static String replaceToSpaceKeepStructure(String strVal) {
        final char[] ESCAPE_ARRY = new char[] { '\t', '\b', '\n', '\r', '\f' };
        strVal = StringUtils.defaultString(strVal);
        char[] arry = strVal.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : arry) {
            if (ArrayUtils.contains(ESCAPE_ARRY, c)) {
                sb.append(c);
            } else {
                sb.append(' ');
            }
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
        private static final char[] ESCAPE_ARRY = new char[] { 't', 'b', 'n', 'r', 'f', '\'', '\"', '\\' };

        AppendReplacementEscaper(String content) {
            this.content = content;
            result = StringUtils.defaultString(content).toString();
            if (StringUtils.isBlank(result)) {
                return;
            }
            // ---------↓↓↓↓↓↓ 測試中
            result = replaceCharDelimit(result);
            // ---------↑↑↑↑↑↑ 測試中
            if (result.indexOf('$') != -1) {
                result = replaceChar(result, '$');
            }
            if (result.indexOf('/') != -1) {
                result = replaceChar(result, '/');
            }
            if ("\\".equals(result)) {
                result = "\\\\";
            }
        }

        private String replaceCharDelimit(String content) {
            StringBuffer sb = new StringBuffer();
            char[] arry = content.toCharArray();
            for (int ii = 0; ii < arry.length; ii++) {
                char a = arry[ii];
                if (a == '\\') {
                    boolean ignore = false;
                    Character before = null;
                    Character after = null;
                    if (ii - 1 >= 0) {
                        before = arry[ii - 1];
                    }
                    if (ii + 1 <= arry.length - 1) {
                        after = arry[ii + 1];
                    }
                    if (before != null && ArrayUtils.contains(ESCAPE_ARRY, before)) {
                        ignore = true;
                    } else if (after != null && ArrayUtils.contains(ESCAPE_ARRY, after)) {
                        ignore = true;
                    }
                    if (!ignore) {
                        sb.append('\\');
                    }
                }
                sb.append(a);
            }
            return sb.toString();
        }

        private String replaceChar(String content, char from) {
            if (content.indexOf(from) == -1) {
                return content;
            }
            StringBuffer sb = new StringBuffer();
            char[] arry = content.toCharArray();
            for (int ii = 0; ii < arry.length; ii++) {
                char a = arry[ii];
                if (a == from) {
                    if (from != '\\') {
                        if ((ii - 1) >= 0 && arry[ii - 1] != '\\') {
                            sb.append("\\" + a);
                        } else if (ii == 0) {
                            sb.append("\\" + a);
                        }
                    } else if (from == '\\') {
                        if (ii == arry.length - 1) {
                            sb.append("\\\\");
                        } else if ((ii + 1) < arry.length) {
                            char b = arry[ii + 1];
                            if (!ArrayUtils.contains(ESCAPE_ARRY, b)) {
                                sb.append("\\\\");
                            }
                        }
                    }
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

    public static List<String> readContentToList(String content, boolean trim, boolean emptyIgnore, boolean distinct) {
        List<String> lst = new ArrayList<String>();
        BufferedReader bufreader = null;
        try {
            bufreader = new BufferedReader(new StringReader(content));
            for (String line = null; (line = bufreader.readLine()) != null;) {
                if (trim) {
                    line = StringUtils.trimToEmpty(line);
                }
                if (emptyIgnore && StringUtils.isBlank(line)) {
                    continue;
                }
                if (distinct) {
                    if (!lst.contains(line)) {
                        lst.add(line);
                        continue;
                    }
                } else {
                    lst.add(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufreader.close();
            } catch (IOException e) {
            }
        }
        return lst;
    }

    public static String readContentAgain(String content, boolean trim, boolean emptyIgnore, boolean distinct) {
        List<String> lst = readContentToList(content, trim, emptyIgnore, distinct);
        return StringUtils.join(lst, "\r\n");
    }

    public static String oracleDecodeAsString(Object... strs) {
        if (strs == null) {
            throw new RuntimeException("decode args 不可為空!");
        }
        Object val = strs[0];
        int index = 1;
        while (true) {
            if (index > strs.length - 1) {
                if (((strs.length - 1) & 1) == 0) {
                    return null;
                }
                return strs[strs.length - 1] != null ? String.valueOf(strs[strs.length - 1]) : "";
            } else if (index == strs.length - 1) {
                return strs[index] != null ? String.valueOf(strs[index]) : "";
            }
            String strVal = strs[index] != null ? String.valueOf(strs[index]) : "";
            String strVal2 = val != null ? String.valueOf(val) : "";
            if (StringUtils.equals(strVal, strVal2)) {
                return strs[index + 1] != null ? String.valueOf(strs[index + 1]) : "";
            }
            index += 2;
        }
    }

    public static Object oracleDecode(Object... strs) {
        if (strs == null) {
            throw new RuntimeException("decode args 不可為空!");
        }
        Object val = strs[0];
        int index = 1;
        while (true) {
            if (index > strs.length - 1) {
                if (((strs.length - 1) & 1) == 0) {
                    return null;
                }
                return strs[strs.length - 1];
            } else if (index == strs.length - 1) {
                return strs[index];
            }
            if (ObjectUtils.equals(val, strs[index])) {
                return strs[index + 1];
            }
            index += 2;
        }
    }
}
