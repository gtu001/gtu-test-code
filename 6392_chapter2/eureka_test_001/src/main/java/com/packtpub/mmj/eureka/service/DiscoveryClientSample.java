package com.packtpub.mmj.eureka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
public class DiscoveryClientSample implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(DiscoveryClientSample.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void run(String... strings) throws Exception { // print the Discovery
                                                          // Client Description
        System.out.println(discoveryClient.description());
        // Get restaurant-service instances and prints its info
        discoveryClient.getInstances("REST-TEST-001")//
                .forEach((ServiceInstance serviceInstance) -> {
                    logger.info(new StringBuilder("Instance --> ")//
                            .append(serviceInstance.getServiceId()).append("\nServer: ")//
                            .append(serviceInstance.getHost()).append(":")//
                            .append(serviceInstance.getPort()).append("\nURI: ")//
                            .append(serviceInstance.getUri()).append("\n\n\n").toString());
                });
    }
}