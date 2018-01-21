package gtu.regex;

import java.util.*;

import org.apache.oro.text.regex.*;

/**
 * 類簡介: 使用正則表達式驗證數據或提取數據,類中的方法全為靜態的 主要方法:1. isHardRegexpValidate(String source,
 * String regexp)
 * 
 * 
 * 區分大小寫敏感的正規表達式批配
 * 
 * 2. isSoftRegexpValidate(String source, String regexp) 不區分大小寫的正規表達式批配 3.
 * getHardRegexpMatchResult(String source, String regexp)
 * 返回許要的批配結果集(大小寫敏感的正規表達式批配) 4. getSoftRegexpMatchResult(String source, String
 * regexp) 返回許要的批配結果集(不區分大小寫的正規表達式批配) 5 getHardRegexpArray(String source, String
 * regexp) 返回許要的批配結果集(大小寫敏感的正規表達式批配) 6. getSoftRegexpMatchResult(String source,
 * String regexp) 返回許要的批配結果集(不區分大小寫的正規表達式批配) 7. getBetweenSeparatorStr(final
 * String originStr,final char leftSeparator,final char rightSeparator)
 * 得到指定分隔符中間的字符串的集合
 * 
 * @mail wuzhi2000@hotmail.com
 * @author ygj
 * 
 */
public final class Regexp {

    /** 保放有四組對應分隔符 */
    static final Set SEPARATOR_SET = new TreeSet();
    {
        SEPARATOR_SET.add("(");
        SEPARATOR_SET.add(")");
        SEPARATOR_SET.add("[");
        SEPARATOR_SET.add("]");
        SEPARATOR_SET.add("{");
        SEPARATOR_SET.add("}");
        SEPARATOR_SET.add("<");
        SEPARATOR_SET.add(">");
    }

    /** 存放各種正規表達式(以key->value的形式) */
    public static HashMap regexpHash = new HashMap();

    /** 存放各種正規表達式(以key->value的形式) */
    public static List matchingResultList = new ArrayList();

    private Regexp() {

    }

    /**
     * 返回 Regexp 實例
     * 
     * @return
     */
    public static Regexp getInstance() {
        return new Regexp();
    }

    /**
     * 匹配圖像
     * 
     * 
     * 格式: /相對路徑/文件名.後綴 (後綴為gif,dmp,png)
     * 
     * 匹配 : /forum/head_icon /admini2005111_ff.gif 或 admini2005111.dmp
     * 
     * 
     * 不匹配: c:/admins4512.gif
     * 
     */
    public static final String icon_regexp = "^(/{0,1}\\w){1,}\\.(gif|dmp|png|jpg)$|^\\w{1,}\\.(gif|dmp|png|jpg )$";

    /**
     * 匹配email地址
     * 
     * 
     * 格式: XXX@XXX.XXX.XX
     * 
     * 匹配 : foo@bar.com 或 foobar@foobar.com.au
     * 
     * 
     * 不匹配: foo@bar 或 $$$@bar.com
     * 
     */
    public static final String email_regexp = "(?:\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3}$)";

    /**
     * 匹配匹配並提取url
     * 
     * 
     * 格式: XXXX://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX
     * 
     * 匹配 : http://www.suncer.com 或news://www
     * 
     * 
     * 提取 (MatchResult matchResult=matcher.getMatch()): matchResult.group(0)=
     * http://www.suncer.com:8080/index.html?login=true matchResult.group(1) =
     * http matchResult.group(2) = www.suncer.com matchResult.group(3) = :8080
     * matchResult.group(4) = /index.html?login=true
     * 
     * 不匹配: c:\window
     * 
     */
    public static final String url_regexp = "(\\w+)://([^/:]+)(:\\d*)?([^#\\s]*)";

