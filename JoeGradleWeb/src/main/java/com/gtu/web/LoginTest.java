package com.gtu.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginTest {

    @RequestMapping(value = "loginTest_test", method = RequestMethod.GET)
    public ModelAndView loginTest_test() {
        List<Map<String, String>> lst = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("aaa", "bbb");
        lst.add(map);

        ModelAndView model = new ModelAndView("/ex1/indexTest");
        model.addObject("list", lst);
        return model;
    }
}
