package gtu.webservice.eg2;

import javax.jws.WebService;

@WebService(endpointInterface = "gtu.webservice.HelloWorld", serviceName = "HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {
        return "Hello " + text;
    }
}