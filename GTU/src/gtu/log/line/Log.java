package gtu.log.line;

import gtu.log.CurrentStackUtil;
import gtu.log.CurrentStackUtil.StackTraceWatcher;

public class Log {

    public static boolean debugMode = true;
    private static StackTraceWatcher stack2;
    private static CurrentStackUtil currentStackUtil;
    static {
        stack2 = CurrentStackUtil.StackTraceWatcher.getInstance();
        stack2.addClass(Log.class);
        currentStackUtil = CurrentStackUtil.getInstance();
    }

    private static String getPrefix() {
        StackTraceElement element = currentStackUtil.apply().currentStack();
        String prefix = "(" + element.getClassName() + ":" + element.getLineNumber() + ") - ";
        return prefix;
    }

    public static void v(String tag, String message) {
        if (!debugMode) {
            return;
        }

        System.out.println(getPrefix() + "" + tag + " - " + message);
    }

    public static void e(String tag, String message) {
        if (!debugMode) {
            return;
        }
        System.out.println(getPrefix() + "" + tag + " - " + message);
    }

    public static void e(String tag, String message, Throwable ex) {
        if (!debugMode) {
            return;
        }
        System.out.println(getPrefix() + "" + tag + " - " + message);
        ex.printStackTrace();
    }
}
