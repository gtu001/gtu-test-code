package com.iisigroup.ris;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;

public class Slf4jConsoleAppender<E> extends ConsoleAppender<E> {
    public boolean isStarted() {
        return super.isStarted();
    }

    public void doAppend(E object) {
        super.doAppend(object);
    }

    public FilterReply getFilterChainDecision(E object) {
        FilterReply filterReply = super.getFilterChainDecision(object);
        Rrefix refix = Rrefix.apply();
        if (refix.intoNewMethod()) {
            System.err.println(refix.newMethodInfo());
        }
        return filterReply;
    }

    static void DEBUG(Object message) {
        //        System.out.println(message);
    }

    static class Rrefix {

        private static Rrefix apply() {
            return new Rrefix().callLogStackTrace();
        }

        Rrefix() {
        }

        StackTraceElement currentStack;

        String prefix() {
            String fileName = currentStack.getFileName();
            String clz = currentStack.getClassName().substring(currentStack.getClassName().lastIndexOf(".") + 1);
            String method = currentStack.getMethodName();
            int line = currentStack.getLineNumber();
            return String.format("%s[%s.%s(%s:%d)] ", "\t", clz, method, fileName, line);
        }

        boolean intoNewMethod() {
            return INTO_NEW_METHOD;
        }

        String newMethodInfo() {
            String fileName = currentStack.getFileName();
            String clz = currentStack.getClassName().substring(currentStack.getClassName().lastIndexOf(".") + 1);
            String method = currentStack.getMethodName();
            int line = currentStack.getLineNumber();
            return String.format("## %s.%s(%s:%d) ...", clz, method, fileName, line);
        }

        private static boolean INTO_NEW_METHOD = false;

        private Rrefix callLogStackTrace() {
            StackTraceElement[] stacks = Thread.getAllStackTraces().get(Thread.currentThread());
            for (int ii = 0, total = stacks.length; ii < total; ii++) {
                DEBUG("ii = " + ii + "\t" + stacks[ii]);
                StackTraceElement stack = stacks[ii];
                if (!StackTraceWatcher.getInstance().containNonTraceStack(stack)) {
                    INTO_NEW_METHOD = StackTraceWatcher.getInstance().isfrist(stack);
                    currentStack = stack;
                    DEBUG("frist : " + currentStack);
                    break;
                }
            }
            return this;
        }
    }

    static class StackTraceWatcher {

        private static final StackTraceWatcher INSTANCE = new StackTraceWatcher();

        /** 註記不需要trace的stack */
        private Set<StackTraceElement> stacks;

        /** 註記現在呼叫Log的stack */
        private StackTraceElement route;

        private StackTraceWatcher() {
            stacks = new HashSet<StackTraceElement>();
            //StackTraceElement(declaringClass, methodName, fileName, lineNumber)
            stacks.add(new StackTraceElement("java.lang.Thread", "dumpThreads", null, -2));
            stacks.add(new StackTraceElement("java.lang.Thread", "dumpThreads", "Thread.java", -2));
            stacks.add(new StackTraceElement("java.lang.Thread", "getAllStackTraces", null, -1));
            stacks.add(new StackTraceElement("java.lang.Thread", "getAllStackTraces", "Thread.java", 1530));
            addClassAllDeclaredMethods(Rrefix.class, stacks);
            addClassAllDeclaredMethods(Slf4jConsoleAppender.class, stacks);
            addClassAllDeclaredMethods(UnsynchronizedAppenderBase.class, stacks);
            addClassAllDeclaredMethods(AppenderAttachableImpl.class, stacks);
            addClassAllDeclaredMethods(ch.qos.logback.classic.Logger.class, stacks);
            addClassAllDeclaredMethods(org.apache.commons.logging.impl.SLF4JLocationAwareLog.class, stacks);
        }

        private void showStackInfo(Set<StackTraceElement> stacks) {
            List<StackTraceElement> list = new ArrayList<StackTraceElement>(stacks);
            Collections.sort(list, new Comparator<StackTraceElement>() {
                public int compare(StackTraceElement paramT1, StackTraceElement paramT2) {
                    return paramT1.getClassName().compareTo(paramT2.getClassName());
                }
            });
            for (StackTraceElement stack : list) {
                System.out.println("NONTRACE = " + stack);
            }
        }

        void addClassAllDeclaredMethods(Class<?> clz, Set<StackTraceElement> stacks) {
            Set<String> mname = new HashSet<String>();
            for (Method method : clz.getDeclaredMethods()) {
                mname.add(method.getName());
            }
            Pattern pat = Pattern.compile("^(.*\\.)(\\w+)(\\$.*)$");
            Matcher matcher = null;
            for (String methodName : mname) {
                String clzName = clz.getSimpleName();
                if (clz.getName().indexOf("$") != -1) {
                    matcher = pat.matcher(clz.getName());
                    if (matcher.find() && matcher.groupCount() == 3) {
                        clzName = matcher.group(2);
                    }
                }
                stacks.add(new StackTraceElement(clz.getName(), methodName, clzName + ".java", -1));
            }
        }

        /**
         * 若route不存在 ,表示此method為第一次呼叫 則回傳 true 否則 表示此method為以呼叫過Log 則回傳false
         * 
         * @param stack
         * @return
         */
        private boolean isfrist(StackTraceElement stack) {
            if (equalStack(route, stack)) {
                DEBUG("debug : 相同 = " + route + "\t" + stack);
                return false;
            } else {
                DEBUG("debug : 不相同 = " + route + "->\t" + stack);
                route = stack;
                return true;
            }
        }

        private boolean containNonTraceStack(StackTraceElement stack) {
            boolean compares = containStack(stacks, stack);
            if (!compares) {
                DEBUG("### [not in NonTraceStack] = " + compares + " => " + stack);
                DEBUG("\tgetClassName ==" + stack.getClassName());
                DEBUG("\tgetFileName ==" + stack.getFileName());
                DEBUG("\tgetMethodName ==" + stack.getMethodName());
                DEBUG("\tgetLineNumber ==" + stack.getLineNumber());
            }
            return compares;
        }

        private boolean containStack(Set<StackTraceElement> stacks, StackTraceElement stack) {
            for (StackTraceElement compareTo : stacks) {
                if (equalStack(compareTo, stack)) {
                    return true;
                }
            }
            return false;
        }

        public boolean equalStack(StackTraceElement stack1, StackTraceElement stack2) {
            if (stack1 == null && stack2 == null) {
                return true;
            }
            if (stack1 != null && stack2 == null) {
                return false;
            }
            if (stack1 == null && stack2 != null) {
                return false;
            }
            if (stack1 != null && stack2 != null) {
                boolean b1 = stack1.getClassName() != null ? stack1.getClassName().equals(stack2.getClassName())
                        : stack2.getClassName() == null;
                boolean b2 = stack1.getMethodName() != null ? stack1.getMethodName().equals(stack2.getMethodName())
                        : stack2.getMethodName() == null;
                boolean b3 = stack1.getFileName() != null ? stack1.getFileName().equals(stack2.getFileName()) : stack2
                        .getFileName() == null;
                return b1 && b2 && b3;
            }
            throw new RuntimeException("能跑到這裡就太神了..." + stack1 + " , " + stack2);
        }

        private static StackTraceWatcher getInstance() {
            return INSTANCE;
        }
    }
}
