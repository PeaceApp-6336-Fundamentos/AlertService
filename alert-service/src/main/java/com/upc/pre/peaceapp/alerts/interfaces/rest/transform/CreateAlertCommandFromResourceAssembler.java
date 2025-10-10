package com.upc.pre.peaceapp.alerts.interfaces.rest.transform;

import com.upc.pre.peaceapp.alerts.domain.model.commands.CreateAlertCommand;
import com.upc.pre.peaceapp.alerts.interfaces.rest.resources.CreateAlertResource;
import org.springframework.stereotype.Component;

@Component
public class CreateAlertCommandFromResourceAssembler {

    public CreateAlertCommand toCommand(CreateAlertResource resource) {
        return new CreateAlertCommand(
                resource.location(),
                resource.type(),
                resource.description(),
                resource.userId(),
                resource.imageUrl(),
                resource.reportId()
        );
    }
}
