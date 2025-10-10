package com.upc.pre.peaceapp.alerts.application.internal.queryservices;

import com.upc.pre.peaceapp.alerts.domain.model.aggregates.Alert;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAlertByIdQuery;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAlertsByUserIdQuery;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAllAlertsQuery;
import com.upc.pre.peaceapp.alerts.domain.services.AlertQueryService;
import com.upc.pre.peaceapp.alerts.infrastructure.persistence.jpa.AlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AlertQueryServiceImpl implements AlertQueryService {

    private final AlertRepository alertRepository;

    public AlertQueryServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public Optional<Alert> handle(GetAlertByIdQuery query) {
        log.info("Fetching alert by ID: {}", query.id());
        return alertRepository.findById(query.id());
    }

    @Override
    public List<Alert> handle(GetAlertsByUserIdQuery query) {
        log.info("Fetching alerts for user ID: {}", query.userId());
        return alertRepository.findAllByUserId(query.userId());
    }

    @Override
    public List<Alert> handle(GetAllAlertsQuery query) {
        log.info("Fetching all alerts");
        return alertRepository.findAll();
    }
}
