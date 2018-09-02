package gtu.rabbitmq.ex._3_routing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import gtu.console.SystemInUtil;
import gtu.log.LogbackUtil;
import gtu.rabbitmq.ex.common.RabbitMqConst;

public class RabbitMqSenderTest_Routing {

    static {
        LogbackUtil.setRootLevel(ch.qos.logback.classic.Level.OFF);
    }

    static final String EXCHANGE = "logs_direct";
    static final String EXCHANGE_TYPE = "direct";

    static final String[] SEVERITY_ARRY = new String[] { "debug", "info", "warn", "severity", "fatal" };

    public static void main(String[] args) throws IOException, TimeoutException {
        String severity = SystemInUtil.readLine("請輸入發送層級:");
        if (StringUtils.isBlank(severity) || !ArrayUtils.contains(SEVERITY_ARRY, severity)) {
            System.out.println("請輸入正確  severity !");
            return;
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMqConst.SERVER_HOST_IP);
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 定義此 sender 送出 message 給 exchange logs
        channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE);

        // 若沒有 queue 綁在 exchange 下 送出 message 會lost
        String message = "test message here!! : " + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/DD HH:mm:ss.SSSSS");

        String routingKey = severity;

        channel.basicPublish(EXCHANGE, routingKey, null, message.getBytes(RabbitMqConst.MSG_ENCODE));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();

        System.out.println("done...");
    }
}
