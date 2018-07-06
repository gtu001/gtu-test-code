package gtu.rabbitmq.ex._5_rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import gtu.rabbitmq.ex.common.RabbitMqConst;

public class RabbitMqServer_Rpc {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMqConst.SERVER_HOST_IP);

        Connection connection = null;
        try {
            connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            // 要進行多server 處理, 為了要分布相同load多台server , 必須定義 prefetchCount 設定 在
            // channel.basicQos
            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()//
                            .correlationId(properties.getCorrelationId())//
                            .build();

                    String response = "";

                    try {
                        String message = new String(body, "UTF-8");
                        int n = Integer.parseInt(message);

                        System.out.println(" [.] fib(" + message + ")");
                        response += fib(n);
                    } catch (RuntimeException e) {
                        System.out.println(" [.] " + e.toString());
                    } finally {
                        channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));

                        channel.basicAck(envelope.getDeliveryTag(), false);

                        // RabbitMq consumer worker thread notifies the RPC
                        // server owner thread
                        synchronized (this) {
                            this.notify();
                        }
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (IOException _ignore) {
                }
        }
    }

    private static String fib(int n) {
        return "Body Len : " + n;
    }
}
