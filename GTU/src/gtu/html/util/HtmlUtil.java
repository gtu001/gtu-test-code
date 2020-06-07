package gtu.html.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class HtmlUtil {

    public static String transferSpaceAndTab(String text) {
        // text = StringUtils.defaultString(text).replaceAll(" ", "&nbsp;");
        // text = text.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        // text = text.replaceAll("<", "&lt;");
        // text = text.replaceAll(">", "&gt;");
        // text = text.replaceAll("\n", "<br/>");
        text = StringEscapeUtils.escapeHtml(text);
        return text;
    }
}
