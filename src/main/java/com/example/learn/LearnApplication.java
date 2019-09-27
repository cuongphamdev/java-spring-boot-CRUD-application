package com.example.learn;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LearnApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(LearnApplication.class, args);
  }

  @Override
  public void run(String... args) {
    //
  }
}
