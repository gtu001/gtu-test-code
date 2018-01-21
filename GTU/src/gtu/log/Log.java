/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.log;

import gtu.file.FileUtil;
import gtu.log.CurrentStackUtil.StackTraceWatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.commons.lang.time.DateFormatUtils;

public class Log {
    
    public static class Systen {
        public static class out {
            public static void println(Object... value){
            	if(value.getClass().isArray() && value.length > 1){
            		Log.debug(value);
            	}else if(value.length == 0){
            		Log.debug("");
            	}else {
            		Log.debug(value[0]);
            	}
            }
        }
    }

    public static void debug(Object messages) {
        setting.doLog(messages);
    }

    public static void debug(String format, Object... messages) {
        setting.doLog(format, messages);
    }

    public static void debug(Throwable ex, String format, Object... messages) {
        setting.doLog(ex, format, messages);
    }

    public static void debug(Throwable ex) {
        setting.doLog(ex);
    }

    public static void debug(Object... messages) {
        setting.doLog(messages);
    }

    public static void debug(Throwable ex, Object... messages) {
        setting.doLog(ex, messages);
    }
    
    static {
        // TODO 設定預設值
        Setting.SIMPLE.apply();
    }

    private static Setting setting;
    static PrintStream writer;
    private static boolean DEBUG = false;

    static {
        StackTraceWatcher stack = CurrentStackUtil.StackTraceWatcher.getInstance();
        stack.addClass(Setting.class).addClass(Log.class);
        for (Setting s : Setting.class.getEnumConstants()) {
            stack.addClass(s.getClass());
        }
        // CurrentStackUtil.StackTraceWatcher.getInstance().showStackInfo();
    }

    public enum Setting {
        QUITE_EXCEPT_EXCEPTION {
            public void apply() {
                setting = this;
                LIMIT_TRACE = 99;
                BEFORE_METHOD_LOG = BeforeMethodLog.ON;
                WRITE_FILE = WriteFile.ON;
                info();
            }

            void doLog(String format, Object... messages) {
            }

            void doLog(Object messages) {
            }

            void doLog(Throwable ex, String format, Object... messages) {
                sysoutLog(ex, format, messages);
            }

            void doLog(Throwable ex) {
                sysoutLog(ex);
            }

            void doLog(Object... messages) {
            }

            void doLog(Throwable ex, Object... messages) {
                sysoutLog(ex, messages);
            }
        },
        QUITE {
            public void apply() {
                setting = this;
                LIMIT_TRACE = 0;
                BEFORE_METHOD_LOG = BeforeMethodLog.OFF;
                WRITE_FILE = WriteFile.OFF;
                info();
            }

            void doLog(String format, Object... messages) {
            }

            void doLog(Object messages) {
            }

            void doLog(Throwable ex, String format, Object... messages) {
            }

            void doLog(Throwable ex) {
            }

            void doLog(Object... messages) {
            }

            void doLog(Throwable ex, Object... messages) {
            }
        },
        SIMPLE {
            public void apply() {
                setting = this;
                LIMIT_TRACE = 0;
                BEFORE_METHOD_LOG = BeforeMethodLog.OFF;
                WRITE_FILE = WriteFile.OFF;
                info();
            }

            void doLog(Throwable ex, String format, Object... messages) {
                sysoutLog(ex, format, messages);
            }

            void doLog(Throwable ex) {
                sysoutLog(ex);
            }

            void doLog(String format, Object... messages) {
                sysoutLog(format, messages);
            }

            void doLog(Object messages) {
                sysoutLog(messages);
            }

            void doLog(Object... messages) {
                sysoutLog(messages);
            }

            void doLog(Throwable ex, Object... messages) {
                sysoutLog(ex, messages);
            }
        }, //
        NORMAL {
            public void apply() {
                setting = this;
                LIMIT_TRACE = 2;
                BEFORE_METHOD_LOG = BeforeMethodLog.OFF;
                WRITE_FILE = WriteFile.ON;
                info();
            }

            void doLog(Throwable ex, String format, Object... messages) {
                sysoutLog(ex, format, messages);
            }

            void doLog(Throwable ex) {
                sysoutLog(ex);
            }

            void doLog(String format, Object... messages) {
                sysoutLog(format, messages);
            }

            void doLog(Object messages) {
                sysoutLog(messages);
            }

            void doLog(Object... messages) {
                sysoutLog(messages);
            }

            void doLog(Throwable ex, Object... messages) {
                sysoutLog(ex, messages);
            }
        }, //
        FULL {
            public void apply() {
                setting = this;
                LIMIT_TRACE = 10000;
                BEFORE_METHOD_LOG = BeforeMethodLog.ON;
                WRITE_FILE = WriteFile.ON;
                info();
            }

            void doLog(Throwable ex, String format, Object... messages) {
                sysoutLog(ex, format, messages);
            }

            void doLog(Throwable ex) {
                sysoutLog(ex);
            }

            void doLog(String format, Object... messages) {
                sysoutLog(format, messages);
            }

            void doLog(Object messages) {
                sysoutLog(messages);
            }

            void doLog(Object... messages) {
                sysoutLog(messages);
            }

            void doLog(Throwable ex, Object... messages) {
                sysoutLog(ex, messages);
            }
        }, //
        ;//

