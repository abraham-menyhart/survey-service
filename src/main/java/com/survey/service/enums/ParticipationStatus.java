package com.survey.service.enums;

/**
 * Represents the participation status of a member in a survey.
 * The IDs (1-4) match the status values used in the CSV data files.
 */
public enum ParticipationStatus {
    NOT_ASKED(1L, "Not asked"),      // Status ID 1: Member was not invited to participate
    REJECTED(2L, "Rejected"),        // Status ID 2: Member was invited but declined
    FILTERED(3L, "Filtered"),        // Status ID 3: Member didn't match target criteria
    COMPLETED(4L, "Completed");      // Status ID 4: Member completed the survey

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