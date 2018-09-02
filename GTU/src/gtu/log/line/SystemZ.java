package gtu.log.line;

import org.apache.commons.lang3.StringUtils;

import gtu.lineapp.ex1.LineAppNotifiyHelper_Simple;

public class SystemZ {

    public static class out {
        public static void println(Object message) {
            ClassInfo inf = new ClassInfo(SystemZ.class);
            System.out.println(inf.getTag() + " : " + message);
        }

        public static void format(String format, Object... args) {
            ClassInfo inf = new ClassInfo(SystemZ.class);
            System.out.format(inf.getTag() + " : " + format, args);
        }

        public static void line(String message) {
            ClassInfo inf = new ClassInfo(SystemZ.class);
            System.out.println(inf.getTag() + " : " + message);
            LineAppNotifiyHelper_Simple.getInstance().send(inf.getTag() + " : " + message);
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
