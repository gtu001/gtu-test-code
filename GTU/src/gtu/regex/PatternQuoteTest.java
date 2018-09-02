package gtu.regex;

import java.util.regex.Pattern;

public class PatternQuoteTest {

    public static void main(String[] args) {
        // 原則上有家quote 替換比較精準
        // "AAA;BBB;CCC".split(quote(";"))也適用
        String value1 = " and T.ORGANIZATION_ID   = '&ORGANIZATION_ID'   ";
        System.out.println("1=" + value1.replaceAll("&ORGANIZATION_ID", "__"));
        System.out.println("1=" + value1.replaceAll(Pattern.quote("&ORGANIZATION_ID"), "__"));

        String value2 = "[[\\.]]";
        System.out.println("2=" + value2.replaceAll("\\.", "__"));
        System.out.println("2=" + value2.replaceAll(Pattern.quote("\\."), "__"));

        String value3 = "[[\\s]]";
        System.out.println("3=" + value3.replaceAll("\\s", "__"));
        System.out.println("3=" + value3.replaceAll(Pattern.quote("\\s"), "__"));

        String value4 = "[[*]]";
        System.out.println("4=" + value4.replaceAll("\\*", "__"));
        System.out.println("4=" + value4.replaceAll(Pattern.quote("*"), "__"));

        String value5 = "[[+]]";
        System.out.println("5=" + value5.replaceAll("\\+", "__"));
        System.out.println("5=" + value5.replaceAll(Pattern.quote("+"), "__"));

        String value6 = "[[?]]";
        System.out.println("6=" + value6.replaceAll("\\?", "__"));
        System.out.println("6=" + value6.replaceAll(Pattern.quote("?"), "__"));
    }
}
