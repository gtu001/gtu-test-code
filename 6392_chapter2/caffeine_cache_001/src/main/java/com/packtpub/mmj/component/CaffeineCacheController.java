package com.packtpub.mmj.component;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.packtpub.mmj.lib.model.Customer;

@RestController
@RequestMapping("caffeine")
public class CaffeineCacheController {

    @Autowired
    CustomerService customerService;

    /**
     * http://localhost:8080/caffeine/getCustomer/123
     */
    @RequestMapping("/getCustomer/{customerId}")
    public String getCustomer(@PathVariable(value = "customerId") String customerId) {
        Long customId = Long.parseLong(customerId);
        Customer customer = customerService.getCustomer(customId);
        StringBuilder sb = new StringBuilder();
        sb.append("hashcode : " + customer.hashCode() + "\r\n");
        sb.append("customer : " + ReflectionToStringBuilder.toString(customer, ToStringStyle.MULTI_LINE_STYLE) + "\r\n");
        return sb.toString();
    }
}
