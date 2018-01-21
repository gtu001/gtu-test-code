package gtu.net.urlConn.demo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DemoService extends HttpServlet {
    private static final long serialVersionUID = 1589233260870765446L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 从请求中获取客户端传入的流信息
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
                req.getInputStream()));
        try {
            // 获取客户端传来的信息对象
            System.out.println((String) in.readObject());
            in.close();

            // 将信息返回给客户端
            ObjectOutputStream out = new ObjectOutputStream(resp
                    .getOutputStream());
            out.flush();

            // 将信息对象传给客户端
            out.writeObject("给 客户端返回的结果，就是我啦！");
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
