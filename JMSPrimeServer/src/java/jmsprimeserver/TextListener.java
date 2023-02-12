/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsprimeserver;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author Punnarat Rattanapawan 62050191
 */
public class TextListener implements MessageListener {
    
    private Session session;
//    private MessageProducer producer;

    public TextListener(Session session) {

        this.session = session;

//        try {
//            producer = session.createProducer(null);
//        } catch (JMSException e) {
//            Logger.getLogger(TextListener.class.getName()).log(Level.SEVERE, null, e);
//        }
    }

    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;

        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText());
            } else {
                System.err.println("Message is not a TextMessage");
            }
            
            Destination replyTo = message.getJMSReplyTo();
            MessageProducer producer = session.createProducer(replyTo);

            String[] numbers = msg.getText().split(",");
            int firstNumber = Integer.parseInt(numbers[0]);
            int secondNumber = Integer.parseInt(numbers[1]);
            int totalPrimes = PrimeNumberCounter.countPrimes(firstNumber, secondNumber);

            TextMessage response = session.createTextMessage(Integer.toString(totalPrimes));
            System.out.println("Sending message: The number of primes between " + firstNumber + " and " + secondNumber + " is " + totalPrimes);
            producer.send(response);
            
            producer.close();
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }
    }
}
