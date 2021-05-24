package com.cpd.debate_0.topics_pub_sub;

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

    public void publishDataScienceMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "data_science", message, m -> {
            m.getMessageProperties().getHeaders().put("sender", debaterName);
            return m;
        });
    }

    public void publishElectricGuitarsMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "electric_guitars", message, m -> {
            m.getMessageProperties().getHeaders().put("sender", debaterName);
            return m;
        });
    }

    public void publishFootballMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "football", message, m -> {
            m.getMessageProperties().getHeaders().put("sender", debaterName);
            return m;
        });
    }
}
