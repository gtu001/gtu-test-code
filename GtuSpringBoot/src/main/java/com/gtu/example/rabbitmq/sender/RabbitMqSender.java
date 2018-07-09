package com.gtu.example.rabbitmq.sender;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gtu.example.entity.RabbitMqCustomMessage;
import com.gtu.example.rabbitmq.RabbitMqBeanDefiner;

@Service
public class RabbitMqSender {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqSender(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 3000L)//fixedRate
    public void sendMessage() {
        log.info("Sending message...");
        
        String exchange = RabbitMqBeanDefiner.EXCHANGE_NAME;
        String routingKey = "test routing key";
        
        final RabbitMqCustomMessage message = //
                new RabbitMqCustomMessage("Hello there!", new Random().nextInt(50), false);

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}