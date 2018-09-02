package gtu.string;
import org.apache.commons.lang.StringEscapeUtils;

public class StringEscapeTest {

    public static void main(String[] args) {
        System.out.println(StringEscapeUtils.escapeXml("'"));
        System.out.println(StringEscapeUtils.unescapeXml("'"));
    }

}
