package gtu.jsp.errorPage;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter {

    private FilterConfig config;
    private RequestDispatcher dispatcherLogin;
    private RequestDispatcher dispatcherRegister;
    private static final String LOGIN_PAGE = "/login/Welcome.jsf";
    private static final String REGISTER_PAGE = "/login/Register.jsf";
    public static final String AUTH_STATUS = "login-status";

    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        dispatcherLogin = config.getServletContext().getRequestDispatcher(LOGIN_PAGE);
        dispatcherRegister = config.getServletContext().getRequestDispatcher(REGISTER_PAGE);
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        // if a request is made directly for Register.jsf send them to it
        if (request.getServletPath().equals(REGISTER_PAGE)) {
            dispatcherRegister.forward(req, res);
        }
        // otherwise if their login-status is not null
        else if (request.getSession(true).getAttribute(AUTH_STATUS) != null) {
            // if it is true, send them on to the next filter in the chain
            if (request.getSession(true).getAttribute(AUTH_STATUS) == Boolean.TRUE) {
                chain.doFilter(req, res);
                // otherwise send them to the login page
            } else {
                dispatcherLogin.forward(req, res);
            }
        }
        // if login-status is not set at all send them to the login page
        else {
            dispatcherLogin.forward(req, res);
        }
    }

    //XXX
    private void redirectJspPage(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpsr = (HttpServletRequest) request;
        HttpServletResponse httpresponse = (HttpServletResponse) response;
        String directpage = httpsr.getContextPath().concat("/verify.jsp");
        httpresponse.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        httpresponse.setHeader("Location", directpage);
    }

    public void destroy() {
        // TODO Auto-generated method stub
    }
}