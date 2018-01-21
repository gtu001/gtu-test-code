package gtu.log;

public class LogDuring {
    private int startLine;
    private long startTime;

    public static LogDuring newInstance() {
        return new LogDuring();
    }

    LogDuring() {
        this.startLine = getLine();
        this.startTime = System.currentTimeMillis();
    }

    public String getLog() {
        return String.format("[during] (%d - %d) : %d ms  --> %s", //
                startLine, getLine(), System.currentTimeMillis() - startTime, getMethodName());
    }

    static String getMethodName() {
        StackTraceElement ste = getStackTraceElement();
        if (ste == null) {
            return "";
        } else {
            return ste.getMethodName();
        }
    }

    static int getLine() {
        StackTraceElement ste = getStackTraceElement();
        if (ste == null) {
            return -1;
        } else {
            return ste.getLineNumber();
        }
    }

    static StackTraceElement getStackTraceElement() {
        int pos = 0;
        boolean hasThisOk = false;
        boolean findOk = false;
        StackTraceElement[] stks = Thread.currentThread().getStackTrace();
        for (int ii = 0; ii < stks.length; ii++) {
            StackTraceElement s = stks[ii];
            //                System.out.println(ii + "..." + s);
            if (s.toString().indexOf("LogDuring") == -1 && hasThisOk) {
                pos = ii;
                findOk = true;
                break;
            } else if (s.toString().indexOf("LogDuring") != -1) {
                hasThisOk = true;
            }
        }
        if (findOk) {
            StackTraceElement s = stks[pos];
            return s;
        }
        return null;
    }
}