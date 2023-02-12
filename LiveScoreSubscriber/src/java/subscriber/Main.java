/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subscriber;

import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

/**
 *
 * @author Punnarat Rattanapawan 62050191
 */
public class Main {
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Connection connection = null;
        
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) topic;
            MessageConsumer consumer = session.createConsumer(destination);
            TextListener listener = new TextListener();
        
            consumer.setMessageListener(listener);
            connection.start();
            
            System.out.println("To end program, type q or Q then press enter");
            InputStreamReader inputStream = new InputStreamReader(System.in);
            
            char answer = '\0';
            while (!(answer == 'q' || answer == 'Q'))
                answer = (char) inputStream.read();
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {}
            }
        }
    }
    
}
