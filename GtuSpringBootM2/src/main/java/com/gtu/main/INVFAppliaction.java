package com.gtu.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
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
public class INVFAppliaction  implements CommandLineRunner  {

    private static Logger logger = LoggerFactory.getLogger(INVFAppliaction.class);
    
    @Autowired
    private Environment env;    
    
    public void run(String... args) throws Exception {
        logger.info("-----start");
        for(String profile : env.getActiveProfiles()) {
            logger.info(">> {}", profile);
        }
        logger.info("-----end");
    }

    /**
     * main
     * 
     * @param args
     */
    public static void main(String[] args) {
//        args = ArrayUtils.add(args, "--debug");//AUTO-CONFIGURATION REPORT
        System.setProperty("spring.config.name", "application");
        ApplicationContext applicationContext = SpringApplication.run(INVFAppliaction.class, args);
        logger.info("## spring.config.name = " + System.getProperty("spring.config.name"));
        logger.info("scan bean ========================= start");
        for (String name : applicationContext.getBeanDefinitionNames()) {
//            logger.info("\tscan bean => {}", name);
        }
        logger.info("scan bean ========================= end");
    }
}
