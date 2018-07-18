package com.gtu.example.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gtu.example.springdata.entity.Address;
import com.gtu.example.springdata.entity.Employee;
import com.gtu.example.springdata.entity.WorkItem;

@RestController
@RequestMapping("/springdata-dbMain/") 
public class SpringDataDBMainController {

    private static final Logger log = LoggerFactory.getLogger(SpringDataDBMainController.class);

    @GetMapping(value = "/hello")   
    public String hello() {    
        return "hello";     
    }

    @GetMapping(value = "/tables")
    public String tables() {
        List<String> tabLst = new ArrayList<String>();
        tabLst.add("Address"); 
        tabLst.add("Employee"); 
        tabLst.add("WorkItem");
        return JSONArray.toJSONString(tabLst);
    }

    @GetMapping(value = "/dbMain")
    public ModelAndView dbMain() {
        ModelAndView model = new ModelAndView();  
        model.setViewName("db_process"); 
        return model;
    }

    @GetMapping(value = "/table-get-columns")
    public String tableColumns(@RequestParam(name = "table") String table) {
        // 不word 
        Reflections reflections = new Reflections("com.gtu.example.springdata.entity");
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        allClasses.add(WorkItem.class); 
        allClasses.add(Employee.class);    
        allClasses.add(Address.class);    
  
        log.info("<<" + allClasses); 

        Optional<List<String>> obj = allClasses.stream()//
                .filter(c -> c.getSimpleName().equalsIgnoreCase(table))//
                .findFirst().map(c -> {
                    return Stream.of(c.getDeclaredFields())//
                            .map(Field::getName)//
                            .collect(Collectors.toList());
                });
        String rtnStr = JSONArray.toJSONString(obj.get());
        log.info("return : {}", rtnStr);
        return rtnStr;
    }
}
