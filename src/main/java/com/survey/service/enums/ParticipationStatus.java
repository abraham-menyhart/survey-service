package com.survey.service.enums;

public enum ParticipationStatus {
    NOT_ASKED(1L, "Not asked"),
    REJECTED(2L, "Rejected"),
    FILTERED(3L, "Filtered"),
    COMPLETED(4L, "Completed");

    private final Long id;
    private final String name;

    ParticipationStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ParticipationStatus fromId(Long id) {
        for (ParticipationStatus status : ParticipationStatus.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status ID: " + id);
    }

    public boolean isEligibleForPoints() {
        return this == COMPLETED || this == FILTERED;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }
}