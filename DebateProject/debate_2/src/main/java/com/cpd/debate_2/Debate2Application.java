package com.cpd.debate_2;

import com.cpd.debate_2.pub_sub.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Debate2Application {
	@Bean
	public CommandLineRunner runOnStartup(Publisher publisher) {
		publisher.publishFootballMessage("debate_2");
		return args -> publisher.publishFootballMessage("debate_2");
	}

    public static void main(String[] args) {
        SpringApplication.run(Debate2Application.class, args);
    }

}
