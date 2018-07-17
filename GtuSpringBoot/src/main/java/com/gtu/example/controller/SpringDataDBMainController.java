package com.gtu.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/springdata-dbMain/")
public class SpringDataDBMainController {

    @GetMapping("/dbMain")
    public ModelAndView dbMain() {
        ModelAndView model = new ModelAndView();
        model.setViewName("db_process");
        return model;
    }

    @GetMapping(name = "/tables", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getTableLst() {
        List<String> tabLst = new ArrayList<String>();
        tabLst.add("Address");
        tabLst.add("Employee");
        tabLst.add("WorkItem");
        return JSONArray.toJSONString(tabLst);
    }
}
