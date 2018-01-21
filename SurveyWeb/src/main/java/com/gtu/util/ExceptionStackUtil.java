package com.gtu.util;

import java.io.PrintWriter;
import java.io.StringWriter;
//import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ExceptionStackUtil {
    
    private static Logger logger = Logger.getLogger(ExceptionStackUtil.class);

    /**
     * 處理錯誤訊息, 顯示在頁面
     */
    public static RuntimeException parse(Class<?> clz, Throwable ge, HttpServletRequest request) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ge.printStackTrace(pw);
        Throwable parentThrowEx = ge;
        while ((parentThrowEx = parentThrowEx.getCause()) != null) {
            parentThrowEx.printStackTrace(pw);
        }
        request.setAttribute("exceptionStack", sw.toString());
//        Logger.getLogger("ExceptionStackUtil").log(java.util.logging.Level.SEVERE, ge.getMessage(), ge);
        logger.error(ge.getMessage(), ge);
        ActionMessageUtils.saveErrors(request, ge.getMessage());
        return new RuntimeException(ge.getMessage(), ge);
    }
}
