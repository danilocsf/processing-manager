package com.br.processing_manager.record;


import com.br.processing_manager.enums.ProcessingStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
    Exemplo de Json
    {
	"id": 1497014222380,
    "processName": "Processo teste",
    "numberOfActivities": 12,
    "status":"finished",
    "mockError": false
}
 */
public record ProcessingRecord(
        Long id,
        String processName,
        int numberOfActivities,
        @JsonProperty("status") ProcessingStatus status,
        boolean mockError
    ) {}
