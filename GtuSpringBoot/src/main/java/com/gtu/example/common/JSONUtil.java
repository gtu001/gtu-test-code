package com.gtu.example.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class JSONUtil {

    private static final Logger log = LoggerFactory.getLogger(JSONUtil.class);

    public static JSONObject getThrowable(Throwable ex) {
        log.error("<<ERROR>> : " + ex.getMessage(), ex);
        JSONObject json = new JSONObject();
        json.put("messsage", ex.getMessage());
        json.put("result", "error");
        json.put("exception", ExceptionUtils.getFullStackTrace(ex));
        return json;
    }

    public static JSONObject getThrowablePrecise(Throwable ex) {
        log.error("<<ERROR>> : " + ex.getMessage(), ex);
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(ex.getClass().getSimpleName() + " : " + ex.getMessage() + "\r\n");
        } while ((ex = ex.getCause()) != null);
        JSONObject json = new JSONObject();
        json.put("messsage", sb.toString());
        json.put("result", "error");
        return json;
    }

    public static JSONObject getThrowableRoot(Throwable ex) {
        log.error("<<ERROR>> : " + ex.getMessage(), ex);
        Throwable root = ExceptionUtils.getRootCause(ex);
        if (root == null) {
            root = ex;
        }
        JSONObject json = new JSONObject();
        json.put("messsage", root.getClass().getSimpleName() + " : " + root.getMessage());
        json.put("result", "error");
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
