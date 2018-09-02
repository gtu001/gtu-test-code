package gtu.listener;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

public class MyHttpSessionBindingListener implements HttpSessionBindingListener {

    private static Logger logger = Logger.getLogger(MyHttpSessionListener.class);

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        logger.info("-----------valueBound : " + event.getName() + " -> " + event.getValue());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        logger.info("-----------valueUnbound : " + event.getName() + " -> " + event.getValue());
    }
}



