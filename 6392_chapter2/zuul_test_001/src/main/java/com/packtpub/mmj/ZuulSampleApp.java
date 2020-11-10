package com.packtpub.mmj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@EnableDiscoveryClient // <-- 含在 EnableZuulProxy
@SpringBootApplication
public class ZuulSampleApp {
    public static void main(String[] args) {
        SpringApplication.run(ZuulSampleApp.class, args);
    }
}

// page 161

// http://localhost:8761/dashboard
// http://localhost:8765/rest_test_001/calculation/power?base=33&exponent=2