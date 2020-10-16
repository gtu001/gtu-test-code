package gtu.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gtu.test.EmployeeController.Employee;

//https://www.javainuse.com/spring/spring-cloud-netflix-feign-tutorial
@FeignClient(name = "employee-producer")
public interface EmployeeRemoteCallService {
    @RequestMapping(method = RequestMethod.GET, value = "/employee")
    public Employee getData();
}
