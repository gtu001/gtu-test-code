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
        model.addObject("entity", new EntityBean());
        return model;
    }

    public static class EntityBean {
        String testLabel;
        String testText;
        String testHidden;
        String testRadio1;
        String testRadio2;
        String testSelect1;
        String testSelect2;
        String testChk1;
        String testChk2;
        String testArea;

        public String getTestLabel() {
            return testLabel;
        }

        public void setTestLabel(String testLabel) {
            this.testLabel = testLabel;
        }

        public String getTestText() {
            return testText;
        }

        public void setTestText(String testText) {
            this.testText = testText;
        }

        public String getTestHidden() {
            return testHidden;
        }

        public void setTestHidden(String testHidden) {
            this.testHidden = testHidden;
        }

        public String getTestRadio1() {
            return testRadio1;
        }

        public void setTestRadio1(String testRadio1) {
            this.testRadio1 = testRadio1;
        }

        public String getTestRadio2() {
            return testRadio2;
        }

        public void setTestRadio2(String testRadio2) {
            this.testRadio2 = testRadio2;
        }

        public String getTestSelect1() {
            return testSelect1;
        }

        public void setTestSelect1(String testSelect1) {
            this.testSelect1 = testSelect1;
        }

        public String getTestSelect2() {
            return testSelect2;
        }

        public void setTestSelect2(String testSelect2) {
            this.testSelect2 = testSelect2;
        }

        public String getTestChk1() {
            return testChk1;
        }

        public void setTestChk1(String testChk1) {
            this.testChk1 = testChk1;
        }

        public String getTestChk2() {
            return testChk2;
        }

        public void setTestChk2(String testChk2) {
            this.testChk2 = testChk2;
        }

        public String getTestArea() {
            return testArea;
        }

        public void setTestArea(String testArea) {
            this.testArea = testArea;
        }
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
