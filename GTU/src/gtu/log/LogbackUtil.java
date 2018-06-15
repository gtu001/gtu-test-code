package gtu.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import gtu.collection.ListUtil;

public class LogbackUtil {
    private LogbackUtil() {
    }
    
    public static Logger getLogger(Class<?> clz) {
        return LoggerFactory.getLogger(clz);
    }

    public static void setRootLevel(Level level) {
        LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
        Logger rootLogger = context.getLogger("ROOT");
        setLevel(rootLogger, level);
    }

    public static JoranConfigurator configure() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        context.reset();
        return configurator;
    }

    public static void showConfig() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
        StatusPrinter.print(context);
    }

    public static void showSystemLogger() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ListUtil.showListInfo(context.getLoggerList());
    }

    public static void setLevel(Logger log, Level level) {
        if (log instanceof ch.qos.logback.classic.Logger) {
            ((ch.qos.logback.classic.Logger) log).setLevel(level);
        }else {
            System.err.println("無法設定log Level");
        }
    }
}
