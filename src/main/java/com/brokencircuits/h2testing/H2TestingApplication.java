package com.brokencircuits.h2testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class H2TestingApplication {

  public static void main(String[] args) {
    SpringApplication.run(H2TestingApplication.class, args);
  }

}
