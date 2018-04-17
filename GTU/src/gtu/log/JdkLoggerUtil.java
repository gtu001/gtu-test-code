package gtu.log;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JdkLoggerUtil {

    private static final Logger log = Logger.getLogger(JdkLoggerUtil.class.getCanonicalName());
    private static final Logger rootlog = Logger.getLogger("");
    
    public static void setupRootLogLevel(Level rootLevel) {
        rootlog.setLevel(rootLevel);
        for (Handler handler : rootlog.getHandlers()) {
            handler.setLevel(rootLevel);
        }
    }

    public static void main(String[] args) {

        Formatter customFormatter = new java.util.logging.Formatter() {
            @Override
            public String format(LogRecord record) {
                System.out.println(record.getThrown());
                return "[" + record.getLevel() + "]" + record.getSourceClassName() + "." + record.getSourceMethodName() + ":" + record.getMessage() + "\n";
            }
        };

        Handler[] handlers = rootlog.getHandlers();
        for (Handler handler : handlers) {
             handler.setFormatter(new SimpleFormatter());
        }

        rootlog.setLevel(Level.ALL);
        for (Handler handler : rootlog.getHandlers()) {
            handler.setLevel(Level.ALL);
        }
        // log.setLevel(myLevel);
        // rootlog.setLevel(globalLevel);

        log.fine("fine");
        log.finer("finer");
        log.finest("finest");
        log.warning("warning");
        log.severe("severe");
    }

}
