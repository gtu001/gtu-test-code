package gtu.log;

import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

public class LogbackUtil_silent {
    private LogbackUtil_silent() {
    }

    public static void setRootLevel(_LogbackUtil_silent_LEVEL level) {
        try {
            Class<?> clz = Class.forName("org.slf4j.LoggerFactory");
            Object loggerContext = MethodUtils.invokeExactStaticMethod(clz, "getILoggerFactory", new Object[0]);
            Object logger = MethodUtils.invokeMethod(loggerContext, "getLogger", new Object[] { "ROOT" }, new Class[] { String.class });

            Class<?> clz2 = Class.forName("ch.qos.logback.classic.Level");
            Object levelObj = FieldUtils.readDeclaredStaticField(clz2, level.name(), true);

            MethodUtils.invokeMethod(logger, "setLevel", new Object[] { levelObj }, new Class[] { clz2 });
        } catch (Exception e) {
            throw new RuntimeException("setRootLevel ERR : " + e.getMessage(), e);
        }
    }

    public enum _LogbackUtil_silent_LEVEL {
        OFF, ERROR, WARN, INFO, DEBUG, TRACE, ALL
    }
}
