package com.packtpub.mmj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class FeignSampleApp {
    public static void main(String[] args) {
        SpringApplication.run(FeignSampleApp.class, args);
        //https://cloud.spring.io/spring-cloud-netflix/multi/multi__service_discovery_eureka_clients.html
    }
}
