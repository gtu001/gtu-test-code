package gtu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RightFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain arg2) throws IOException, ServletException {
        // 獲取uri地址        
        HttpServletRequest request = (HttpServletRequest) sreq;
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        uri = uri.substring(ctx.length());
        //判斷admin級別網頁的流覽許可權        
        if (uri.startsWith("/admin")) {
            if (request.getSession().getAttribute("admin") == null) {
                request.setAttribute("message", "您沒有這個許可權");
                request.getRequestDispatcher("/login.jsp").forward(sreq, sres);
                return;
            }
        }
        //判斷manage級別網頁的流覽許可權        
        if (uri.startsWith("/manage")) {
        }
        //下面還可以添加其他的用戶許可權，省去。        

    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}