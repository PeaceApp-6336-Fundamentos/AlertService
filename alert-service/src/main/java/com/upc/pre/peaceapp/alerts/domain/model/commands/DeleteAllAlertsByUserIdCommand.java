package com.upc.pre.peaceapp.alerts.domain.model.commands;

public record DeleteAllAlertsByUserIdCommand(Long userId) {
    public DeleteAllAlertsByUserIdCommand {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId must be greater than 0");
    }
}
