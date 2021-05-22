package com.cpd.debate_1.pub_sub;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Subscriber {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${debater.name}")
    private String debaterName;

    @RabbitListener(queues = "${rabbitmq.queue.0}")
    public void receiveDataScienceMessage(Message message) {
        String messageBody = (String) rabbitTemplate.getMessageConverter().fromMessage(message);
        String messageHeader = "";
        try {
            messageHeader = (String) message.getMessageProperties().getHeaders().values().toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Header is null.");
        }

        if (!messageHeader.equals(debaterName))
            System.out.println("Received data science message: " + messageBody);
    }

    @RabbitListener(queues = "${rabbitmq.queue.1}")
    public void receiveElectricGuitarsMessage(Message message) {
        String messageBody = (String) rabbitTemplate.getMessageConverter().fromMessage(message);
        String messageHeader = "";
        try {
            messageHeader = (String) message.getMessageProperties().getHeaders().values().toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Header is null.");
        }

        if (!messageHeader.equals(debaterName))
            System.out.println("Received electric guitars message: " + messageBody);
    }
}
