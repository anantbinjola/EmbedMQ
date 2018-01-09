package activeMQEmbed;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class EmbedConsumer implements Runnable, ExceptionListener {

    public void run() {
        try {

            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
            
            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            
            connection.start();

            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST.FOO");

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);
            
            List<Message> list = new ArrayList<Message>();
            
            Message message = consumer.receive(1000);
            
            while(message!=null){
            	list.add(message);
            	message = consumer.receive(1000);
            }
            
            for(Message msg : list){
            	if (msg instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) msg;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + msg);
                }	
            }
                  

            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }

}
