package gtu.webservice.eg1;

import javax.xml.ws.Endpoint;

public class WebServiceStarter extends HttpServlet {
    public void init() throws ServletException {
        try {
            Endpoint.publish("http://localhost:8088/calcuator", new Calculator());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}