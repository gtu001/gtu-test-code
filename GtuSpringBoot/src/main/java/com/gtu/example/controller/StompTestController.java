package com.gtu.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/stomp_test/")
public class StompTestController {

    private static final Logger log = LoggerFactory.getLogger(StompTestController.class);

    @GetMapping(value = "/stomp_test")
    public ModelAndView stomp_test() {
        ModelAndView model = new ModelAndView();
        model.setViewName("stomp_test");// stomp_test XXX
        return model;
    }
    
    @GetMapping(value = "/stomp_test_ex1")
    public ModelAndView stomp_test_ex1() {
        ModelAndView model = new ModelAndView();
        model.setViewName("stomp_test_ex1");// stomp_test XXX
        return model;
    }
    
    @GetMapping(value = "/stomp_test_ex2")
    public ModelAndView stomp_test_ex2() {
        ModelAndView model = new ModelAndView();
        model.setViewName("stomp_test_ex2");// stomp_test XXX
        return model;
    }
}
