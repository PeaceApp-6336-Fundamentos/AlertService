package com.upc.pre.peaceapp.alerts.domain.model.commands;

import com.upc.pre.peaceapp.alerts.domain.model.valueobjects.AlertType;

public record CreateAlertCommand(
        String location,
        AlertType type,
        String description,
        Long userId,
        String imageUrl,
        Long reportId
) {
    public CreateAlertCommand {
        if (location == null || location.isBlank())
            throw new IllegalArgumentException("location cannot be null or empty");
        if (type == null)
            throw new IllegalArgumentException("type cannot be null or empty");
        if (userId <= 0)
            throw new IllegalArgumentException("userId must be greater than 0");
    }
}
