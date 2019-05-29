package gtu.json;

import java.util.Collection;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.BeanUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil_forWeb {

    public static String toJsonStr(Object obj) {
        String val = "";
        if (obj instanceof Collection) {
            val = JSONArray.fromObject(obj).toString();
        } else {
            val = JSONObject.fromObject(obj).toString();
        }
        return StringEscapeUtils.escapeJson(val);
    }

    public static <T> void reverseToBean(String jsonString, T form) {
        T form1 = (T) JSONObject.toBean(JSONObject.fromObject(jsonString), form.getClass());
        BeanUtils.copyProperties(form1, form);
    }
}
