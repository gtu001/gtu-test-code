package gtu.apache;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;

public class StringEscapeUtilsTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    @Test
    public void testEscapeInformation() {
        final String str = "test asdfbalsdkjfl;lkj";

        System.out.println("escapeCsv..");
        // System.out.println(StringEscapeUtils.escapeCsv(str));
        System.out.println();
        System.out.println("escapeHtml..");
        System.out.println(StringEscapeUtils.escapeHtml(str));
        System.out.println();
        System.out.println("escapeJava..");
        System.out.println(StringEscapeUtils.escapeJava(str));
        System.out.println();
        System.out.println("escapeJavaScript..");
        System.out.println(StringEscapeUtils.escapeJavaScript(str));
        System.out.println();
        System.out.println("escapeSql..");
        System.out.println(StringEscapeUtils.escapeSql(str));
        System.out.println();
        System.out.println("escapeXml..");
        System.out.println(StringEscapeUtils.escapeXml(str));
        System.out.println();
        System.out.println("unescapeCsv..");
        // System.out.println(StringEscapeUtils.unescapeCsv(str));
        System.out.println();
        System.out.println("unescapeHtml..");
        System.out.println(StringEscapeUtils.unescapeHtml(str));
        System.out.println();
        System.out.println("unescapeJava..");
        System.out.println(StringEscapeUtils.unescapeJava(str));
        System.out.println();
        System.out.println("unescapeJavaScript..");
        System.out.println(StringEscapeUtils.unescapeJavaScript(str));
        System.out.println();
        System.out.println("unescapeXml..");
        System.out.println(StringEscapeUtils.unescapeXml(str));
        System.out.println();
    }

}
