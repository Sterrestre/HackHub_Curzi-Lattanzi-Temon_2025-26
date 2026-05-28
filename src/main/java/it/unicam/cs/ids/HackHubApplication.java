package it.unicam.cs.ids;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HackHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(HackHubApplication.class, args);
    }
}

