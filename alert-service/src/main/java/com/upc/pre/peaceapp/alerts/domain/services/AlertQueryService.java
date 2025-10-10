package com.upc.pre.peaceapp.alerts.domain.services;

import com.upc.pre.peaceapp.alerts.domain.model.aggregates.Alert;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAlertByIdQuery;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAlertsByUserIdQuery;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAllAlertsQuery;

import java.util.List;
import java.util.Optional;

public interface AlertQueryService {
    Optional<Alert> handle(GetAlertByIdQuery query);
    List<Alert> handle(GetAlertsByUserIdQuery query);
    List<Alert> handle(GetAllAlertsQuery query);
}
