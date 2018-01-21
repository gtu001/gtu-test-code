package gtu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CharsetEncodingFilter implements Filter {
    private FilterConfig config = null;
    private String RequestEncoding = null;
    private String ResponseEncoding = null;

    public void init(FilterConfig arg0) throws ServletException {
        this.config = arg0;
        this.RequestEncoding = config.getInitParameter("RequestEncoding");
        this.ResponseEncoding = config.getInitParameter("ResponseEncoding");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc)
            throws IOException, ServletException {
        if (this.config == null) {
            return;
        }
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        CharacterEncodingHttpServletRequestWrapper mrequestw = new CharacterEncodingHttpServletRequestWrapper(
                request, ResponseEncoding);
        CharacterEncodingHttpServletResponseWrapper wresponsew = new CharacterEncodingHttpServletResponseWrapper(
                response, ResponseEncoding);
        fc.doFilter(mrequestw, wresponsew);
    }

    public void destroy() {
    }

}
