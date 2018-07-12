package com.gtu.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.example.common.SecurityConfig;

@RestController
@RequestMapping("/api/books")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/home")
    public String home() {
        log.info("home!!");
        return "Hello World!";
    }
    
    @GetMapping
    public String findAll(){
        log.info("findAll!!");
        return "OK";
    }

    @RequestMapping(value = "/gtu001Test", method = RequestMethod.GET)
    public String gtu001Test() {
        log.info("gtu001Test!!");
        return "OK";
    }
}
