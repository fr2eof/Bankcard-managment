package com.pet.card_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class CardSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardSystemApplication.class, args);
    }

}
