package gtu.db.access.exp1;

/* 控制組件 */

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Troy 2009/02/02
 * 
 */
public class loginServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=BIG5";

    // 初始化Servlet
    public void init() throws ServletException {
    }

    // 處理 HTTP POST請求
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 從請求中取出用戶名和密碼的值
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 生成一個ArrayList對象，並把用戶名和密碼的值存入該對象中
        ArrayList arr = new ArrayList();
        arr.add(username);
        arr.add(password);

        // 生成一個Session 對象
        HttpSession session = request.getSession(true);
        session.removeAttribute("username");
        session.setAttribute("username", username);

        // 調用模型組件loginHandler，檢查該用戶是否已注冊
        loginHandler login = new loginHandler();
        boolean mark = login.checkLogin(arr);

        // 如果已注冊，進入主頁面
        if (mark)
            response.sendRedirect("main.jsp");
        // 如果未注冊，進入注冊頁面
        else
            response.sendRedirect("register.jsp");

    }

    // 處理 HTTP GET請求
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    // 銷毀Servlet
    public void destroy() {
    }
}