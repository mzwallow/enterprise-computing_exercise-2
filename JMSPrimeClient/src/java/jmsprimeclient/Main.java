/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsprimeclient;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

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
    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            MessageProducer producer = session.createProducer(queue);
            
            TemporaryQueue tempQueue = session.createTemporaryQueue();
            
            MessageConsumer consumer = session.createConsumer(tempQueue);
            TextListener listener = new TextListener();
            consumer.setMessageListener(listener);
            
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(tempQueue);
            
            Scanner input = new Scanner(System.in);
            
            String receivedMsg;
            while (true) {
                System.out.print("Enter two numbers. Use ',' to seperate each number. To end the program press enter: ");
                receivedMsg = input.nextLine();
                
                if (receivedMsg.isEmpty())
                    break;
                
                message.setText(receivedMsg);
                
                System.out.println("Sending message: " + receivedMsg);
                producer.send(message);
            }
            
            consumer.close();
            producer.close();
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
