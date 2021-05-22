package com.cpd.debate_0;

import com.cpd.debate_0.debaters_order.TransRecvConnection;
import com.cpd.debate_0.topics_pub_sub.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Debate0Application {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Debate0Application.class, args);
        Publisher publisher = context.getBean(Publisher.class);
        publisher.publishDataScienceMessage("merge_0");
        publisher.publishElectricGuitarsMessage("merge_0");

        TransRecvConnection transRecvConnection = new TransRecvConnection();
        transRecvConnection.start();
    }
}
