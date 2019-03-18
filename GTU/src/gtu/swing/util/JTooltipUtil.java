package gtu.swing.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class JTooltipUtil {

    public static String escapeHtml(String orignText) {
        String tmpStr = StringUtils.defaultString(orignText);
        tmpStr = StringEscapeUtils.escapeHtml4(tmpStr);
        tmpStr = tmpStr.replaceAll(" ", "&nbsp;");
        tmpStr = tmpStr.replaceAll("    ", "&nbsp;&nbsp;&nbsp;&nbsp;");
        return StringUtils.join(tmpStr.split("\n"), "<br/>");
    }
}
