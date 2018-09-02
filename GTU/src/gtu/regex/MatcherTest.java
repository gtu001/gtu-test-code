package gtu.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class MatcherTest {

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("(aaaa)(_)(vvvvv)(-)(eeeee)");
        Matcher matcher = pattern.matcher("aaaa_vvvvv-eeeee");

        // TODO
        System.out.println(matcher.hasAnchoringBounds());
        System.out.println(matcher.hasTransparentBounds());
        System.out.println(matcher.hitEnd());
        System.out.println(matcher.lookingAt());
        System.out.println(matcher.regionStart());
        System.out.println(matcher.requireEnd());
    }

    @Test
    public void testXXXXXXX() {
        Pattern pattern = Pattern.compile("and");
        Matcher matcher = pattern.matcher("aaaaaa and vvvvvv and eeeeee and vvvvvv");

        while (matcher.find()) {
            System.out.println("--------------------------");
            System.out.println("hasAnchoringBounds = " + matcher.hasAnchoringBounds());
            System.out.println("hasTransparentBounds = " + matcher.hasTransparentBounds());
            System.out.println("hitEnd = " + matcher.hitEnd());
            System.out.println("lookingAt = " + matcher.lookingAt());
            System.out.println("regionStart = " + matcher.regionStart());
            System.out.println("requireEnd = " + matcher.requireEnd());
        }
    }

    // @Test
    public void testQuoteReplacement() {
        // 用途不明，只知道會替換"\","$"兩個字元 ，替換規則 : "\"->"\\" , "$"->"\$"
        // http://renren.it/a/caozuoxitong/OS/20101213/70838.html
        System.out.println(Matcher.quoteReplacement("\\$aaaabbbb\\ccc$dddd$"));
    }

    // @Test
    public void test_appendReplacement_appendTail_replaceAll_replaceFirst() {
        System.out.println("#appendReplacement_appendTail");
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("Bond");
        Matcher matcher = pattern.matcher("My name is Bond. James Bond. I would like a martini.");
        while (matcher.find()) {
            // append並取代每次匹配的結果
            // 第1次append "My name is Smith"
            // 第2次append ". James Smith"
            matcher.appendReplacement(sb, "Smith");
            System.out.println(sb);
        }
        matcher.appendTail(sb); // 加回來
        System.out.println(sb);

        System.out.println("\n#replaceAll");
        // 比較簡單的做法是用 replaceAll
        System.out.println(matcher.replaceAll("John"));

        System.out.println("\n#replaceFirst");
        System.out.println(matcher.replaceFirst("Mary"));
    }

    // @Test
    public void testMatchs() {
        // matchs與find不同 ，必須完全匹配
        Matcher matcher1 = Pattern.compile("(aaaa)").matcher("EEEaaaa_vvvvv-eeeeeaaaa_vvvvv-eeeeeWWW");
        System.out.println("matcher1 = " + matcher1.matches());
        Matcher matcher2 = Pattern.compile("(aaaa)").matcher("aaaaaaa");
        System.out.println("matcher2 = " + matcher2.matches());
        Matcher matcher3 = Pattern.compile("(aaaa)").matcher("aaaa");
        System.out.println("matcher3 = " + matcher3.matches());
    }

    // @Test
    public void testFind() {
        // group代表每個"()"匹配的條件，下面有5個括號 group就是5
        // 必須先find 才能做其他事否則 groupCount還有group都無用
        // find(num) -> num表示find要從第幾個char開始找

        Pattern pattern = Pattern.compile("(aaaa)(_)(vvvvv)(-)(eeeee)");
        Matcher matcher = pattern.matcher("EEEaaaa_vvvvv-eeeeeaaaa_vvvvv-eeeeeWWW");

        boolean findResult = false;
        while (findResult = matcher.find()) {
            System.out.println("find = " + findResult);
            System.out.println("start = " + matcher.start() + "\tend = " + matcher.end());
            System.out.println("groupCount = " + matcher.groupCount());
            for (int ii = 0, count = matcher.groupCount(); ii < count; ii++) {
                String str = matcher.group(ii);
                System.out.println("ii = " + ii + "\tgroup = " + str);
            }
        }
    }
}
