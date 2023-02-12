/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livescore;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
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
    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) topic;
            
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
        
            Scanner input = new Scanner(System.in);
            
            String score;
            while (true) {
                System.out.print("Enter live score (q or Q to exit program): ");
                score = input.nextLine();
                
                if (score.equals("q") || score.equals("Q")) 
                    break;
                
                message.setText(score);
                producer.send(message);
            }
            
            producer.close();
            session.close();
            connection.close();
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
