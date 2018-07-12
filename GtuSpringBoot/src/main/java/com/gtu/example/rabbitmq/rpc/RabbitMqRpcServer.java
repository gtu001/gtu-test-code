package com.gtu.example.rabbitmq.rpc;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile(value = { "rabbitmq", "rpc" })
@Service
public class RabbitMqRpcServer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqRpcServer.class);

    @RabbitListener(queues = "#{rpcQueue.name}")
    public String fibonacci(String reqData) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info("[rpc server][receiver] {} Received request for " + reqData, methodName);
        String newResponseData = "RESP_" + UUID.randomUUID().toString();
        return newResponseData;
    }
}