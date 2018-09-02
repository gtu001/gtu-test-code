package com.cmsad.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdController {
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("#. AdController -- postConstruct ");;
    }

    // 廣告列表頁
    @RequestMapping(value = "go_test_gtu")
    public String goTestGtu(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX goTestGtu");
        return "test_gtu";
    }
}
