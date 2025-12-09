package com.codeit.sb06deokhugamteam2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
public class Sb06DeokhugamTeam2Application {

    public static void main(String[] args) {
        SpringApplication.run(Sb06DeokhugamTeam2Application.class, args);
    }

}
