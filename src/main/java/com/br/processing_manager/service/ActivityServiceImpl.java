package com.br.processing_manager.service;

import com.br.processing_manager.enums.ActivityStatus;
import com.br.processing_manager.producer.ActivityProducer;
import com.br.processing_manager.record.ActivityRecord;
import com.br.processing_manager.record.ProcessingRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActivityProducer producer;

    public void raiseActivities(ProcessingRecord processing) {
        List<ActivityRecord> activities = buildActivities(processing);
        activities.forEach(a -> {
            try {
                producer.sendActivity(a);
            } catch (JsonProcessingException e) {
                String msg = MessageFormat.format("Error sending activity {0}: {1}", a.activityName(),
                        e.getMessage());
                LOGGER.error(msg);
                throw new RuntimeException(e);
            }
        });
    }

    private List<ActivityRecord> buildActivities(ProcessingRecord processing){
        String msg = MessageFormat.format("Building {0} activities for process {1}", processing.numberOfActivities(),
                processing.processName());
        LOGGER.info(msg);
        List<ActivityRecord> activities = new ArrayList<>();
        for(int i=0; i< processing.numberOfActivities(); i++){
            activities.add(new ActivityRecord(processing.id(), processing.processName(), "Activity "+i,
                    processing.numberOfActivities(), ActivityStatus.AWAITING));
        }
        return activities;
    }
}
