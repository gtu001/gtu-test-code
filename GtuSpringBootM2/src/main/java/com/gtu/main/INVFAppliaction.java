package com.gtu.main;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application
 * 
 * @author nt82552
 *
 */
@SpringBootApplication()
@EnableCaching
@EnableAsync
@ComponentScan("com.gtu")
public class INVFAppliaction {

    private static Logger logger = LoggerFactory.getLogger(INVFAppliaction.class);

    /**
     * main
     * 
     * @param args
     */
    public static void main(String[] args) {
//        args = ArrayUtils.add(args, "--debug");//AUTO-CONFIGURATION REPORT
        ApplicationContext applicationContext = SpringApplication.run(INVFAppliaction.class, args);
        for (String name : applicationContext.getBeanDefinitionNames()) {
            logger.info("scan bean => {}", name);
        }
    }
}
