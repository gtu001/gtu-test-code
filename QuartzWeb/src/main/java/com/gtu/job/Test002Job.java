package com.gtu.job;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("test002Job")
public class Test002Job {
    
    private Logger logger = Logger.getLogger(getClass());
    
    public void printMessage() {
        logger.info("Test002Job running...");
    }
}