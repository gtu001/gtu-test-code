package com.packtpub.mmj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching//<-----XXX
@SpringBootApplication
public class CaffeineCacheSampleApp {
    public static void main(String[] args) {
        SpringApplication.run(CaffeineCacheSampleApp.class, args);
    }
}
