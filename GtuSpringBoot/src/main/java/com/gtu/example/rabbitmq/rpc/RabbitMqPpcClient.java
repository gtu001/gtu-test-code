package com.gtu.example.rabbitmq.rpc;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gtu.example.rabbitmq.RabbitMqBeanDefiner;

@Profile(value = { "rabbitmq" })
@Service
public class RabbitMqPpcClient {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqPpcClient.class);

    @Autowired
    private RabbitTemplate template;

    @Autowired
    @Qualifier("rpcDirectExchange")
    private DirectExchange exchange;


    @Scheduled(fixedDelay = 100000, initialDelay = 500)
    public void send() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();//
        log.info("[rpc client][sender] {} ", methodName);
        String sendData = UUID.randomUUID().toString();
        String response = (String) template.convertSendAndReceive(exchange.getName(), RabbitMqBeanDefiner.RPC_ROUTING_KEY, sendData);
        log.info("[rpc client][sender] {} Got '" + response + "'", methodName);
    }
}