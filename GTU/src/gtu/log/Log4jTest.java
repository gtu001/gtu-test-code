package gtu.log;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author Troy 2009/02/02
 * 
 */
public class Log4jTest {

    public static void main(String[] args) {
        System.out.println("------------------------------");
        Logger log = Logger.getLogger(Log4jTest.class);
        Properties logp = new Properties();
        try {
            logp.load(Log4jTest.class.getClassLoader().getResourceAsStream("gtu/log/log4j.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // org.apache.log4j.PropertyConfigurator.configure(logp);

        log.removeAllAppenders();
        log.addAppender(new ConsoleAppender(new PatternLayout("%p %t %m%n"), ConsoleAppender.SYSTEM_OUT));

        log.debug("xxxxxx");
    }
}
