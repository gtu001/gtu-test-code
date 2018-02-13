package gtu.listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.log4j.Logger;

/**
 * <listener> <description>test1</description>
 * <listener-class>com.fuco.mb.web.listener.MyHttpSessionAttributeListener</listener-class>
 * </listener>
 */
public class MyHttpSessionAttributeListener implements HttpSessionAttributeListener {

    private static Logger logger = Logger.getLogger(MyHttpSessionListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        logger.info("======== attributeAdded : " + se.getName() + " -> " + se.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        logger.info("======== attributeRemoved : " + se.getName() + " -> " + se.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        logger.info("======== attributeReplaced : " + se.getName() + " -> " + se.getValue());
    }
}
