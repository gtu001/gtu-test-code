package com.gtu.example.rabbitmq.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Profile(value = { "rabbitmq" })
@Service
public class RabbitMqPpcClient {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqPpcClient.class);

    @Autowired
    private RabbitTemplate template;

    @Autowired
    @Qualifier("rpcDirectExchange")
    private DirectExchange exchange;

    int start = 0;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[rpc client][sender] {} Requesting fib(" + start + ")", methodName);
        Integer response = (Integer) template.convertSendAndReceive(exchange.getName(), "XXXXXXXXXXXXXXXXXXX", start++);
        log.info("[rpc client][sender] {} Got '" + response + "'", methodName);
    }
}