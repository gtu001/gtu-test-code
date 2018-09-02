package gtu.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebUtil {
    
    private static final Logger logger = Logger.getLogger(WebUtil.class);

    public static WebApplicationContext getApplicationContext(){
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
        // for(int ii = 0 ; ii < context.getBeanDefinitionCount() ; ii ++){
        // logger.debug("bean - " + context.getBeanDefinitionNames()[ii]);
        // }
        return context;
    }
    
    public static HttpServletRequest getRequest(){
        return ServletActionContext.getRequest();
    }
    
    public static HttpSession getSession(){
        return ServletActionContext.getRequest().getSession();
    }
}
