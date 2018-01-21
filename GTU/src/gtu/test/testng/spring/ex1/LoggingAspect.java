package gtu.test.testng.spring.ex1;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

public class LoggingAspect {

    private final ThreadLocal<Integer> level = new ThreadLocal<Integer>();

    public LoggingAspect() {
        level.set(0);
    }

    public Object log(ProceedingJoinPoint call) throws Throwable {
        Signature signature = new Signature(call);
        Logger l = LoggerFactory.getLogger(signature.getTargetClass());

        int i = level.get();
        l.debug("{}==>{}", getPadding(i), signature);

        try {
            increaseLevel();
            long startTime = System.currentTimeMillis();
            Object point = call.proceed();
            long endTime = System.currentTimeMillis();

            if (endTime - startTime > 1000) {
                l.info("{}({})<=={}", new Object[] { getPadding(i), endTime - startTime, call.getSignature().getName() });
            }
            l.debug("{}<==({}){}", new Object[] { getPadding(i), point, call.getSignature().getName() });

            return point;
        } catch (Throwable e) {
            l.error("{}==>{}", getPadding(i), signature);
            resetLevel(i);
            throw e;
        } finally {
            decreaseLevel();
        }
    }

    private void resetLevel(int i) {
        level.set(i);
    }

    private void increaseLevel() {
        level.set(level.get() + 1);
    }

    private void decreaseLevel() {
        level.set(level.get() - 1);
    }

    private String getPadding(int level) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < level; i++) {
            buf.append(" ");
        }
        return buf.toString();
    }

    private class Signature {

        private final ProceedingJoinPoint call;

        private Class<?> clazz;

        private StringBuffer buf;

        public Signature(ProceedingJoinPoint call) {
            this.call = call;
            clazz = call.getTarget().getClass();
            if (AopUtils.isCglibProxyClass(clazz)) {
                clazz = clazz.getSuperclass();
            }
        }

        public Class<?> getTargetClass() {
            return clazz;
        }

        @Override
        public String toString() {
            if (buf == null) {
                buf = new StringBuffer();
                String clazzName = getTargetClass().getSimpleName();
                if (clazzName.endsWith("Impl")) {
                    buf.append(clazzName.substring(0, clazzName.length() - "Impl".length()));
                } else {
                    buf.append(clazzName);
                }
                buf.replace(0, 1, String.valueOf(buf.charAt(0)).toLowerCase());
                buf.append(".");
                buf.append(call.getSignature().getName());
                buf.append("(");
                for (int i = 0; i < call.getArgs().length; i++) {
                    Object param = call.getArgs()[i];
                    if (param instanceof String) {
                        buf.append("\"").append(param).append("\"");
                    } else {
                        buf.append(param);
                    }
                    if (i < call.getArgs().length - 1) {
                        buf.append(",");
                    }
                }
                buf.append(")");
            }
            return buf.toString();
        }
    }
}