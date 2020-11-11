package com.packtpub.mmj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCircuitBreaker
@SpringBootApplication
public class HystrixStartApp {
    public static void main(String[] args) {
        SpringApplication.run(HystrixStartApp.class, args);
    }
}