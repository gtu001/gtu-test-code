package gtu.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * <listener> <description>test2</description>
 * <listener-class>com.fuco.mb.web.listener.MyHttpSessionListener</listener-class>
 * </listener>
 */
public class MyHttpSessionListener implements HttpSessionListener {

    private static Logger logger = Logger.getLogger(MyHttpSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("++++++++++++++++  sessionCreated");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("++++++++++++++++  sessionDestroyed");
    }
}
