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

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ConfigurableApplicationContext ctx;

    public static void main(String[] args) throws Exception {
//         System.setProperty("spring.profiles.active", "rabbitmq");
        System.setProperty("spring.profiles.active", "spring-data");
        SpringApplication.run(Application.class, args);
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
}