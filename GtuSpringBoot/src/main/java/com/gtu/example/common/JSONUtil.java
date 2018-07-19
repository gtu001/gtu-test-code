package com.gtu.example.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import net.sf.json.JSONObject;

public class JSONUtil {

    public static JSONObject getThrowable(Throwable ex) {
        JSONObject json = new JSONObject();
        json.put("messsage", ex.getMessage());
        json.put("result", "error");
        json.put("exception", ExceptionUtils.getFullStackTrace(ex));
        return json;
    }

    public static JSONObject getSuccess(String message) {
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(message)) {
            message = "執行成功!";
        }
        json.put("messsage", message);
        json.put("result", "success");
        return json;
    }
}
