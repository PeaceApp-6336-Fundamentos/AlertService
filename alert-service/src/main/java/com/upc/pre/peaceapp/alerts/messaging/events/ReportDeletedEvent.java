package com.upc.pre.peaceapp.alerts.messaging.events;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published by the Report Service when a report is deleted.
 * Consumed by the Alert Service to remove all alerts related to that report or user.
 */
@Setter
@Getter
public class ReportDeletedEvent implements Serializable {

    private Long reportId;
    private Long userId;
    private String message;
    private LocalDateTime timestamp;

    // Empty constructor required for deserialization
    public ReportDeletedEvent() {}

    public ReportDeletedEvent(Long reportId, Long userId, String message, LocalDateTime timestamp) {
        this.reportId = reportId;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

}
