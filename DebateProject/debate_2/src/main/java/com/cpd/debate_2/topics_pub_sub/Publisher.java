package com.cpd.debate_2.topics_pub_sub;

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

    public void publishMessageOnTopic(String message, String topicName) {
        amqpTemplate.convertAndSend(exchange, topicName, message, m -> {
            m.getMessageProperties().getHeaders().put("sender", debaterName);
            return m;
        });
    }
}
