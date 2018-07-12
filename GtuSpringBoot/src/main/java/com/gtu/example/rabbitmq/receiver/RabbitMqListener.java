package com.gtu.example.rabbitmq.receiver;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.gtu.example.rabbitmq.dto.RabbitMqCustomMessage;

@Profile(value = { "rabbitmq" })
@Service
public class RabbitMqListener {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqListener.class);

    @PostConstruct
    public void initOk() {
    }

    @RabbitListener(queues = "#{topicQueue01.name}")
    public void topicQueue01Listener(final Message message) {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[receive] {} : {}", method, message);
    }

    @RabbitListener(queues = "#{topicQueue01.name}")
    public void topicQueue01Listener(final RabbitMqCustomMessage customMessage) {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[receive] {} : {}", method, customMessage);
    }

    @RabbitListener(queues = "#{topicQueue02.name}")
    public void topicQueue02Listener(final Message message) {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[receive] {} : {}", method, message);
    }

    @RabbitListener(queues = "#{topicQueue02.name}")
    public void topicQueue02Listener(final RabbitMqCustomMessage customMessage) {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[receive] {} : {}", method, customMessage);
    }

    @RabbitListener(queues = "#{directQueue01.name}")
    public void directQueue01Listener(final String message) {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[receive] {} : {}", method, message);
    }

    @RabbitListener(queues = "#{directQueue02.name}")
    public void directQueue02Listener(final String message) {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[receive] {} : {}", method, message);
    }
    
    @RabbitListener(queues = "#{fandoutQueue.name}")
    public void fandoutQueueListener(final String message) {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[receive] {} : {}", method, message);
    }
}