package com.gtu.example.rabbitmq.receiver;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.gtu.example.entity.RabbitMqCustomMessage;
import com.gtu.example.rabbitmq.RabbitMqApplication;
import com.gtu.example.rabbitmq.RabbitMqBeanDefiner;

@Service
public class RabbitMqListener {

    @PostConstruct
    public void initOk() {
    }

    private static final Logger log = LoggerFactory.getLogger(RabbitMqListener.class);

    @RabbitListener(queues = RabbitMqBeanDefiner.POJO_QUEUE_001)
    public void receiveMessage(final Message message) {
        log.info("Received POJO_QUEUE_001 as generic: {}", message.toString());
    }

    @RabbitListener(queues = RabbitMqBeanDefiner.POJO_QUEUE_001)
    public void receiveMessage(final RabbitMqCustomMessage customMessage) {
        log.info("Received POJO_QUEUE_001 as specific class: {}", customMessage.toString());
    }

    @RabbitListener(queues = RabbitMqBeanDefiner.SPRING_QUEUE_001)
    public void receiveMessage2(final Message message) {
        log.info("Received SPRING_QUEUE_001 as generic: {}", message.toString());
    }

    @RabbitListener(queues = RabbitMqBeanDefiner.SPRING_QUEUE_001)
    public void receiveMessage2(final RabbitMqCustomMessage customMessage) {
        log.info("Received SPRING_QUEUE_001 as specific class: {}", customMessage.toString());
    }
}