package com.gtu.example;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.gtu.example.rabbitmq.sender.RabbitMqSender;

//@EnableJpaRepositories("org.baeldung.persistence.repo") 
//@EntityScan("org.baeldung.persistence.model")

@SpringBootApplication // = @Configuration + @EnableAutoConfiguration +
                       // @ComponentScan
public class Application {
    
    @Autowired
    private ConfigurableApplicationContext ctx;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);

    public static void main(String[] args) throws Exception {
        System.setProperty("spring.devtools.restart.enabled", "true"); // 自動重啟開關
                                                                       // (false關閉)
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                logger.info("\t bean : " + beanName);
            }
        };
    }
}