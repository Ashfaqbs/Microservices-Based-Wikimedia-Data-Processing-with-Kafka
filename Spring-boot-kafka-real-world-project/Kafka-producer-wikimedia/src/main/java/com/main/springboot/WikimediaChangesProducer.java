package com.main.springboot;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class WikimediaChangesProducer {

    private static final Logger logger = LoggerFactory.getLogger(WikimediaChangesProducer.class);

private KafkaTemplate<String , String> kafkaTemplate;

    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {
        String topic="wikimedia_recentchange_topic";
//to read real time stream data from wikimedia , we will use event source
        EventHandler eventHandler = new WikimediaChangeHandler(kafkaTemplate,topic);

//Defining event source URL
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        //passing the url to EventSource which will connect to source and will read event data
//created a event source builder
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        //lets create eventsource object from this builder
        EventSource eventSource= builder.build();
        //since this creates thread we need to start
        eventSource.start();

        TimeUnit.MINUTES.sleep(10);



    }
}
