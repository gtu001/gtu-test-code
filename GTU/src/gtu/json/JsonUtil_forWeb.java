package gtu.json;

import java.util.Collection;

import org.apache.commons.lang3.StringEscapeUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil_forWeb {

    public static String toJsonStr(Object obj) {
        String val = "";
        if(obj instanceof Collection) {
            val = JSONArray.fromObject(obj).toString();
        }else {
            val = JSONObject.fromObject(obj).toString();
        }
        return StringEscapeUtils.escapeJson(val);
    }
}
