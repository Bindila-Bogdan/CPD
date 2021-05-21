package com.cpd.debate_0.pub_sub;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Subscriber {
    @RabbitListener(queues = "${rabbitmq.queue.0}")
    public void receiveDataScienceMessage(String message) {
        System.out.println("Received data science message: " + message);
    }

    @RabbitListener(queues = "${rabbitmq.queue.1}")
    public void receiveElectricGuitarsMessage(String message) {
        System.out.println("Received electric guitars message: " + message);
    }

    @RabbitListener(queues = "${rabbitmq.queue.2}")
    public void receiveFootballMessage(String message) {
        System.out.println("Received football message: " + message);
    }
}
