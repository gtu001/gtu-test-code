package gtu.rabbitmq.ex._3_routing;

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

public class RabbitMqReceiver_Routing {

    static {
        LogbackUtil.setRootLevel(ch.qos.logback.classic.Level.OFF);
    }

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMqConst.SERVER_HOST_IP);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(RabbitMqSenderTest_Routing.EXCHANGE, RabbitMqSenderTest_Routing.EXCHANGE_TYPE);
        String queueName = channel.queueDeclare().getQueue();

        for (String severity : RabbitMqSenderTest_Routing.SEVERITY_ARRY) {
            channel.queueBind(queueName, RabbitMqSenderTest_Routing.EXCHANGE, severity);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        });
    }
}
