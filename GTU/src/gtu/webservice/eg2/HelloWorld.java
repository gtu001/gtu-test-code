package gtu.webservice.eg2;

import javax.jws.WebService;

@WebService
public interface HelloWorld {
    String sayHi(String text);
}