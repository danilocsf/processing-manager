package com.br.processing_manager.producer;


import com.br.processing_manager.record.ActivityRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class ActivityProducer {

    @Value("${topic.activity}")
    private String activityTopic;

    @Value("${topic.activity.partitions:3}")
    private int partitions;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void sendActivity(ActivityRecord activity) throws JsonProcessingException {
        String data = objectMapper.writeValueAsString(activity);        ;
        String msg = MessageFormat.format("Sending activity {0} from process {1} to topic {2}",  activity.activityName(),
                activity.processName(), activityTopic);
        LOGGER.info(msg);
        kafkaTemplate.send(activityTopic,null, data);
    }

}
