package com.gtu.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

//@EnableJpaRepositories("org.baeldung.persistence.repo") 
//@EntityScan("org.baeldung.persistence.model")

@SpringBootApplication // = @Configuration + @EnableAutoConfiguration +
                       // @ComponentScan

// DB run 不起來的話要加這個
// @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
//
// 若被強制 redirect 到 login畫面
// @EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class.class })

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired 
    private ConfigurableApplicationContext ctx;

    public static void main(String[] args) throws Exception {
        // System.setProperty("spring.profiles.active", "rabbitmq") ;
        System.setProperty("spring.profiles.active", "spring-data");
        ConfigurableApplicationContext ctx2 = SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            log.info("Let's inspect the beans provided by Spring Boot:"); 

            String[] beanNames = ctx.getBeanDefinitionNames();  
            Arrays.sort(beanNames); 
            for (String beanName : beanNames) { 
                String beanPrefix = "";
                if (ctx.getBean(beanName).getClass().getName().startsWith("com.gtu")) {
                    beanPrefix = "<GTU>";       
                }   
 
                log.info("\t {} bean : {}", beanPrefix, beanName);   
            }   
        }; 
    }

    private void inspectBean(Object bean) {
        for (Field f : bean.getClass().getDeclaredFields()) {
            log.info("f---" + f.getName() + "\t" + f.getType().getSimpleName());
        }
        for (Method m : bean.getClass().getDeclaredMethods()) {
            log.info("m---" + m.getName() + "\t" + m.getReturnType().getSimpleName());
        }
    }
}