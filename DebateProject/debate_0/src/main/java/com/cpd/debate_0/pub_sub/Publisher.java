package com.cpd.debate_0.pub_sub;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    public void publishDataScienceMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "data_science", message);
        System.out.println("Sent data science message: " + message);
    }

    public void publishElectricGuitarsMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "electric_guitars", message);
        System.out.println("Sent electric guitars message: " + message);
    }

    public void publishFootballMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "football", message);
        System.out.println("Sent football message: " + message);
    }
}
