package com.cpd.debate_2.pub_sub;

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

    @Value("${debater.name}")
    private String debaterName;

    public void publishElectricGuitarsMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "electric_guitars", message, m -> {
            m.getMessageProperties().getHeaders().put("sender", debaterName);
            return m;
        });
        System.out.println("Sent electric guitars message: " + message);
    }

    public void publishFootballMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "football", message, m -> {
            m.getMessageProperties().getHeaders().put("sender", debaterName);
            return m;
        });
        System.out.println("Sent football message: " + message);
    }
}
