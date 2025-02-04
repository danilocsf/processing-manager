package com.br.processing_manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityStatus {
    AWAITING("awaiting"),
    PROCESSING("processing"),
    FINISHED("finished");

    private final String description;

    ActivityStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }

    @JsonCreator
    public static ActivityStatus fromDescription(String description) {
        for (ActivityStatus status : ActivityStatus.values()) {
            if (status.description.equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status description: " + description);
    }
}
