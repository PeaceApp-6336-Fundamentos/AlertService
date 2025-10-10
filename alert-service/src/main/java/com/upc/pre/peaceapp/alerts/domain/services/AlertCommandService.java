package com.upc.pre.peaceapp.alerts.domain.services;

import com.upc.pre.peaceapp.alerts.domain.model.aggregates.Alert;
import com.upc.pre.peaceapp.alerts.domain.model.commands.CreateAlertCommand;
import com.upc.pre.peaceapp.alerts.domain.model.commands.DeleteAllAlertsByUserIdCommand;

import java.util.Optional;

public interface AlertCommandService {
    Optional<Alert> handle(CreateAlertCommand command);
    void handle(DeleteAllAlertsByUserIdCommand command);
}
