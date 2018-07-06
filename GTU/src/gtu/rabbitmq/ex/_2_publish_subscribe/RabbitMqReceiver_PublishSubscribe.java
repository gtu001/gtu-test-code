package gtu.rabbitmq.ex._2_publish_subscribe;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import gtu.log.LogbackUtil;
import gtu.rabbitmq.ex.common.RabbitMqConst;

public class RabbitMqReceiver_PublishSubscribe {
    static {
        LogbackUtil.setRootLevel(ch.qos.logback.classic.Level.OFF);
    }

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMqConst.SERVER_HOST_IP);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(RabbitMqSenderTest_PublishSubscribe.EXCHANGE, RabbitMqSenderTest_PublishSubscribe.EXCHANGE_TYPE);

        // 將此 receiver 的 queue 綁在 exchange 底下 (訂閱)
        String routingKey = "";
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, RabbitMqSenderTest_PublishSubscribe.EXCHANGE, routingKey);
        
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        });
    }
}
