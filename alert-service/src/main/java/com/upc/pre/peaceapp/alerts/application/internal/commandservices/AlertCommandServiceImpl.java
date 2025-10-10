package com.upc.pre.peaceapp.alerts.application.internal.commandservices;

import com.upc.pre.peaceapp.alerts.domain.model.aggregates.Alert;
import com.upc.pre.peaceapp.alerts.domain.model.commands.CreateAlertCommand;
import com.upc.pre.peaceapp.alerts.domain.model.commands.DeleteAllAlertsByUserIdCommand;
import com.upc.pre.peaceapp.alerts.domain.services.AlertCommandService;
import com.upc.pre.peaceapp.alerts.infrastructure.persistence.jpa.AlertRepository;
import com.upc.pre.peaceapp.alerts.application.internal.outboundservices.ExternalUserService;
import com.upc.pre.peaceapp.alerts.application.internal.outboundservices.ExternalReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AlertCommandServiceImpl implements AlertCommandService {

    private final AlertRepository alertRepository;
    private final ExternalUserService userService;
    private final ExternalReportService reportService;

    public AlertCommandServiceImpl(AlertRepository alertRepository,
                                   ExternalUserService userService,
                                   ExternalReportService reportService) {
        this.alertRepository = alertRepository;
        this.userService = userService;
        this.reportService = reportService;
    }

    @Override
    @Transactional
    public Optional<Alert> handle(CreateAlertCommand command) {
        log.info("Creating alert for user ID: {}", command.userId());

        if (!userService.existsById(command.userId())) {
            log.error("User with ID {} does not exist", command.userId());
            throw new IllegalArgumentException("User not found");
        }

        if (command.reportId() != null && !reportService.existsById(command.reportId())) {
            log.error("Report with ID {} does not exist", command.reportId());
            throw new IllegalArgumentException("Report not found");
        }

        var alert = new Alert(
                command.location(),
                command.type(),
                command.description(),
                command.userId(),
                command.imageUrl(),
                command.reportId()
        );

        var savedAlert = alertRepository.save(alert);
        log.info("Alert created successfully with ID: {}", savedAlert.getId());
        return Optional.of(savedAlert);
    }

    @Override
    @Transactional
    public void handle(DeleteAllAlertsByUserIdCommand command) {
        log.info("Deleting all alerts for user ID: {}", command.userId());
        alertRepository.deleteAllByUserId(command.userId());
        log.info("All alerts deleted successfully for user ID: {}", command.userId());
    }
}
