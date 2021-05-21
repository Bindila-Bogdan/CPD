package com.cpd.debate_2.pub_sub;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Subscriber {
    @RabbitListener(queues = "${rabbitmq.queue.0}")
    public void receiveElectricGuitarsMessage(String message) {
        System.out.println("Received electric guitars message: " + message);
    }

    @RabbitListener(queues = "${rabbitmq.queue.1}")
    public void receiveFootballMessage(String message) {
        System.out.println("Received football electric guitars message: " + message);
    }
}
