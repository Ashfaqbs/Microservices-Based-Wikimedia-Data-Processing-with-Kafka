package com.main.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootkafkaProducerApplication  implements CommandLineRunner {
//Creating a main entry for this package
//so this project which we created is a maven project so we create this class and annotate with @SpringBootApplication
//then we will have main entry for this package
    public static void main(String[] args) {
        SpringApplication.run(SpringbootkafkaProducerApplication.class);
    }

    @Autowired
    private WikimediaChangesProducer wikimediaChangesProducer;


    @Override
    public void run(String... args) throws Exception {
        wikimediaChangesProducer.sendMessage();
    }
}
