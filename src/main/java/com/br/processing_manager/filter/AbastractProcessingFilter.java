package com.br.processing_manager.filter;


import com.br.processing_manager.enums.ProcessingStatus;
import com.br.processing_manager.record.ProcessingRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.text.MessageFormat;

public abstract class AbastractProcessingFilter implements RecordFilterStrategy<String, String> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper objectMapper;

    protected boolean filter(ConsumerRecord<String, String> consumerRecord, ProcessingStatus acceptedStatus) {
        LOGGER.info("Filtering received message from topic "+consumerRecord.topic());
        LOGGER.info("Accepted status: "+acceptedStatus);
        boolean skipMsg = true;
        ProcessingRecord processingRecord;
        try {
            processingRecord = objectMapper.readValue(consumerRecord.value(), ProcessingRecord.class);
            String msg = MessageFormat.format("Message information: partition: {0} - offset: {1} - key: {2} - status: {3}",
                    consumerRecord.partition(), consumerRecord.offset(), consumerRecord.key(), processingRecord.status());
            LOGGER.info(msg);
            skipMsg = acceptedStatus != processingRecord.status();
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting "+consumerRecord.value());
        }
        if (skipMsg)
            LOGGER.info("Skipping message");
        return skipMsg;
    }
}
