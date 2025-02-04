package com.br.processing_manager.filter;

import com.br.processing_manager.enums.ProcessingStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class ProcessingInProgressFilterStrategy extends AbastractProcessingFilter {

    @Override
    public boolean filter(ConsumerRecord<String, String> consumerRecord) {
        return super.filter(consumerRecord, ProcessingStatus.IN_PROGRESS);
    }
}
