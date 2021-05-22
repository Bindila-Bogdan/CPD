package com.cpd.debate_2;

import com.cpd.debate_2.debaters_order.TransRecvConnection;
import com.cpd.debate_2.pub_sub.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Debate2Application {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Debate2Application.class, args);
        Publisher publisher = context.getBean(Publisher.class);
        publisher.publishElectricGuitarsMessage("merge_2");
        publisher.publishFootballMessage("merge_2");

        TransRecvConnection transRecvConnection = new TransRecvConnection();
        transRecvConnection.start();
    }

}
