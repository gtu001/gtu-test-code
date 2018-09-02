package gtu.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaUtilLoggerTest {

    private static Logger log = Logger.getLogger(JavaUtilLoggerTest.class.getName());

    /**
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("是否會顯示log...");
        System.out.println("ALL = " + log.isLoggable(Level.ALL));
        System.out.println("CONFIG = " + log.isLoggable(Level.CONFIG));
        System.out.println("FINE = " + log.isLoggable(Level.FINE));
        System.out.println("FINEST = " + log.isLoggable(Level.FINEST));
        System.out.println("INFO = " + log.isLoggable(Level.INFO));
        System.out.println("OFF = " + log.isLoggable(Level.OFF));
        System.out.println("SEVERE = " + log.isLoggable(Level.SEVERE));
        System.out.println("WARNING = " + log.isLoggable(Level.WARNING));

        log.log(Level.INFO, "test1");

        log.log(Level.SEVERE, "test2", new Exception());

        log.info("test 3");
        log.warning("test 4");
    }

}
