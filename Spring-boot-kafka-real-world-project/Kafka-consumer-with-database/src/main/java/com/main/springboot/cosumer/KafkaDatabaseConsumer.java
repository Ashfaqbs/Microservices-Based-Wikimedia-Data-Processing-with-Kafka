package com.main.springboot.cosumer;

import com.main.springboot.cosumer.DAO.WikimediaDataDAO;
import com.main.springboot.cosumer.entity.WikimediaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaDatabaseConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);
@Autowired
private WikimediaDataDAO wikimediaDataDAO;

    @KafkaListener(topics = "wikimedia_recentchange_topic",groupId = "mygroup")
public void consume(String message)
{

    //since the data from wikimedia is not defined or is not proper to be assigned to a particular class/object
//    we will save it as lob large object and have id which spring boot will generate
    WikimediaData wikimediaData = new WikimediaData();
        wikimediaData.setWikiEventData(message);



    wikimediaDataDAO.save(wikimediaData);

    LOGGER.info(String.format("Consumer Message received  -> %s",message));

}




}
