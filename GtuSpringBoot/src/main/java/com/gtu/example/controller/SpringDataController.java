package com.gtu.example.controller;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.example.springdata.dao.EmployeeRepository;
import com.gtu.example.springdata.entity.Employee;

@RestController
@RequestMapping("/springdata/")
public class SpringDataController {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/create")
    public String createOne() {
        String uuid = UUID.randomUUID().toString();
        Employee vo = new Employee("F_" + uuid, "L_" + uuid, "D_" + uuid);
        employeeRepository.save(vo);
        log.info(ReflectionToStringBuilder.toString(vo));
        return ReflectionToStringBuilder.toString(vo);
    }

    @RequestMapping("/findAll")
    public String findAll() {
        Iterable<Employee> iter = employeeRepository.findAll();
        Stream<Employee> targetStream = StreamSupport.stream(iter.spliterator(), false);
        return targetStream.map((vo) -> ReflectionToStringBuilder.toString(vo))//
                .reduce("", (v1, v2) -> v1 += v2 + "\n");
    }
}
