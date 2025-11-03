package com.upc.pre.peaceapp.alerts.messaging.listeners;

import com.upc.pre.peaceapp.alerts.domain.model.commands.DeleteAllAlertsByReportIdCommand;
import com.upc.pre.peaceapp.alerts.domain.services.AlertCommandService;
import com.upc.pre.peaceapp.alerts.messaging.events.ReportDeletedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class ReportDeletedEventListener {

    private final AlertCommandService alertCommandService;

    public ReportDeletedEventListener(AlertCommandService alertCommandService) {
        this.alertCommandService = alertCommandService;
    }

    @RabbitListener(
            queues = "${app.broker.queue.alert.deleted}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleReportDeleted(ReportDeletedEvent event) {
        if (event.getReportId() != null) {
            alertCommandService.handle(new DeleteAllAlertsByReportIdCommand(event.getReportId()));
            System.out.println("🚨 Deleted all alerts for report ID: " + event.getReportId());
        } else {
            System.out.println("⚠️ Skipped alert deletion: reportId missing in event.");
        }
    }
}
