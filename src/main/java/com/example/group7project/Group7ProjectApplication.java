package com.example.group7project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Group7ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(Group7ProjectApplication.class, args);
    }

}