    /**
     * 匹配並提取http
     * 
     * 
     * 格式: http://XXX.XXX.XXX.XX /XXX.XXX?XXX=XXX 或 ftp://XXX.XXX.XXX 或
     * https://XXX
     * 
     * 匹配 : http://www.suncer.com:8080 /index.html?login=true
     * 
     * 
     * 提取 (MatchResult matchResult=matcher.getMatch()): matchResult.group(0)=
     * http://www.suncer.com:8080/index.html?login=true matchResult.group(1) =
     * http matchResult.group(2) = www.suncer.com matchResult.group(3) = :8080
     * matchResult.group(4) = /index.html?login=true
     * 
     * 不匹配: news://www
     * 
     */
    public static final String http_regexp = "(http|https|ftp)://([^/:]+)(:\\d*)?([^#\\s]*)";

    /**
     * 匹配日期
     * 
     * 
     * 格式(首位不為0): XXXX-XX-XX 或 XXXX XX XX 或 XXXX- XX
     * 
     * 
     * 範圍:1900--2099
     * 
     * 
     * 匹配 : 2005-04-04
     * 
     * 
     * 不匹配: 01-01-01
     * 
     */
    public static final String date_regexp = "^((((19){1}|(20){1})d{2})|d{2})[-\\s]{1}[01]{1}d{1}[ -\\s]{1}[0-3]{1}d{1}$";// 匹配日期

    /**
     * 匹配電話
     * 
     * 
     * 格式為: 0XXX-XXXXXX(10-13位首位必須為0) 或 0XXX XXXXXXX(10-13位首位必須為0) 或
     * 
     * (0XXX)XXXXXXXX(11-14位首位必須為 0) 或 XXXXXXXX(6-8位首位不為0) 或
     * XXXXXXXXXXX(11位首位不為0)
     * 
     * 
     * 匹配 : 0371-123456 或 (0371)1234567 或 (0371)12345678 或 010-123456 或
     * 010-12345678 或 12345678912
     * 
     * 
     * 不匹配: 1111-134355 或 0123456789
     * 
     */
    public static final String phone_regexp = "^(?:0[0-9]{2,3}[-\\s]{1}|\\(0[0-9]{2,4}\\))[0-9]{ 6,8}$|^[1-9]{1}[0-9]{5,7}$|^[1-9]{1}[0-9]{10}$";

    /**
     * 匹配身份證
     * 
     * 
     * 格式為: XXXXXXXXXX(10位) 或 XXXXXXXXXXXXX(13 位) 或 XXXXXXXXXXXXXXX(15位) 或
     * XXXXXXXXXXXXXXXXXX(18位)
     * 
     * 
     * 匹配 : 0123456789123
     * 
     * 
     * 不匹配: 0123456
     * 
     */
    public static final String ID_card_regexp = "^\\d{10}|\\d{13}|\\d{15}|\\d{18}$";

    /**
     * 匹配郵編代碼
     * 
     * 
     * 格式為: XXXXXX(6位)
     * 
     * 
     * 匹配 : 012345
     * 
     * 
     * 不匹配: 0123456
     * 
     */
    public static final String ZIP_regexp = "^[0-9]{6}$";// 匹配郵編代碼

    /**
     * 不包括特殊字符的匹配 (字符串中不包括符號 數學次方號^ 單引號' 雙引號" 分號; 逗號, 帽號: 數學減號- 右尖括號> 左尖括號< 反斜杠\
     * 即空格,製表符,回車符等 )
     * 
     * 
     * 格式為: x 或 一個一上的字符
     * 
     * 
     * 匹配 : 012345
     * 
     * 
     * 不匹配: 0123456
     * 
     */
    public static final String non_special_char_regexp = "^[^'\"\\;,:-<>\\s].+$";// 匹配郵編代碼

    /**
     * 匹配非負整數（正整數 + 0)
     */
    public static final String non_negative_integers_regexp = "^\\d+$";

    /**
     * 匹配不包括零的非負整數（正整數 > 0)
     */
    public static final String non_zero_negative_integers_regexp = "^[1-9]+\\d*$";

    /**
     * 
     * 匹配正整數
     * 
     */
    public static final String positive_integer_regexp = "^[0-9]*[1-9][0-9]*$";

