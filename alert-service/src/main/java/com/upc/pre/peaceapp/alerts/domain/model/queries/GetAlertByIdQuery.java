package com.upc.pre.peaceapp.alerts.domain.model.queries;

public record GetAlertByIdQuery(Long id) {
    public GetAlertByIdQuery {
        if (id == null)
            throw new IllegalArgumentException("id cannot be null");
    }
}