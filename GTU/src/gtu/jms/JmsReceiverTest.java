package gtu.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.activemq.ActiveMQConnectionFactory;
import org.activemq.command.ActiveMQDestination;
import org.activemq.command.ActiveMQQueue;

public class JmsReceiverTest {

    //http://localhost:8161
    public static void main(String[] args) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
////        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
//        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.127:61616");
        ActiveMQDestination queue = new ActiveMQQueue("testQueue");
//        queue = new ActiveMQTopic("testTopic");

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        MessageConsumer consumer = session.createConsumer(queue);
//        consumer.setMessageListener(new MessageListener() {
//            @Override
//            public void onMessage(Message arg0) {
//                try {
//                    TextMessage message = (TextMessage)arg0;
//                    System.out.println("======>" + message.getText());
//                    PrintStream out = new PrintStream(new FileOutputStream(new File(FileUtil.DESKTOP_PATH, "log.txt")), true);
//                    out.println("======>" + message.getText());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        
        Message message = consumer.receive();
        System.out.println("====>" + ((TextMessage)message).getText());
        connection.close();
    }
}
