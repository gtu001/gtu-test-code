package gtu.listener;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

public class MyHttpSessionActivationListener implements HttpSessionActivationListener {

    private static Logger logger = Logger.getLogger(MyHttpSessionListener.class);

    @Override
    public void sessionWillPassivate(HttpSessionEvent se) {
        logger.info("--------------sessionWillPassivate");
    }

    @Override
    public void sessionDidActivate(HttpSessionEvent se) {
        logger.info("--------------sessionDidActivate");
    }
}


