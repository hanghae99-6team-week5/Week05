package com.example.team6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class team6Application {

  public static void main(String[] args) {
    SpringApplication.run(team6Application.class, args);
  }

}
