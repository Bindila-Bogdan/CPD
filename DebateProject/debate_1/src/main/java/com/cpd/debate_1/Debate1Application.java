package com.cpd.debate_1;

import com.cpd.debate_1.pub_sub.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Debate1Application {
    @Bean
    public CommandLineRunner runOnStartup(Publisher publisher) {
        publisher.publishElectricGuitarsMessage("debate_1");
        return args -> publisher.publishElectricGuitarsMessage("debate_1");
    }

    public static void main(String[] args) {
        SpringApplication.run(Debate1Application.class, args);
    }
}
