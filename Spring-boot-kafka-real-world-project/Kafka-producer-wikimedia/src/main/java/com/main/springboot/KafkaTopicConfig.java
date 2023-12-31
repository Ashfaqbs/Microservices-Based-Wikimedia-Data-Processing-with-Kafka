package com.main.springboot;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

@Bean
    public NewTopic kafkaTopicBuilder()
{
    return TopicBuilder.
            name("wikimedia_recentchange_topic")
//            .partitions(10)
//            .replicas(2)
            .build();

}

}
