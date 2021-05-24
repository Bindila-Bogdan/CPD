package com.cpd.debate_1;

import com.cpd.debate_1.presenter.Presenter;
import com.cpd.debate_1.topics_pub_sub.Publisher;
import com.cpd.debate_1.topics_pub_sub.Subscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Debate1Application {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Debate1Application.class, args);

        System.setProperty("java.awt.headless", "false");

        Publisher publisher = context.getBean(Publisher.class);
        Subscriber subscriber = context.getBean(Subscriber.class);

        Presenter presenter = new Presenter(subscriber, publisher,
                "Data science topic - debater 1",
                "Electric guitars topic - debater 1");
        presenter.showFrames();
    }
}
