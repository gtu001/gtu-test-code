package gtu.log.finder.etc;

import java.util.Collection;

import org.apache.commons.lang3.StringEscapeUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DebugMointerUIHtmlUtil {

    public static String replaceChangeLineToBr(String message) {
        if (message == null) {
            return "";
        }
        return message.replaceAll("[\n]", "\n<br/>");
    }

    public static String toJsonStr(Object obj) {
        String val = "";
        if (obj instanceof Collection) {
            val = JSONArray.fromObject(obj).toString();
        } else {
            val = JSONObject.fromObject(obj).toString();
        }
        return StringEscapeUtils.escapeJson(val);
    }
}
