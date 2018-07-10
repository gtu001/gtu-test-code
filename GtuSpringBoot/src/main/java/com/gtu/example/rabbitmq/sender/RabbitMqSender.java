package com.gtu.example.rabbitmq.sender;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gtu.example.entity.RabbitMqCustomMessage;
import com.gtu.example.rabbitmq.RabbitMqBeanDefiner;

@Profile(value = { "rabbitmq" })
@Service
public class RabbitMqSender {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqSender(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 3000L)//fixedRate
    public void topicSendMessage() {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[sender] {} ...", method);
        
        String exchange = RabbitMqBeanDefiner.TOPIC_EXCHANGE;
        String routingKey = "test routing key";
        
        final RabbitMqCustomMessage message = //
                new RabbitMqCustomMessage("topic message there!", new Random().nextInt(50), false);

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
    
    @Scheduled(fixedDelay = 3000L)//fixedRate
    public void fanoutSendMessage() {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[sender] {} ...", method);
        
        String exchange = RabbitMqBeanDefiner.FANOUT_EXCHANGE;
        String routingKey = "";
        String message = "fanout message here!!";

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
    
    @Scheduled(fixedDelay = 3000L)//fixedRate
    public void directSendMessage() {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[sender] {} ...", method);
        
        String exchange = RabbitMqBeanDefiner.DIRECT_EXCHANGE;
        String routingKey = RabbitMqBeanDefiner.DIRECT_ROUTING_KEY;
        String message = "direct message here!!";

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}