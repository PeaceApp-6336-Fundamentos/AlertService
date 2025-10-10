package com.upc.pre.peaceapp.alerts.interfaces.rest.resources;

import com.upc.pre.peaceapp.alerts.domain.model.valueobjects.AlertType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Alert resource representation")
public record AlertResource(

        @Schema(description = "Alert unique identifier", example = "1")
        Long id,

        @Schema(description = "Alert location", example = "Av. Primavera 123, Lima")
        String location,

        @Schema(description = "Alert type", example = "ROBBERY")
        AlertType type,

        @Schema(description = "Description of the alert", example = "Robbery reported in the area")
        String description,

        @Schema(description = "User ID who reported or receives the alert", example = "101")
        Long userId,

        @Schema(description = "Image URL associated with the alert", example = "https://example.com/images/alert1.jpg")
        String imageUrl,

        @Schema(description = "Associated report ID if exists", example = "45")
        Long reportId
) {}
