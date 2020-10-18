//package gtu.test;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.client.RestClientException;
//
//import gtu.test.EmployeeController.Employee;
//
//@Controller
//public class EmployeeConsumerControllerClient {
//
//    @Autowired
//    private EmployeeRemoteCallService loadBalancer;
//
//    @RequestMapping(method = RequestMethod.GET, name="/test_feign_employee")
//    public void getEmployee() throws RestClientException, IOException {
//        try {
//            Employee emp = loadBalancer.getData();
//            System.out.println(emp.getName());
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//    }
//}