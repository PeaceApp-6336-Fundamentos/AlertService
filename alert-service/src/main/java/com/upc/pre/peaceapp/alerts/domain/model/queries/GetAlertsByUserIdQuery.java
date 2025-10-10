package com.upc.pre.peaceapp.alerts.domain.model.queries;

public record GetAlertsByUserIdQuery(Long userId) {
    public GetAlertsByUserIdQuery {
        if (userId <= 0)
            throw new IllegalArgumentException("userId must be greater than 0");
    }
}