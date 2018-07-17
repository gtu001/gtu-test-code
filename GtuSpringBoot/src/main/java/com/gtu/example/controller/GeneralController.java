package com.gtu.example.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/GtuSpringBoot/")
public class GeneralController {

    // 不work
    // @RequestMapping(value = "/main")
    // public String main(Model model) {
    // model.addAttribute("test_message", "GTU測試!!");
    // return "redirect:/GtuSpringBoot/main2";
    // }

    @GetMapping("/main")
    public ModelAndView main(@RequestParam(required = false) String path) {
        ModelAndView model = new ModelAndView();
        if (StringUtils.isBlank(path)) {
            model.setViewName("main_page");
            model.addObject("message", "未設定path!!");
        } else {
            model.setViewName("redirect:" + path);
        }
        return model;
    }
}
