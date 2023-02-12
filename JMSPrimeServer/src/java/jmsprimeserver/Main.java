/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsprimeserver;

import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

/**
 *
 * @author Punnarat Rattanapawan 62050191
 */
public class Main {

    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/TempQueue")
    private static Queue queue;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            TemporaryQueue tempQueue = session.createTemporaryQueue();
            
            MessageConsumer consumer = session.createConsumer(queue);
            TextListener listener = new TextListener(session);
            consumer.setMessageListener(listener);

            System.out.println("Type q or Q and press enter to exit program)");
            InputStreamReader inputStream = new InputStreamReader(System.in);

            char answer = '\0';
            while (!(answer == 'q' || answer == 'Q'))
                answer = (char) inputStream.read();
            
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}
