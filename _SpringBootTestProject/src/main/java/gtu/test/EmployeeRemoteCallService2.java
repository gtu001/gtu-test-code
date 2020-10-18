package gtu.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//https://www.javainuse.com/spring/spring-cloud-netflix-feign-tutorial
@FeignClient(name = "github-test-producer")
public interface EmployeeRemoteCallService2 {
    @RequestMapping(method = RequestMethod.GET, value = "/posts")
    public String getData();
}
