package com.cpd.debate_1.topics_pub_sub;

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

    private String dataScienceMessages = "*Welcome to data science debate*\n\n";
    private String electricGuitarsMessages = "*Welcome to electric guitars debate*\n\n";

    @RabbitListener(queues = "${rabbitmq.queue.0}")
    public void receiveDataScienceMessage(Message message) {
        dataScienceMessages = extractFromMessage(message, dataScienceMessages);
    }

    @RabbitListener(queues = "${rabbitmq.queue.1}")
    public void receiveElectricGuitarsMessage(Message message) {
        electricGuitarsMessages = extractFromMessage(message, electricGuitarsMessages);
    }

    private String extractFromMessage(Message message, String topicMessages) {
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

        if (messageContent.get(0).equals(debaterName))
            dataScienceMessages += "\t\t      me";
        else
            dataScienceMessages += messageContent.get(0);

        dataScienceMessages += (": " + messageContent.get(1));
        dataScienceMessages += "\n";

        return dataScienceMessages;
    }

    public String getDataScienceMessages() {
        return dataScienceMessages;
    }

    public String getElectricGuitarsMessages() {
        return electricGuitarsMessages;
    }
}
