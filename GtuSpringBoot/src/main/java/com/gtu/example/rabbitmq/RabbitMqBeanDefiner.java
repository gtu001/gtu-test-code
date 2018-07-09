package com.gtu.example.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

public class RabbitMqBeanDefiner {
    
    public static final String POJO_QUEUE_001 = "DOJO_QUEUE_001";
    public static final String SPRING_QUEUE_001 = "SPRING_QUEUE_001";
    public static final String EXCHANGE_NAME = "EXCHANGE_NAME";
    public static final String ROUTING_KEY = "#";

    /**
     * The following is a complete declaration of an exchange, a queue and a
     * exchange-queue binding
     */
    @Bean
    public TopicExchange appExchange() {
        // return new TopicExchange(EXCHANGE_NAME);
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    /**
     * This queue will be declared. This means it will be created if it does not
     * exist. Once declared, you can do something like the following:
     * 
     * @RabbitListener(queues = "#{@myDurableQueue}")
     * @Transactional public void handleMyDurableQueueMessage(CustomDurableDto
     *                myMessage) { // Anything you want! This can also return a
     *                non-void which will queue it back in to the queue attached
     *                to @RabbitListener }
     */
    @Bean
    public Queue appQueueGeneric() {
        // This queue has the following properties:
        // name: my_durable
        // durable: true
        // exclusive: false
        // auto_delete: false
        // return new Queue(DOJO_EXCHANGE_001);
        return new Queue(POJO_QUEUE_001, true, false, false);
    }

    @Bean
    public Queue appQueueSpecific() {
        // return new Queue(SPRING_MESSAGE_EXCHANGE);
        return new Queue(SPRING_QUEUE_001, true, false, false);
    }

    @Bean
    public Binding declareBindingGeneric() {
        return BindingBuilder.bind(appQueueGeneric())//
                .to(appExchange())//
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding declareBindingSpecific() {
        return BindingBuilder.bind(appQueueGeneric())//
                .to(appExchange())//
                .with(ROUTING_KEY);
    }
}
