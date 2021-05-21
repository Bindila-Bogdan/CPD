package com.cpd.debate_0;

import com.cpd.debate_0.pub_sub.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Debate0Application {
    @Bean
    public CommandLineRunner runOnStartup(Publisher publisher) {
        publisher.publishDataScienceMessage("debate_0");
        return args -> publisher.publishDataScienceMessage("debate_0");
    }

    public static void main(String[] args) {
        SpringApplication.run(Debate0Application.class, args);
    }

}
