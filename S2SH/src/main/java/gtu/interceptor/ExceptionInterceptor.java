package gtu.interceptor;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ExceptionInterceptor extends AbstractInterceptor {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(ExceptionInterceptor.class);

    @Override
    public void init() {
        super.init();
    }

    private boolean trace = false;
    
    public ExceptionInterceptor() {
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }
    
    public boolean isTrace() {
        return trace;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            return invocation.invoke();
        } catch (Exception e) {
            //呼叫Exception Handler
            ActionProxy proxy = invocation.getProxy();
            //Object result = ctrl.process(new HandlerParameter(e, invocation.getAction(), proxy.getMethod(), null));   
            logger.error(e.getMessage(), e);
            throw e;            
        }
    }
}
