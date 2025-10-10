package com.upc.pre.peaceapp.alerts.interfaces.rest.transform;

import com.upc.pre.peaceapp.alerts.domain.model.aggregates.Alert;
import com.upc.pre.peaceapp.alerts.interfaces.rest.resources.AlertResource;
import org.springframework.stereotype.Component;

@Component
public class AlertResourceFromEntityAssembler {

    public static AlertResource toResourceFromEntity(Alert alert) {
        return new AlertResource(
                alert.getId(),
                alert.getLocation(),
                alert.getType(),
                alert.getDescription(),
                alert.getUserId(),
                alert.getImageUrl(),
                alert.getReportId()
        );
    }
}
