package gtu.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.activemq.ActiveMQConnectionFactory;
import org.activemq.command.ActiveMQDestination;
import org.activemq.command.ActiveMQQueue;

public class JmsSenderTest {

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
        Message message = session.createTextMessage("Hello JMS!");

        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.send(message);
        
        connection.close();
    }
}
