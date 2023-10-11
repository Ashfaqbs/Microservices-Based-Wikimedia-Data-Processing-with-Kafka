package com.main.springboot.cosumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootkafkaConsumerApplication  {
//Creating a main entry for this package
//so this project which we created is a maven project so we create this class and annotate with @SpringBootApplication
//then we will have main entry for this package

    public static void main(String[] args) {
        SpringApplication.run(SpringbootkafkaConsumerApplication.class);
    }

}
