package gtu.log.fakeAndroid;

import gtu.log.CurrentStackUtil;
import gtu.log.CurrentStackUtil.StackTraceWatcher;

public class Log {

    private static StackTraceWatcher stack2;
    private static CurrentStackUtil currentStackUtil;
    static {
        stack2 = CurrentStackUtil.StackTraceWatcher.getInstance();
        stack2.addClass(Log.class);
        currentStackUtil = CurrentStackUtil.getInstance();
    }

    public static void v(String tag, String message) {
        StackTraceElement element = currentStackUtil.apply().currentStack();
        String prefix = element.getClassName() + ":" + element.getLineNumber() + " - ";
        System.out.println(prefix + "" + tag + " - " + message);
    }
    
    public static void e(String tag, String message) {
        StackTraceElement element = currentStackUtil.apply().currentStack();
        String prefix = element.getClassName() + ":" + element.getLineNumber() + " - ";
        System.out.println(prefix + "" + tag + " - " + message);
    }
    
    public static void e(String tag, String message, Throwable ex) {
        StackTraceElement element = currentStackUtil.apply().currentStack();
        String prefix = element.getClassName() + ":" + element.getLineNumber() + " - ";
        System.out.println(prefix + "" + tag + " - " + message);
        ex.printStackTrace();
    }
}
