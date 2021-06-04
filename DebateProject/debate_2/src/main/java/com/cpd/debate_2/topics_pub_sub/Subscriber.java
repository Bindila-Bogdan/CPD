package com.cpd.debate_2.topics_pub_sub;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Subscriber {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${debater.name}")
    private String debaterName;

    private String electricGuitarsMessages = "*Welcome to electric guitars debate*\n\n";
    private String footballMessages = "*Welcome to football debate*\n\n";

    @RabbitListener(queues = "${rabbitmq.queue.0}")
    public void receiveElectricGuitarsMessage(Message message) {
        electricGuitarsMessages = extractFromMessage(message, electricGuitarsMessages);

    }

    @RabbitListener(queues = "${rabbitmq.queue.1}")
    public void receiveFootballMessage(Message message) {
        footballMessages = extractFromMessage(message, footballMessages);
    }

    private String extractFromMessage(Message message, String topicMessages) {
        String messageBody = (String) rabbitTemplate.getMessageConverter().fromMessage(message);

        String messageHeader = "";
        try {
            messageHeader = (String) message.getMessageProperties().getHeaders().values().toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Header is null.");
        }

        if (messageHeader.equals(debaterName))
            topicMessages += "\t\t      me";
        else
            topicMessages += messageHeader;

        topicMessages += (": " + messageBody);
        topicMessages += "\n";

        return topicMessages;
    }

    public String getElectricGuitarsMessages() {
        return electricGuitarsMessages;
    }

    public String getFootballMessages() {
        return footballMessages;
    }
}
