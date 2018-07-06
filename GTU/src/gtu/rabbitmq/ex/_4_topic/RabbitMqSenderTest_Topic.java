package gtu.rabbitmq.ex._4_topic;

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

public class RabbitMqSenderTest_Topic {

    static {
        LogbackUtil.setRootLevel(ch.qos.logback.classic.Level.OFF);
    }

    static final String EXCHANGE = "logs_topic";
    static final String EXCHANGE_TYPE = "topic";

    public static void main(String[] args) throws IOException, TimeoutException {

        // * (star) 替代一個word (以.隔開算一個字)
        // # (hash) 替代0-n個word (以.隔開算一個字)

        // 定義如下 : <speed>.<colour>.<species>
        // receiver 1 : *.orange.* --> 所有橘色動物
        // receiver 2 : *.*.rabbit --> 所有兔子
        // receiver 3 : lazy.# --> 所有覽的動物

        // routin key 1 : quick.orange.rabbit        --> receiver get : (1,2)
        // routin key 2 : lazy.orange.elephant       --> receiver get : (3,1)
        // routin key 3 : quick.orange.fox           --> receiver get : (1)
        // routin key 4 : lazy.brown.fox             --> receiver get : (3)
        // routin key 5 : lazy.pink.rabbit           --> receiver get : (3,2)
        // routin key 6 : quick.brown.fox            --> receiver get : (list)
        // routin key 7 : quick.orange.male.rabbit   --> receiver get : (lost)
        // routin key 8 : lazy.orange.male.rabbit    --> receiver get : (3)
        
//[x] Received [bindKey : *.*.rabbit ] 'quick.orange.rabbit':'test message here!! : 2018/07/187 14:29:42.00782'
//[x] Received [bindKey : *.orange.* ] 'quick.orange.rabbit':'test message here!! : 2018/07/187 14:29:42.00782'
//[x] Received [bindKey : *.orange.* ] 'lazy.orange.elephant':'test message here!! : 2018/07/187 14:29:42.00782'
//[x] Received [bindKey : *.orange.* ] 'quick.orange.fox':'test message here!! : 2018/07/187 14:29:42.00782'
//[x] Received [bindKey : lazy.# ] 'lazy.orange.elephant':'test message here!! : 2018/07/187 14:29:42.00782'
//[x] Received [bindKey : lazy.# ] 'lazy.brown.fox':'test message here!! : 2018/07/187 14:29:42.00782'
//[x] Received [bindKey : lazy.# ] 'lazy.pink.rabbit':'test message here!! : 2018/07/187 14:29:42.00782'
//[x] Received [bindKey : lazy.# ] 'lazy.orange.male.rabbit':'test message here!! : 2018/07/187 14:29:42.00782'
//[x] Received [bindKey : *.*.rabbit ] 'lazy.pink.rabbit':'test message here!! : 2018/07/187 14:29:42.00782'
        
        String[] routinKeyArry = new String[]{//
                "quick.orange.rabbit",//
                "lazy.orange.elephant",//
                "quick.orange.fox",//
                "lazy.brown.fox",//
                "lazy.pink.rabbit",//
                "quick.brown.fox",//
                "quick.orange.male.rabbit",//
                "lazy.orange.male.rabbit",//
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMqConst.SERVER_HOST_IP);
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 定義此 sender 送出 message 給 exchange logs
        channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE);

        // 若沒有 queue 綁在 exchange 下 送出 message 會lost
        String message = "test message here!! : " + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/DD HH:mm:ss.SSSSS");
        for(String routingKey : routinKeyArry){
            channel.basicPublish(EXCHANGE, routingKey, null, message.getBytes(RabbitMqConst.MSG_ENCODE));
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();

        System.out.println("done...");
    }
}
