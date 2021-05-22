package com.cpd.debate_1;

import com.cpd.debate_1.debaters_order.TransRecvConnection;
import com.cpd.debate_1.pub_sub.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Debate1Application {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Debate1Application.class, args);
        Publisher publisher = context.getBean(Publisher.class);
        publisher.publishDataScienceMessage("merge_1");
        publisher.publishElectricGuitarsMessage("merge_1");

        TransRecvConnection transRecvConnection = new TransRecvConnection();
        transRecvConnection.start();
    }
}
