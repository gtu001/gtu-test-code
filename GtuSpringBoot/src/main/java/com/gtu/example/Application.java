package com.gtu.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableJpaRepositories("org.baeldung.persistence.repo") 
//@EntityScan("org.baeldung.persistence.model")

@SpringBootApplication  // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class Application {
    public static void main(String[] args) throws Exception {
        System.setProperty("spring.devtools.restart.enabled", "true"); //自動重啟開關 (false關閉)
        SpringApplication.run(Application.class, args);
    }
}