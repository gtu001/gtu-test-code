package gtu.log;

import gtu.collection.ListUtil;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackUtil {
    private LogbackUtil() {
    }

    private static Logger log = LoggerFactory.getLogger(LogbackUtil.class);

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

    public static void setAll(Logger log) {
        setLevel(log, Level.ALL);
    }

    public static void setDebug(Logger log) {
        setLevel(log, Level.DEBUG);
    }

    public static void setError(Logger log) {
        setLevel(log, Level.ERROR);
    }

    public static void setInfo(Logger log) {
        setLevel(log, Level.INFO);
    }

    public static void setOff(Logger log) {
        setLevel(log, Level.OFF);
    }

    public static void setTrace(Logger log) {
        setLevel(log, Level.TRACE);
    }

    public static void setWarn(Logger log) {
        setLevel(log, Level.WARN);
    }

    static Logger root() {
        if (root == null) {
            LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
            root = context.getLogger("ROOT");
        }
        return root;
    }

    private static LevelSetInfo TURNINFO = LevelSetInfo.ON;

    enum LevelSetInfo {
        ON, OFF
    }

    static void rootWarn(String message) {
        if (TURNINFO == LevelSetInfo.ON) {
            root().warn(message);
        }
    }

    static Logger root;

    static void setLevel(Logger log, Level level) {
        if (log instanceof ch.qos.logback.classic.Logger) {
            ((ch.qos.logback.classic.Logger) log).setLevel(level);
            rootWarn("set level [" + level + "] : " + log.getName());
            return;
        }
        rootWarn("set level set failed : " + log.getName());
    }
}
