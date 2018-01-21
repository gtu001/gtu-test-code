package gtu.webservice.eg1;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(targetNamespace = "http://localhost/sample")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class Calculator {
    @WebMethod
    public int add(int a, int b) {
        return a + b;
    }
}