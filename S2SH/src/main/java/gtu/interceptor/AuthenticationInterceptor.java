package gtu.interceptor;

import gtu.constant.Constants;
import gtu.model.entity.User;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthenticationInterceptor implements Interceptor {

    private static final long serialVersionUID = 1L;

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
        User user = (User) session.get(Constants.USER_KEY);
        boolean isAuthenticated = user != null;
        if (!isAuthenticated) {
            return Action.LOGIN;
        } else {
            return actionInvocation.invoke();
        }
    }
}
