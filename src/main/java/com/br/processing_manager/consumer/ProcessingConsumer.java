package com.br.processing_manager.consumer;

import com.br.processing_manager.record.ProcessingRecord;
import com.br.processing_manager.service.ActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class ProcessingConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActivityService service;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = "${topic.processing}",
            groupId = "${topic.processing.group.id}",
            containerFactory = "kafkaListenerContainerFactory",
            filter = "processingInProgressFilterStrategy")
    public void processingListener(String  processingContent) throws JsonProcessingException {
        LOGGER.info("Received new processing data");
        ProcessingRecord processingRecord = objectMapper.readValue(processingContent, ProcessingRecord.class);
        String msg = MessageFormat.format("Received Processing information: processing id: {0} - process name: {1}",
                String.valueOf(processingRecord.id()), processingRecord.processName());
        LOGGER.info(msg);
        service.raiseActivities(processingRecord);

    }

    @KafkaListener(topics = "${topic.processing}",
            groupId = "${topic.processing.result.group.id}",
            containerFactory = "kafkaListenerContainerFactory",
            filter = "processingFinishedFilterStrategy")
    public void processingResultListener(String  processingResultContent) throws JsonProcessingException {
        LOGGER.info("Received new processing result data");
        ProcessingRecord processingRecord = objectMapper.readValue(processingResultContent, ProcessingRecord.class);
        String msg = MessageFormat.format("Processing successfully finished: processing id: {0} - process name: {1}",
                String.valueOf(processingRecord.id()), processingRecord.processName());
        LOGGER.info(msg);
    }

}
