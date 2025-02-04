package com.br.processing_manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessingStatus {
    IN_PROGRESS("in_progress"),
    FINISHED("finished");

    private final String description;

    ProcessingStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }

    @JsonCreator
    public static ProcessingStatus fromDescription(String description) {
        for (ProcessingStatus status : ProcessingStatus.values()) {
            if (status.description.equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status description: " + description);
    }
}
