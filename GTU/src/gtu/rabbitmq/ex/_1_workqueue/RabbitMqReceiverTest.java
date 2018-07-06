package gtu.rabbitmq.ex._1_workqueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import gtu.log.LogbackUtil;
import gtu.rabbitmq.ex.common.RabbitMqConst;

public class RabbitMqReceiverTest {
    
    static {
        LogbackUtil.setRootLevel(ch.qos.logback.classic.Level.OFF);
    }
    
    public static void main(String[] argv) throws java.io.IOException, java.lang.InterruptedException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMqConst.SERVER_HOST_IP);
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(RabbitMqSenderTest.QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicConsume(RabbitMqSenderTest.QUEUE_NAME, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        });
    }
}