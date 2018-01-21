package gtu.db.hook;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Troy 2009/06/09
 * 
 */
public class Log4jInit {

    static Logger logger;

    public void init(Class cls) {

        Properties logp = new Properties();
        try {
            logp.load(Log4jInit.class.getResourceAsStream("log4j.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PropertyConfigurator.configure(logp);
        logger = Logger.getLogger(cls.getName());
        logger.setLevel(Level.DEBUG);
        logger.debug("log4j_init_ok!!");
    }

    public Log4jInit(Class cls) {
        init(cls);
    }

    public void debug(String xxx) {
        logger.debug(xxx);
    }

}