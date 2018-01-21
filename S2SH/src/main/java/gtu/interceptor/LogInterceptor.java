package gtu.interceptor;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LogInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(LogInterceptor.class);

    @Override
    public void init() {
        super.init();
    }

    public LogInterceptor() {
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String actionClsName = invocation.getAction().getClass().getSimpleName();
        String namespace = invocation.getProxy().getNamespace();
        String actionName = invocation.getProxy().getActionName();
        String methodName = invocation.getProxy().getMethod();
        String formatStr = String.format("action[%s],namespace[%s],act_name[%s],method[%s]", actionClsName, namespace, actionName, methodName);
        String rtnVal = null;
        logger.debug("#. " + formatStr + " .s");
        rtnVal = invocation.invoke();
        logger.debug("#. " + formatStr + String.format(" -> result[%s]", rtnVal)+ " .e");
        return rtnVal;
    }
}