    /**
     * 
     * 匹配非正整數（負整數 + 0）
     * 
     */
    public static final String non_positive_integers_regexp = "^((-\\d+)|(0+))$";

    /**
     * 
     * 匹配負整數
     * 
     */
    public static final String negative_integers_regexp = "^-[0-9]*[1-9][0-9]*$";

    /**
     * 
     * 匹配整數
     * 
     */
    public static final String integer_regexp = "^-?\\d+$";

    /**
     * 
     * 匹配非負浮點數（正浮點數 + 0）
     * 
     */
    public static final String non_negative_rational_numbers_regexp = "^\\d+(\\.\\d+)?$";

    /**
     * 
     * 匹配正浮點數
     * 
     */
    public static final String positive_rational_numbers_regexp = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";

    /**
     * 
     * 匹配非正浮點數（負浮點數 + 0）
     * 
     */
    public static final String non_positive_rational_numbers_regexp = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";

    /**
     * 
     * 匹配負浮點數
     * 
     */
    public static final String negative_rational_numbers_regexp = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$";

    /**
     * 
     * 匹配浮點數
     * 
     */
    public static final String rational_numbers_regexp = "^(-?\\d+)(\\.\\d+)?$";

    /**
     * 
     * 匹配由26個英文字母組成的字符串
     * 
     */
    public static final String letter_regexp = "^[A-Za-z]+$";

    /**
     * 
     * 匹配由26個英文字母的大寫組成的字符串
     * 
     */
    public static final String upward_letter_regexp = "^[A-Z]+$";

    /**
     * 
     * 匹配由26個英文字母的小寫組成的字符串
     * 
     */
    public static final String lower_letter_regexp = "^[a-z]+$";

    /**
     * 
     * 匹配由數字和26個英文字母組成的字符串
     * 
     */
    public static final String letter_number_regexp = "^[A-Za-z0-9]+$";

    /**
     * 
     * 匹配由數字、26個英文字母或者下劃線組成的字符串
     * 
     */
    public static final String letter_number_underline_regexp = "^\\w+$";

    /**
     * 添加正規表達式 (以key->value的形式存儲)
     * 
     * @param regexpName
     *            該正規表達式名稱 `
     * @param regexp
     *            該正規表達式內容
     */
    public void putRegexpHash(String regexpName, String regexp) {
        regexpHash.put(regexpName, regexp);
    }

    /**
     * 得到正規表達式內容 (通過key名提取出value[正規表達式內容])
     * 
     * @param regexpName
     *            正規表達式名稱
     * 
     * @return 正規表達式內容
     */
    public String getRegexpHash(String regexpName) {
        if (regexpHash.get(regexpName) != null) {
            return ((String) regexpHash.get(regexpName));
        } else {
            System.out.println("在 regexpHash中沒有此正規表達式");
            return "";
        }
    }

    /**
     * 清除正規表達式存放單元
     */
    public void clearRegexpHash() {
        regexpHash.clear();
        return;
    }

    /**
     * 大小寫敏感的正規表達式批配
     * 
     * @param source
     *            批配的源字符串
     * 
     * @param regexp
     *            批配的正規表達式
     * 
     * @return 如果源字符串符合要求返回真,否則返回假 如:
     *         Regexp.isHardRegexpValidate("ygj@suncer.com.cn",email_regexp) 返回真
     */
    public static boolean isHardRegexpValidate(String source, String regexp) {

        try {
            // 用於定義正規表達式對像模板類型
            PatternCompiler compiler = new Perl5Compiler();

            // 正規表達式比較批配對象
            PatternMatcher matcher = new Perl5Matcher();

            // 實例大小大小寫敏感的正規表達式模板
            Pattern hardPattern = compiler.compile(regexp);

            // 返回批配結果
            return matcher.contains(source, hardPattern);

        } catch (MalformedPatternException e) {
            e.printStackTrace();

        }
        return false;
    }
}
