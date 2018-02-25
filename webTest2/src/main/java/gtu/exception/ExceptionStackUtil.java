package gtu.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class ExceptionStackUtil {

    public static String parseToString(Throwable ge) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ge.printStackTrace(pw);
        Throwable parentThrowEx = ge;
        while ((parentThrowEx = parentThrowEx.getCause()) != null) {
            parentThrowEx.printStackTrace(pw);
        }
        return sw.toString();
    }

    /**
     * 處理錯誤訊息, 顯示在頁面
     */
    public static RuntimeException parse(Throwable ge, HttpServletRequest request) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ge.printStackTrace(pw);
        Throwable parentThrowEx = ge;
        while ((parentThrowEx = parentThrowEx.getCause()) != null) {
            parentThrowEx.printStackTrace(pw);
        }
        request.setAttribute("exceptionStack", sw.toString());
        Logger.getLogger("ExceptionStackUtil").log(java.util.logging.Level.SEVERE, ge.getMessage(), ge);
        return new RuntimeException(ge.getMessage(), ge);
    }

    /**
     * 處理錯誤訊息, 顯示在頁面
     */
    public static void doThrow(Throwable ge, HttpServletRequest request) {
        throw ExceptionStackUtil.parse(ge, request);
    }
}
