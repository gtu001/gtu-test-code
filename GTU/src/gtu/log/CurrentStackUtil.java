package gtu.log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gtu.log.CurrentStackUtil.StackTraceWatcher;

public class CurrentStackUtil {

    public static void main(String[] args) {
        StackTraceWatcher stack2 = CurrentStackUtil.StackTraceWatcher.getInstance();
        CurrentStackUtil currentStackUtil = CurrentStackUtil.getInstance();
        StackTraceElement stack = currentStackUtil.apply().currentStack();
        System.out.println(stack);
    }

    public static boolean DEBUG = false;

    public static CurrentStackUtil getInstance() {
        return INSTANCE;
    }

    public StackTraceElement currentStack() {
        return currentStack.get();
    }

    public boolean isIntoNewMethod() {
        return intoNewMethod.get();
    }

    private ThreadLocal<StackTraceElement> currentStack = new ThreadLocal<StackTraceElement>();
    private ThreadLocal<Boolean> intoNewMethod = new ThreadLocal<Boolean>();

    public CurrentStackUtil apply() {
        StackTraceElement[] stacks = Thread.getAllStackTraces().get(Thread.currentThread());
        for (int ii = 0, total = stacks.length; ii < total; ii++) {
            DEBUG("ii = " + ii + "\t" + stacks[ii]);
            StackTraceElement stack = stacks[ii];
            if (!StackTraceWatcher.getInstance().containNonTraceStack(stack)) {
                intoNewMethod.set(StackTraceWatcher.getInstance().isfrist(stack));
                currentStack.set(stack);
                DEBUG("frist : " + currentStack);
                break;
            }
        }
        return this;
    }

    public static class StackTraceWatcher {

        public static StackTraceWatcher getInstance() {
            return INSTANCE.get();
        }

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
            addClassAllDeclaredMethods(Log.Systen.out.class, stacks);
            addClassAllDeclaredMethods(Log.Setting.class, stacks);
            addClassAllDeclaredMethods(StackTraceWatcher.class, stacks);
            addClassAllDeclaredMethods(CurrentStackUtil.class, stacks);
        }

        public StackTraceWatcher showStackInfo() {
            if (!DEBUG) {
                return this;
            }
            List<StackTraceElement> list = new ArrayList<StackTraceElement>(stacks);
            Collections.sort(list, new Comparator<StackTraceElement>() {
                public int compare(StackTraceElement paramT1, StackTraceElement paramT2) {
                    return paramT1.getClassName().compareTo(paramT2.getClassName());
                }
            });
            for (StackTraceElement stack : list) {
                DEBUG(String.format("NONTRACE = %s %s %s", stack.getClassName(), stack.getMethodName(),
                        stack.getFileName()));
            }
            return this;
        }

        public StackTraceWatcher addClass(Class<?> clz) {
            addClassAllDeclaredMethods(clz, stacks);
            return this;
        }

        private void addClassAllDeclaredMethods(Class<?> clz, Set<StackTraceElement> stacks) {
            Set<String> mname = new HashSet<String>();
            for (Method method : clz.getDeclaredMethods()) {
                mname.add(method.getName());
            }
            String clzName = clz.getName();
            if (clz.getPackage() == null) {
                if (clzName.indexOf("$") != -1) {
                    clzName = clzName.substring(0, clzName.indexOf("$"));
                }
            } else {
                int pos1 = clzName.lastIndexOf(".") + 1;
                int pos2 = clzName.indexOf("$");
                if (pos1 == -1 || pos2 == -1) {
                    clzName = clz.getSimpleName();
                } else {
                    clzName = clzName.substring(pos1, pos2);
                }
            }
            for (String methodName : mname) {
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

        private boolean equalStack(StackTraceElement stack1, StackTraceElement stack2) {
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

        private static final ThreadLocal<StackTraceWatcher> INSTANCE = new ThreadLocal<StackTraceWatcher>() {
            protected StackTraceWatcher initialValue() {
                return new StackTraceWatcher();
            }
        };
    }

    private static void DEBUG(Object message) {
        if (DEBUG) {
            System.err.println("DEBUG : " + message);
        }
    }

    private static final CurrentStackUtil INSTANCE = new CurrentStackUtil();
}
