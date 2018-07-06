package gtu.rabbitmq.ex._2_publish_subscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.time.DateFormatUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import gtu.log.LogbackUtil;
import gtu.rabbitmq.ex.common.RabbitMqConst;

public class RabbitMqSenderTest_PublishSubscribe {

    static {
        LogbackUtil.setRootLevel(ch.qos.logback.classic.Level.OFF);
    }

    static final String EXCHANGE = "logs";
    static final String EXCHANGE_TYPE = "fanout";// fanout 廣播所有收到的 message 給所有
                                                 // queue

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMqConst.SERVER_HOST_IP);
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 定義此 sender 送出 message 給 exchange logs
        channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE);

        // 若沒有 queue 綁在 exchange 下 送出 message 會lost
        String message = "test message here!! : " + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/DD HH:mm:ss.SSSSS");

        channel.basicPublish(EXCHANGE, "", null, message.getBytes(RabbitMqConst.MSG_ENCODE));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();

        System.out.println("done...");
    }
}
