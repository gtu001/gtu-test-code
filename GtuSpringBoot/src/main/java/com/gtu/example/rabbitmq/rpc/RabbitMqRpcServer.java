package com.gtu.example.rabbitmq.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile(value = { "rabbitmq" })
@Service
public class RabbitMqRpcServer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqRpcServer.class);

    @RabbitListener(queues = "#{rpcQueue.name}")
    public int fibonacci(int n) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        log.info("[rpc server][receiver] {} Received request for " + n, methodName);
        int result = fib(n);
        log.info("[rpc server][receiver] {} Returned " + result, methodName);
        return result;
    }

    public int fib(int n) {
        return n == 0 ? 0 : n == 1 ? 1 : (fib(n - 1) + fib(n - 2));
    }
}