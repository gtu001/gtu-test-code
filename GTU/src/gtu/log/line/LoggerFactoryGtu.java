package gtu.log.line;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import gtu.lineapp.ex1.LineAppNotifiyHelper_Simple;

/**
 * Created by wistronits on 2018/8/10.
 */

public class LoggerFactoryGtu {

    private static final String DEFAULT_TAG = "<Unknowed Class>";

    private static final Class<?>[] IGNORE_TOAST_CLZ = new Class[] { //
    };//

    public static Logger getLogger() {
        return new Logger();
    }

    public static class Logger {

        private Logger() {
        }

        public void debug(String message) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[debug]" + message);
        }

        public void debug(String message, Throwable e) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[debug]" + message);
            e.printStackTrace();
        }

        public void info(String message) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[info ]" + message);
        }

        public void info(String message, Throwable e) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[info ]" + message);
            e.printStackTrace();
        }

        public void warn(String message) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[warn ]" + message);
        }

        public void warn(String message, Throwable e) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[warn ]" + message);
            e.printStackTrace();
        }

        public void error(String message) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[error]" + message);
        }

        public void error(String message, Throwable e) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[error]" + message);
            e.printStackTrace();
        }

        public void line(String message) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[line ]" + message);
            LineAppNotifiyHelper_Simple.getInstance().send(info.getTag() + " : " + message);
        }

        public void line(String message, Throwable e) {
            final ClassInfo info = new ClassInfo(LoggerFactoryGtu.class);
            System.out.println(info.getTag() + "[line ]" + message);
            e.printStackTrace();
            LineAppNotifiyHelper_Simple.getInstance().send(info.getTag() + message + "======" + ExceptionUtils.getMessage(e));
        }
    }

    private static class ClassInfo {
        private String simpleName;
        private Class<?> clz;
        private int lineNumber = -1;
        private String methodName = "";
        private StackTraceElement stk;
        private Class ignoreClz;

        private ClassInfo(Class ignoreClz) {
            this.ignoreClz = ignoreClz;
            stk = getCurrentStackTrace();
            if (stk != null) {
                try {
                    clz = Class.forName(stk.getClassName());
                    simpleName = clz.getSimpleName();
                } catch (Exception e) {
                }

                lineNumber = stk.getLineNumber();
                methodName = stk.getMethodName();
            }
        }

        private StackTraceElement getCurrentStackTrace() {
            StackTraceElement[] sks = Thread.currentThread().getStackTrace();
            StackTraceElement currentElement = null;
            boolean findThisOk = false;
            for (int ii = 0; ii < sks.length; ii++) {
                if (StringUtils.equals(sks[ii].getFileName(), ignoreClz.getSimpleName() + ".java")) {
                    findThisOk = true;
                }
                if (findThisOk && //
                        !StringUtils.equals(sks[ii].getFileName(), ignoreClz.getSimpleName() + ".java")) {
                    currentElement = sks[ii];
                    break;
                }
            }
            if (currentElement != null) {
                return currentElement;
            }
            return null;
        }

        private String getTag() {
            if (StringUtils.isEmpty(simpleName)) {
                return "NA";
            }
            methodName = StringUtils.isNotEmpty(methodName) ? methodName : "<na>";
            return simpleName + "." + methodName + ":" + lineNumber;
        }
    }
}
