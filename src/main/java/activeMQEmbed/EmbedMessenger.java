package activeMQEmbed;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class EmbedMessenger {

	public static Connection connection = null;
	public static Session session = null;

	public static void main(String[] args) {
		EmbedExample.thread(new EmbedProducer(), false);
		init();
		for (int i = 0; i < 10; i++) {
			sendMessage("testmessage" + i, "TEST.FOO");
		}
		cleanup();
	}

	public static void init() {
		try {
			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

			// Create a Connection
			connection = connectionFactory.createConnection();
			connection.start();

			// Create a Session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}

	}

	public static void sendMessage(String message, String destination) {
		try {
			// Create the destination (Topic or Queue)
			Destination dst = session.createQueue(destination);

			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(dst);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			TextMessage msg = session.createTextMessage(message);
			System.out.println("Sent message: " + msg.getText() + " : " + Thread.currentThread().getName());
			producer.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void cleanup() {
		// Clean up
		try {
			session.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
