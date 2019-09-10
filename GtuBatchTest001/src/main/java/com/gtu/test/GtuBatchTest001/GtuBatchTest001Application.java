package com.gtu.test.GtuBatchTest001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Configuration
@ComponentScan("com.gtu.test")
@Component
@EnableScheduling
public class GtuBatchTest001Application {

    public static void main(String[] args) {
        SpringApplication.run(GtuBatchTest001Application.class, args);
    }
}
