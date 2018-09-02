package gtu.jsp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * User's session 加入servletContext，以便可得知線上使用者資訊
 * 
 * @author <a href="mailto:chris@mail.omniwise.com.tw">Chris</a>
 * @version 2008/2/13:上午 9:49:13
 */
public class OnlineSessionListener implements HttpSessionListener {
    public final static String ONLINE_USER_SESSION_MAP = "online_user_session_map";

    private ServletContext servletContext;
    private Map sessions;

    public OnlineSessionListener() {

    }

    private synchronized void addSession(String id, HttpSession hs) {
        this.sessions.put(id, hs);
    }

    private synchronized void deleteSession(String id) {
        try {
            this.sessions.remove(id);
        } catch (Exception e) {
        }
        ;
    }

    // session.setAttribute(PipelineSessionConstants.PIPELINE_SESSION_NAME,
    // sessionMap);
    public synchronized void sessionCreated(HttpSessionEvent hse) {
        this.servletContext = hse.getSession().getServletContext();
        Object tmpMap = this.servletContext.getAttribute(OnlineSessionListener.ONLINE_USER_SESSION_MAP);
        if (null == tmpMap) {
            this.sessions = new HashMap();
            this.servletContext.setAttribute(OnlineSessionListener.ONLINE_USER_SESSION_MAP, this.sessions);
        } else {
            this.sessions = (Map) tmpMap;
        }
        this.addSession(hse.getSession().getId(), hse.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent hse) {
        this.deleteSession(hse.getSession().getId());
    }

}