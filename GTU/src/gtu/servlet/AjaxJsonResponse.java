package gtu.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

@WebServlet("/AjaxJsonResponse")
public class AjaxJsonResponse extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AjaxJsonResponse() {
        super();
    }

    private static Logger logger = Logger.getLogger(AjaxJsonResponse.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class<?> outClazz = Object.class;
            Object outObject = new Object();

            String resultString = null;

            if (String.class.equals(outClazz)) {
                resultString = outObject.toString();
                response.setContentType("text/html;charset=UTF-8");
            } else {
                resultString = JSONObject.fromObject(outObject).toString();
                response.setContentType("application/json;charset=UTF-8");
            }

            PrintWriter pw = response.getWriter();
            pw.write(resultString);
            pw.flush();
        } catch (Exception t) {
            logger.error(t.getMessage(), t);

            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(t.getMessage());
            pw.flush();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
