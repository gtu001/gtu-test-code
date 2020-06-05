package gtu.html.util;

import org.apache.commons.lang.StringUtils;

public class HtmlUtil {

    public static String transferSpaceAndTab(String text) {
        text = StringUtils.defaultString(text).replaceAll(" ", "&nbsp;");
        text = text.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        return text;
    }
}
