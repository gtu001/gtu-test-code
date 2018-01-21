package com.gtu.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("anotherBean")
public class AnotherBean {
    private Logger logger = Logger.getLogger(AnotherBean.class);

    public void test() {
        logger.info("AnotherBean running...");
    }
}
