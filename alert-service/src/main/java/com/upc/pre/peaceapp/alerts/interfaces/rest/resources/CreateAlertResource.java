package com.upc.pre.peaceapp.alerts.interfaces.rest.resources;

import com.upc.pre.peaceapp.alerts.domain.model.valueobjects.AlertType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload for creating a new alert")
public record CreateAlertResource(

        @NotBlank
        @Schema(description = "Alert location", example = "Av. Primavera 123, Lima")
        String location,

        @NotNull
        @Schema(description = "Alert type", example = "ROBBERY")
        AlertType type,

        @Schema(description = "Description of the alert", example = "Robbery reported in the area")
        String description,

        @NotNull
        @Schema(description = "User ID associated with this alert", example = "101")
        Long userId,

        @Schema(description = "Image URL associated with this alert", example = "https://example.com/images/alert1.jpg")
        String imageUrl,

        @Schema(description = "Associated report ID if available", example = "45")
        Long reportId
) {}
