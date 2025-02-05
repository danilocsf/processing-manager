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
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActivityProducer producer;

    @Transactional
    public void raiseActivities(ProcessingRecord processing) {
        List<ActivityRecord> activities = buildActivities(processing);
        int errorPosition = mockErrorPosition(processing);

        int i = 0;
        for (ActivityRecord activity : activities) {
            if(errorPosition != 0 && i == errorPosition) {
                throw new RuntimeException("Mocked Exception");
            }
            i++;
            try {
                producer.sendActivity(activity);
            } catch (JsonProcessingException e) {
                String msg = MessageFormat.format("Error sending activity {0}: {1}", activity.activityName(),
                        e.getMessage());
                LOGGER.error(msg);
                throw new RuntimeException(e);
            }
        }
    }

    private int mockErrorPosition(ProcessingRecord processing) {
        int position = 0;
        if (processing.mockError()){
            Random random = ThreadLocalRandom.current();
            position = random.nextInt(processing.numberOfActivities()/2, processing.numberOfActivities()+1) - 1;
            LOGGER.info("Application will throw an error on activity "+position);
        }
        return position;
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
