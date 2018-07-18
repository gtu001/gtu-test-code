package com.gtu.example.controller;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/home")
    public String home() {
        log.info("home!!");
        return "Hello World!";
    }

    @GetMapping(value = "/findAll")
    public String findAll() {
        log.info("findAll!!");
        return "OK";
    }

    @PostMapping(value = "/postTest")
    public String postTest(@Valid @RequestBody ReqBean bean) {
        log.info("postTest!!");
        log.info(ReflectionToStringBuilder.toString(bean));
        return "OK";
    }

    @RequestMapping(value = "/gtu001Test", method = RequestMethod.GET)
    public String gtu001Test() {
        log.info("gtu001Test!!");
        return "OK";
    }

    @RequestMapping(value = "/find/{id}")
    public String findById(@PathVariable("id") long id) {
        return "you find id = " + id;
    }
    
    @GetMapping(value = "/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    public static class ReqBean {
        String test_column1;
        String test_column2;

        public String getTest_column1() {
            return test_column1;
        }

        public void setTest_column1(String test_column1) {
            this.test_column1 = test_column1;
        }

        public String getTest_column2() {
            return test_column2;
        }

        public void setTest_column2(String test_column2) {
            this.test_column2 = test_column2;
        }
    }
}
