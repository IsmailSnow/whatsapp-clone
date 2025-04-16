package com.example.whatsappsclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class WhatsappscloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsappscloneApplication.class, args);
    }

}
