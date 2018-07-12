package com.gtu.example.rabbitmq;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

public class RabbitMqBeanDefiner {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqBeanDefiner.class);

    public static final String TOPIC_QUEUE_01 = "[spring_boot] topic_queue_01";
    public static final String TOPIC_QUEUE_02 = "[spring_boot] topic_queue_02";
    public static final String TOPIC_EXCHANGE = "[spring_boot] topic_exchange";
    public static final String FANOUT_EXCHANGE = "[spring_boot] fanout_exchange";
    public static final String DIRECT_EXCHANGE = "[spring_boot] direct_exchange";
    public static final String RPC_DIRECT_EXCHANGE = "[spring_boot] rpc_direct_exchange";
    public static final String RPC_QUEUE = "rpc_queue";
    public static final String RPC_ROUTING_KEY = "rpc_routing_key";
    public static final String DIRECT_ROUTING_KEY = "direct_routing_key";

    public static void main(String[] args) {
        for (Class<?> clz : RabbitMqBeanDefiner.class.getDeclaredClasses()) {
            for (Method mth : clz.getDeclaredMethods()) {
                if (mth.getReturnType() == Queue.class) {
                    log.info("---->" + mth.getName());
                }
            }
        }
        log.info("done...");
    }

    @Profile(value = { "rabbitmq", "rpc" })
    @Configuration
    public static class RpcConfig {
        @Bean
        public DirectExchange rpcDirectExchange() {
            return new DirectExchange(RPC_DIRECT_EXCHANGE);
        }

        @Bean
        public Queue rpcQueue() {
            return new Queue(RPC_QUEUE);
        }

        @Bean
        public Binding rpcBinding() {
            return BindingBuilder.bind(rpcQueue())//
                    .to(rpcDirectExchange())//
                    .with(RPC_ROUTING_KEY);
        }
    }

    @Profile(value = { "rabbitmq", "topic" })
    @Configuration
    public static class TopicConfig {
        @Bean
        public TopicExchange topicExchange() {
            return new TopicExchange(TOPIC_EXCHANGE);
        }

        @Bean
        public Queue topicQueue01() {
            return new Queue(TOPIC_QUEUE_01);
        }

        @Bean
        public Queue topicQueue02() {
            return new Queue(TOPIC_QUEUE_02);
        }

        @Bean
        public Binding topicBinding01() {
            return BindingBuilder.bind(topicQueue01())//
                    .to(topicExchange())//
                    .with("#");
        }

        @Bean
        public Binding topicBinding02() {
            return BindingBuilder.bind(topicQueue02())//
                    .to(topicExchange())//
                    .with("#");
        }
    }

    @Profile(value = { "rabbitmq", "fanout" })
    @Configuration
    public static class FanoutConfig {
        @Bean
        public Queue fandoutQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public FanoutExchange fanoutExchange() {
            return new FanoutExchange(FANOUT_EXCHANGE);
        }

        @Bean
        public Binding fanoutBinding() {
            return BindingBuilder.bind(fandoutQueue())//
                    .to(fanoutExchange());
        }
    }

    @Profile(value = { "rabbitmq", "direct" })
    @Configuration
    public static class DirectConfig {
        @Bean
        public Queue directQueue01() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue directQueue02() {
            return new AnonymousQueue();
        }

        @Bean
        public DirectExchange directExchange() {
            return new DirectExchange(DIRECT_EXCHANGE);
        }

        @Bean
        public Binding directBinding01(Queue directQueue01) {
            return BindingBuilder.bind(directQueue01)//
                    .to(directExchange())//
                    .with(DIRECT_ROUTING_KEY);
        }

        @Bean
        public Binding directBinding02(Queue directQueue02) {
            return BindingBuilder.bind(directQueue02)//
                    .to(directExchange())//
                    .with(DIRECT_ROUTING_KEY);
        }
    }
}