        private static int LIMIT_TRACE;
        private static BeforeMethodLog BEFORE_METHOD_LOG;
        private static WriteFile WRITE_FILE;

        public abstract void apply();

        abstract void doLog(String format, Object... messages);

        abstract void doLog(Object messages);

        abstract void doLog(Throwable ex, String format, Object... messages);

        abstract void doLog(Throwable ex);

        abstract void doLog(Object... messages);

        abstract void doLog(Throwable ex, Object... messages);

        // root start ......
        void sysoutLog(Object... messages) {
            System.out.println(beforeSysoutLog() + Arrays.toString(messages));
            appendSb(beforeSysoutLog() + Arrays.toString(messages));

        }

        void sysoutLog(Throwable ex, Object... messages) {
            System.err.println(beforeSysoutLog() + Arrays.toString(messages));
            appendSb(beforeSysoutLog() + Arrays.toString(messages));
            throwableStackTrace(ex);
        }

        void sysoutLog(Object messages) {
            System.out.println(beforeSysoutLog() + messages);
            appendSb(beforeSysoutLog() + messages);
        }

        void sysoutLog(String format, Object... messages) {
            System.out.println(beforeSysoutLog() + String.format(format, messages));
            appendSb(beforeSysoutLog() + String.format(format, messages));
        }

        void sysoutLog(Throwable ex, String format, Object... messages) {
            System.err.println(beforeSysoutLog() + ex + " : " + String.format(format, messages));
            appendSb(beforeSysoutLog() + ex + " : " + String.format(format, messages));
            throwableStackTrace(ex);
        }

        void sysoutLog(Throwable ex) {
            System.err.println(beforeSysoutLog() + ex);
            appendSb(beforeSysoutLog() + ex);
            throwableStackTrace(ex);
        }

        CurrentStackUtil currentStackUtil = CurrentStackUtil.getInstance();

        String beforeSysoutLog() {
            StackTraceElement stack = currentStackUtil.apply().currentStack();
            String clzName = stack.getClassName();
            // String fileName = stack.getFileName();
            int line = stack.getLineNumber();
            String methodName = stack.getMethodName();
            if (BEFORE_METHOD_LOG == BeforeMethodLog.ON && currentStackUtil.isIntoNewMethod()) {
                System.err.println(String.format("# %s.%s(%d) ...", clzName, methodName, line));
                appendSb(String.format("# %s.%s(%d) ...", clzName, methodName, line));
            }
            return String.format("(%s.java:%d)[%s]", clzName, line, methodName);
        }

        void throwableStackTrace(Throwable ex) {
            ex.printStackTrace();
            if(writer!=null){
                ex.printStackTrace(writer);
            }
//            StackTraceElement[] trace = ex.getStackTrace();
//            for (int ii = 0, total = trace.length; ii < total; ii++) {
//                System.out.println("\t" + trace[ii]);
//                appendSb("\t" + trace[ii]);
//            }
        }

        static void appendSb(Object message) {
            if(WRITE_FILE == WriteFile.ON){
                initWriter();
                try {
                    if (message == null) {
                        writer.println("null");
                        writer.flush();
                    } else {
                        writer.println(message.toString());
                        writer.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        static void initWriter(){
            if(writer == null){
                String fileDir = FileUtil.DESKTOP_PATH + File.separatorChar + "temp" + File.separatorChar ;
                String fileName = "log_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".log";
                File file = new File(fileDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                try {
                    writer = new PrintStream(new FileOutputStream(new File(fileDir, fileName)), true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // root end ......

        public static void info() {
            System.err.println("Log.Setting : " + setting);
            System.err.println("\tLIMIT_TRACE : " + LIMIT_TRACE);
            System.err.println("\tBEFORE_METHOD_LOG : " + BEFORE_METHOD_LOG);
            System.err.println("\tLOG_IN_STRINGBUILDER : " + WRITE_FILE);
        }
    }

    private static void DEBUG(Object message) {
        if (DEBUG) {
            System.err.println("DEBUG : " + message);
        }
    }
    
    public enum WriteFile {
        ON, OFF;

        public void apply() {
            Setting.WRITE_FILE = this;
        }
    }

    public enum BeforeMethodLog {
        ON, OFF, ;

        public void apply() {
            Setting.BEFORE_METHOD_LOG = this;
        }
    }
}
