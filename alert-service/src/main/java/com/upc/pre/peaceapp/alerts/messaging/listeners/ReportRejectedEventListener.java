package com.upc.pre.peaceapp.alerts.messaging.listeners;

import com.upc.pre.peaceapp.alerts.domain.model.commands.DeleteAllAlertsByReportIdCommand;
import com.upc.pre.peaceapp.alerts.domain.services.AlertCommandService;
import com.upc.pre.peaceapp.alerts.messaging.events.ReportRejectedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class ReportRejectedEventListener {

    private final AlertCommandService alertCommandService;

    public ReportRejectedEventListener(AlertCommandService alertCommandService) {
        this.alertCommandService = alertCommandService;
    }

    @RabbitListener(
            queues = "${app.broker.queue.report.rejected}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleReportRejected(ReportRejectedEvent event) {
        if (event.getReportId() != null) {
            alertCommandService.handle(new DeleteAllAlertsByReportIdCommand(event.getReportId()));
            System.out.println("🚨 Deleted all alerts (REJECTED) for report ID: " + event.getReportId());
        } else {
            System.out.println("⚠️ Skipped REJECTED alert deletion: reportId missing in event.");
        }
    }
}
