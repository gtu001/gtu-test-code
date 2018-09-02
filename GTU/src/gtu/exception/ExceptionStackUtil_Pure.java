package gtu.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionStackUtil_Pure {
    
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
}
