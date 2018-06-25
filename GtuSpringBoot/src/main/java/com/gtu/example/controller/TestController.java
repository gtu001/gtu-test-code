package com.gtu.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class TestController {

    @RequestMapping("/home")
    public String home() {
        System.out.println("home!!");
        return "Hello World!";
    }
    
    @GetMapping
    public String findAll(){
        System.out.println("findAll!!");
        return "OK";
    }

    @RequestMapping(value = "/gtu001Test", method = RequestMethod.GET)
    public String gtu001Test() {
        System.out.println("gtu001Test!!");
        return "OK";
    }
}
