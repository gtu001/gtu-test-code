package gtu.jms;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.activemq.ActiveMQConnectionFactory;
import org.activemq.command.ActiveMQDestination;
import org.activemq.command.ActiveMQQueue;

public class LoggerJmsSender {
    
    public static void sendTextMessage(final String text) {
        sendMessage(new CreateMessage() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();
                message.setText(text);
                return message;
            }
        });
    }
    
    public static void sendSerialMessage(final Serializable serial) {
        sendMessage(new CreateMessage() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(serial);
                return message;
            }
        });
    }
    
    private static void sendMessage(CreateMessage createMessage){
        try{
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.127:61616");
            ActiveMQDestination queue = new ActiveMQQueue("testQueue");

            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Message message = createMessage.createMessage(session);

            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(message);
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public interface CreateMessage{
        Message createMessage(Session session) throws JMSException;
    }
}
