package com.br.processing_manager.rest;

import com.br.processing_manager.enums.ProcessingStatus;
import com.br.processing_manager.record.ProcessingRecord;
import com.br.processing_manager.service.ActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ActivityService service;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity test() throws JsonProcessingException {
        long time = new Date().getTime();
        ProcessingRecord processingRecord = new ProcessingRecord(time, "TESTE - "+time, 20, ProcessingStatus.IN_PROGRESS, false);
        service.raiseActivities(processingRecord);
        return new ResponseEntity(HttpStatus.OK);
    }
}
