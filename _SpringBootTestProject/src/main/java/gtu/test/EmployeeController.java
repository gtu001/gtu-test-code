package gtu.test;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class EmployeeController {

    @RequestMapping(method = RequestMethod.GET, name = "/employee")
    public Employee getEmployee() {
        Employee mEmployee = new Employee();
        mEmployee.setName("Troy");
        mEmployee.setSalary(new BigDecimal(10000));
        return mEmployee;
    }

    public static class Employee {
        String name;
        BigDecimal salary;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }
    }
}
