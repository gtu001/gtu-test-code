package gtu.timer;

import java.io.IOException;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <servlet></servlet>
 * < servlet >
 * < servlet-name >taskservlet< / servlet-name >
 * < servlet-class >com.task< /servlet-class >
 * < init-param >
 * < param-name >startTask< /param-name >
 * < param-value >true< /param-value >
 * < /init-param >
 * < init-param >
 * < param-name >intervalTime< /param-name >
 * < param-value >1< /param-value >
 * < /init-param >
 * < load-on-startup >300< /load-on-startup >
 * < /servlet >
 * <servlet></servlet>
 * 
 * @author Troy 2012/1/8
 */
public class ConvergeDataServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Timer timer1 = null;

    public ConvergeDataServlet() {
        super();
    }

    public void destroy() {
        super.destroy();
        if (timer1 != null) {
            timer1.cancel();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    // init方法启动定时器
    public void init() throws ServletException {

        ServletContext context = getServletContext();

        // (true为用定时间刷新缓存)
        String startTask = getInitParameter("startTask");

        // 定时刷新时间(分钟)
        Long delay = Long.parseLong(getInitParameter("delay"));

        // 启动定时器
        if (startTask.equals("true")) {
            timer1 = new Timer(true);
            // task1 = new Task(context);
            // timer1.schedule(task1, delay * 60 * 1000, delay * 60 * 1000);
        }
    }
}