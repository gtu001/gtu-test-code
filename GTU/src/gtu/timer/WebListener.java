package gtu.timer;

import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class WebListener implements ServletContextListener {

    private Timer timer = null;

    private Logger log = Logger.getLogger(getClass());

    final String separator = System.getProperty("file.separator");

    public void contextInitialized(ServletContextEvent event) {
        // 在这里初始化监听器，在tomcat启动的时候监听器启动，可以在这里实现定时器功能
        ServletContext context = event.getServletContext();

        timer = new Timer();

        log.info("定时器已启动");// 添加日志，可在tomcat日志中查看到

        // timer.schedule(new LEDTimerTack(), 20000, runTime);

        log.info("已经添加任务");
    }

    public void contextDestroyed(ServletContextEvent event) {// 在这里关闭监听器，所以在这里销毁定时器。
        timer.cancel();
        log.info("定时器销毁");
    }
}