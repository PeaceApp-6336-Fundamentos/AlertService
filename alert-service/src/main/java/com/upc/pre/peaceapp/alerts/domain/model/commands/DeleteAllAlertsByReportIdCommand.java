package com.upc.pre.peaceapp.alerts.domain.model.commands;

public record DeleteAllAlertsByReportIdCommand(Long reportId) {
    public DeleteAllAlertsByReportIdCommand {
        if (reportId == null || reportId <= 0)
            throw new IllegalArgumentException("reportId must be greater than 0");
    }
}
