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
        ArrayList<String> messageContent = extractFromMessage(message);

        if (messageContent.get(0).equals(debaterName))
            electricGuitarsMessages += "\t\t      me";
        else
            electricGuitarsMessages += messageContent.get(0);

        electricGuitarsMessages += (": " + messageContent.get(1));
        electricGuitarsMessages += "\n";
    }

    @RabbitListener(queues = "${rabbitmq.queue.1}")
    public void receiveFootballMessage(Message message) {
        ArrayList<String> messageContent = extractFromMessage(message);

        if (messageContent.get(0).equals(debaterName))
            footballMessages += "\t\t      me";
        else
            footballMessages += messageContent.get(0);

        footballMessages += (": " + messageContent.get(1));
        footballMessages += "\n";
    }

    private ArrayList<String> extractFromMessage(Message message) {
        String messageBody = (String) rabbitTemplate.getMessageConverter().fromMessage(message);

        String messageHeader = "";
        try {
            messageHeader = (String) message.getMessageProperties().getHeaders().values().toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Header is null.");
        }

        ArrayList<String> messageContent = new ArrayList<>();
        messageContent.add(messageHeader);
        messageContent.add(messageBody);

        return messageContent;
    }

    public String getElectricGuitarsMessages() {
        return electricGuitarsMessages;
    }

    public String getFootballMessages() {
        return footballMessages;
    }
}
