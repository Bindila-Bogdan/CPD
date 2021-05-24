package com.cpd.debate_2;

import com.cpd.debate_2.presenter.Presenter;
import com.cpd.debate_2.topics_pub_sub.Publisher;
import com.cpd.debate_2.topics_pub_sub.Subscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Debate2Application {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Debate2Application.class, args);

        System.setProperty("java.awt.headless", "false");

        Publisher publisher = context.getBean(Publisher.class);
        Subscriber subscriber = context.getBean(Subscriber.class);

        Presenter presenter = new Presenter(subscriber, publisher,
                "Electric guitars topic - debater 2",
                "Football topic - debater 2");
        presenter.showFrames();
    }

}
