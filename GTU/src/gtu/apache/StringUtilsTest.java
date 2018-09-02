package gtu.apache;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class StringUtilsTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    @Test
    public void testStringUtils() {
        System.out.println("縮寫一段文字  = " + StringUtils.abbreviate("abcdefghijklmno", -1, 10));

        System.out.println("銜接在一起 = " + StringUtils.join(new String[] { "111", "222", "333" }));

        System.out.println("替換目標字串一次 = " + StringUtils.replaceOnce("aba", "a", "_"));

        System.out.println("覆蓋原來的字串 = " + StringUtils.overlay("abcdef", "zzzz", -1, 4));

        System.out.println("切掉後面的字串1 = " + StringUtils.chomp("foobar", "bar"));
        System.out.println("切掉後面的字串2 = " + StringUtils.chomp("foobar", "baz"));

        // System.out.println("補齊方法 = " + StringUtils.padding(3, 'e'));

        System.out.println("居中 = [" + StringUtils.center("a", 5) + "]");
        System.out.println("左邊補字 = " + StringUtils.leftPad("abc", 10, '_'));
        System.out.println("右邊補字 = " + StringUtils.rightPad("abc", 10, '_'));

        System.out.println("首字母大寫 = " + StringUtils.capitalize("cat"));
        System.out.println("首字母小寫 = " + StringUtils.uncapitalize("CAT"));
        System.out.println("大小寫顛倒 = " + StringUtils.swapCase("The dog has a BONE"));

        System.out.println("檢查字串是否只有unicode字母(abc) = " + StringUtils.isAlpha("abc"));
        System.out.println("檢查字串是否只有unicode字母(ab2c) = " + StringUtils.isAlpha("ab2c"));
        System.out.println("檢查字串是否只有unicode字母(ab-c) = " + StringUtils.isAlpha("ab-c"));

        System.out.println("檢查字串是否只有unicode字母和‘ ’ 空格(ab2c) = " + StringUtils.isAlphaSpace("ab2c"));
        System.out.println("檢查字串是否只有unicode字母和‘ ’ 空格(ab c) = " + StringUtils.isAlphaSpace("ab c"));
        System.out.println("檢查字串是否只有unicode字母和‘ ’ 空格(ab-c) = " + StringUtils.isAlphaSpace("ab-c"));
        System.out.println("檢查字串是否只有unicode字母和數位(ab2c d) = " + StringUtils.isAlphanumeric("ab2c d"));
        System.out.println("檢查字串是否只有unicode字母和數位和空格(ab2c d) = " + StringUtils.isAlphanumericSpace("ab2c d"));

        System.out.println("檢查字串是否只有可列印的ASCII編碼的的字元(!ab-c~) = " + StringUtils.isAsciiPrintable("!ab-c~"));
        System.out.println("檢查字串是否只有可列印的ASCII編碼的的字元(\u0020) = " + StringUtils.isAsciiPrintable("\u0020"));
        System.out.println("檢查字串是否只有可列印的ASCII編碼的的字元(Ceki G\u00fclc\u00fc) = "
                + StringUtils.isAsciiPrintable("Ceki G\u00fclc\u00fc"));

        System.out.println("檢查字串是否只有數位(asdf1234) = " + StringUtils.isNumeric("asdf1234"));

        System.out.println("檢查字串是否都是空格(abc d) = " + StringUtils.isWhitespace("abc d"));

        System.out.println("顛倒字串 = " + StringUtils.reverse("bat"));

        System.out.println("刪除開頭與結尾的空白 = [" + StringUtils.strip("  aa  bb  ") + "]");
        System.out.println("刪除開頭與結尾的空白 = [" + StringUtils.trim("  aa  bb  ") + "]");
        System.out.println("刪除字串中所有空白 = [" + StringUtils.deleteWhitespace("  aa bb cc dd ee  ") + "]");

        System.out.println("計算符合次數 = " + StringUtils.countMatches("abcabcdd", "ab"));

        System.out.println("比較字串傳回不同 = " + StringUtils.difference("1234", "123"));
        System.out.println("比較字串傳回不同 = " + StringUtils.difference("1234", "234"));
        System.out.println("比較字串傳回不同 = " + StringUtils.difference("1234", "23"));

        System.out.println("尋找符合字串 = " + StringUtils.indexOf("abcdefg", "c"));
        System.out.println("尋找符合字串(回傳最左) = " + StringUtils.indexOfAny("abcdefg", new String[] { "f", "g", "e", "z" }));

        System.out.println("得到符合字串之前的字串(不含查詢字串) = " + StringUtils.substringBefore("1234567", "5"));
        System.out.println("得到符合字串之後的字串(不含查詢字串) = " + StringUtils.substringAfter("1234567", "3"));
        System.out.println("得到最後一次符合字串之前的字串(不含查詢字串) = " + StringUtils.substringBeforeLast("1234512345", "3"));
        System.out.println("得到最後一次符合字串之後的字串(不含查詢字串) = " + StringUtils.substringAfterLast("1234512345", "3"));
        System.out.println("取得符合字串中間部分(不含查詢字串) = " + StringUtils.substringBetween("12345abcde12345", "3"));
        System.out.println("取得符合字串中間部分(不含查詢字串) = " + StringUtils.substringBetween("12345abcde12345", "3", "c"));
    }
}
