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

        ModelAndView model = new ModelAndView("/old_jsp/index_test.jsp");
        model.addObject("list", lst);
        return model;
    }

    @RequestMapping(value = "thymeleaf_test", method = RequestMethod.GET)
    public ModelAndView thymeleaf_test() {
        List<Map<String, String>> lst = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("aaaa", "a1");
        map.put("bbbb", "b1");
        map.put("cccc", "c1");
        lst.add(map);

        List<TestBean> lst2 = new ArrayList<TestBean>();
        lst2.add(new TestBean("v1", "Lbl1"));
        lst2.add(new TestBean("v2", "Lbl2"));
        lst2.add(new TestBean("v3", "Lbl3"));
        lst2.add(new TestBean("v4", "Lbl4"));

        ModelAndView model = new ModelAndView("/thymeleaf/thymeleaf_base_test_001.html");
        model.addObject("XXXXXXXX_MapList", lst);
        model.addObject("XXXXXXXX_List", lst2);
        model.addObject("formTp", "formTp");
        model.addObject("statusNm", "statusNm");
        model.addObject("status", "status");
        model.addObject("modNo", "modNo");
        return model;
    }

    public static class TestBean {
        String XXXXXX_value;
        String XXXXXX_label;

        public TestBean(String xXXXXX_value, String xXXXXX_label) {
            super();
            XXXXXX_value = xXXXXX_value;
            XXXXXX_label = xXXXXX_label;
        }

        public String getXXXXXX_value() {
            return XXXXXX_value;
        }

        public void setXXXXXX_value(String xXXXXX_value) {
            XXXXXX_value = xXXXXX_value;
        }

        public String getXXXXXX_label() {
            return XXXXXX_label;
        }

        public void setXXXXXX_label(String xXXXXX_label) {
            XXXXXX_label = xXXXXX_label;
        }
    }
}
