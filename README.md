# Real-Time Wikimedia Event Processor with Microservices, Kafka, and Spring Boot

## Description
This project is like a super-fast news aggregator, but instead of news, it collects and processes real-time data from Wikimedia, such as Wikipedia. Imagine Wikimedia as a live TV show, and we have a system that watches every change, big or small, and stores it in a big diary. We have two parts to this system:

- **The "Reporter" (Producer)**: It watches Wikimedia and writes down every change in a diary.
- **The "Diary Keeper" (Consumer)**: It reads the diary and stores the changes in a big filing cabinet.

## Detailed Description
This project is built with Spring Boot and uses microservices architecture for processing real-time data from Wikimedia and saving it in a Kafka topic and a database. Let's break down the key components:

- **Microservices**: Think of microservices as small, specialized teams working together. In this project, we have "teams" that do specific tasks: one team collects data, and another team stores it.

- **Kafka**: Kafka is like a message-passing system. It's the conveyor belt that carries the data from the data collector to the data storer.

- **Producer (WikimediaChangesProducer)**: This part watches Wikimedia in real time. It's like a reporter with a notepad, recording every change as it happens. The reporter sends this information onto the conveyor belt (Kafka).

- **Consumer (KafkaDatabaseConsumer)**: This part waits for the messages from the conveyor belt (Kafka). It's like a librarian who takes the notes from the reporter, reads them, and files them in a giant cabinet (database) for future reference.

- **Database**: The big cabinet where all the changes are neatly filed. It's like an organized library of all the Wikimedia changes.

## Features
- **Real-Time Data Collection**: The project continuously watches for updates on Wikimedia, such as Wikipedia, and records them instantly.

- **Microservices Architecture**: The system is designed as small specialized teams that work together efficiently.

- **Kafka Integration**: Kafka acts as the messaging system, ensuring seamless communication between the data collector and the data storer.

- **Data Persistence**: All changes from Wikimedia are saved in a structured way in a database for easy access and analysis.

- **Asynchronous Processing**: The data collector and data storer work independently, allowing for efficient and quick handling of data.

- **Scalability**: The system can be easily scaled by adding more "reporters" or "librarians" to handle a higher volume of data.

- **Reliability**: Changes are captured and stored reliably, ensuring no data is lost, even during high loads.

By using this project, you can efficiently capture and analyze real-time changes happening on Wikimedia, which can be valuable for various research, analysis, and monitoring purposes.

### Flow

1. WikimediaChangeHandler Class
          
           package com.main.springboot;
       import com.launchdarkly.eventsource.EventHandler;
        import com.launchdarkly.eventsource.MessageEvent;
       import org.slf4j.Logger;
       import org.slf4j.LoggerFactory;
        import org.springframework.kafka.core.KafkaTemplate;

        public class WikimediaChangeHandler  implements EventHandler {
       private static final Logger logger = LoggerFactory.getLogger(WikimediaChangeHandler.class);
       private KafkaTemplate<String,String> kafkaTemplate;
         private String topic;

       public WikimediaChangeHandler(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        }
 
       @Override
       public void onOpen() throws Exception {

       }

        @Override
       public void onClosed() throws Exception {

        }

        @Override
        public void onMessage(String s, MessageEvent messageEvent) throws Exception {
        logger.info(String.format("event data -> %s", messageEvent.getData()));
        kafkaTemplate.send(topic, messageEvent.getData());
        }

       @Override
        public void onComment(String s) throws Exception {

       }

       @Override
       public void onError(Throwable throwable) {

        }
       }

The WikimediaChangeHandler class is an event handler responsible for processing events received from the Wikimedia event source and forwarding them to a Kafka topic.

It implements the EventHandler interface, which defines methods for handling various event-related actions.

The onMessage method is crucial here. It's called when a new message (change) is received from Wikimedia. It logs the received data and then uses the kafkaTemplate to send this data to the specified Kafka topic. This is the core of the data flow from Wikimedia to Kafka.

2. WikimediaChangesProducer Class

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
        String topic = "wikimedia_recentchange_topic";

        // To read real-time stream data from Wikimedia, an event source is used.
        EventHandler eventHandler = new WikimediaChangeHandler(kafkaTemplate, topic);

        // Defining the event source URL
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        // Passing the URL to EventSource, which connects to the source and reads event data.
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        // Starting the event source. It creates a thread to manage the connection.
        eventSource.start();

        // Sleep for a specified time to keep the program running. You can adjust the duration.
        TimeUnit.MINUTES.sleep(10);
       }
       }

The WikimediaChangesProducer class is responsible for initiating the process of collecting real-time data from Wikimedia and sending it to a Kafka topic.

It uses the EventHandler created in the WikimediaChangeHandler class to handle incoming events.

The sendMessage method sets up the event source, specifying the Wikimedia stream URL and the Kafka topic where data should be sent. It then starts the event source.

The TimeUnit.MINUTES.sleep(10) line allows the program to run for 10 minutes, continuously collecting and sending data to Kafka.

Importance:

These two classes work together to collect real-time data from Wikimedia and relay it to Kafka for further processing. The WikimediaChangeHandler handles the specific event handling logic, while the WikimediaChangesProducer coordinates the entire process.

This real-time data collection mechanism can be critical for monitoring and analyzing Wikimedia changes as they happen, which can be valuable for research, data analysis, and other applications. It demonstrates an efficient way to connect different components in a distributed system

3.Main Class

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

To Consumner @KafkaListener function 

## Snippets

### Wikimedia Data Source - <a href="https://stream.wikimedia.org/v2/stream/recentchange">Wikimedia Link</a>
![image](https://github.com/DarkSharkAsh/Microservices-Based-Wikimedia-Data-Processing-with-Kafka/assets/105435085/a10b8a70-4a41-4890-9cc1-50e6fec39267)


### DB
![image](https://github.com/DarkSharkAsh/Microservices-Based-Wikimedia-Data-Processing-with-Kafka/assets/105435085/c780ab98-a395-4b98-b258-71d31e14b9f1)

### Zookeeper
![image](https://github.com/DarkSharkAsh/Microservices-Based-Wikimedia-Data-Processing-with-Kafka/assets/105435085/bddc29b5-627b-4c04-b413-009e9ae3a482)

### Kafka Server 
![image](https://github.com/DarkSharkAsh/Microservices-Based-Wikimedia-Data-Processing-with-Kafka/assets/105435085/51ad6ef7-b987-4037-99ad-527bf3afcb26)




### Note 
Make sure ZooKeeper and kafka server are up and running 
