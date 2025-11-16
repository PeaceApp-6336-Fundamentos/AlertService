package com.upc.pre.peaceapp.alerts.messaging.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRejectedEvent implements Serializable {

    private Long reportId;
    private Long userId;
    private String message;
    private LocalDateTime timestamp;

    public ReportRejectedEvent() {}

    public ReportRejectedEvent(Long reportId, Long userId, String message, LocalDateTime timestamp) {
        this.reportId = reportId;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }
}
