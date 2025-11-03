package com.upc.pre.peaceapp.alerts.interfaces.rest;

import com.upc.pre.peaceapp.alerts.domain.model.aggregates.Alert;
import com.upc.pre.peaceapp.alerts.domain.model.commands.CreateAlertCommand;
import com.upc.pre.peaceapp.alerts.domain.model.commands.DeleteAllAlertsByUserIdCommand;
import com.upc.pre.peaceapp.alerts.domain.model.commands.DeleteAllAlertsByReportIdCommand;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAlertByIdQuery;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAlertsByUserIdQuery;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAllAlertsQuery;
import com.upc.pre.peaceapp.alerts.domain.services.AlertCommandService;
import com.upc.pre.peaceapp.alerts.domain.services.AlertQueryService;
import com.upc.pre.peaceapp.alerts.interfaces.rest.resources.AlertResource;
import com.upc.pre.peaceapp.alerts.interfaces.rest.resources.CreateAlertResource;
import com.upc.pre.peaceapp.alerts.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import com.upc.pre.peaceapp.alerts.interfaces.rest.transform.CreateAlertCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/alerts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Alerts", description = "Operations related to Alert Management")
@Slf4j
public class AlertController {

    private final AlertCommandService alertCommandService;
    private final AlertQueryService alertQueryService;
    private final CreateAlertCommandFromResourceAssembler createAlertCommandFromResourceAssembler;

    public AlertController(AlertCommandService alertCommandService,
                           AlertQueryService alertQueryService,
                           CreateAlertCommandFromResourceAssembler createAlertCommandFromResourceAssembler) {
        this.alertCommandService = alertCommandService;
        this.alertQueryService = alertQueryService;
        this.createAlertCommandFromResourceAssembler = createAlertCommandFromResourceAssembler;
    }

    // ----------------------------------------------------------------------
    // CREATE ALERT
    // ----------------------------------------------------------------------
    @Operation(summary = "Create a new alert",
            description = "Registers a new alert for a specific user. The user and report must exist if referenced.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alert created successfully",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AlertResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or related entities not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAlert(@RequestBody CreateAlertResource resource) {
        log.info("Creating new alert for user ID: {}", resource.userId());

        try {
            CreateAlertCommand command = createAlertCommandFromResourceAssembler.toCommand(resource);
            Optional<Alert> alertOptional = alertCommandService.handle(command);

            return alertOptional
                    .map(alert -> {
                        AlertResource alertResource = AlertResourceFromEntityAssembler.toResourceFromEntity(alert);
                        URI location = URI.create("/api/v1/alerts/" + alert.getId());
                        return ResponseEntity.created(location).body(alertResource);
                    })
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            log.error("Error creating alert: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating alert: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    // GET ALERT BY ID
    // ----------------------------------------------------------------------
    @Operation(summary = "Get alert by ID", description = "Retrieve a specific alert by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AlertResource.class))),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlertResource> getAlertById(@PathVariable Long id) {
        var alert = alertQueryService.handle(new GetAlertByIdQuery(id));
        return alert
                .map(a -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(a)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ----------------------------------------------------------------------
    // GET ALERTS BY USER ID
    // ----------------------------------------------------------------------
    @Operation(summary = "Get alerts by user ID",
            description = "Retrieve all alerts created or assigned to a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerts found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AlertResource.class))),
            @ApiResponse(responseCode = "404", description = "No alerts found for the specified user")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AlertResource>> getAlertsByUserId(
            @Parameter(description = "User ID to search alerts for", required = true)
            @PathVariable Long userId) {

        log.info("Fetching alerts for user ID: {}", userId);

        var query = new GetAlertsByUserIdQuery(userId);
        var alerts = alertQueryService.handle(query);

        if (alerts.isEmpty()) return ResponseEntity.notFound().build();

        var alertResources = alerts.stream()
                .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(alertResources);
    }

    // ----------------------------------------------------------------------
    // GET ALL ALERTS
    // ----------------------------------------------------------------------
    @Operation(summary = "Get all alerts", description = "Retrieve all alerts registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerts retrieved successfully",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AlertResource.class))),
            @ApiResponse(responseCode = "204", description = "No alerts found")
    })
    @GetMapping
    public ResponseEntity<List<AlertResource>> getAllAlerts() {
        log.info("Fetching all alerts");

        var alerts = alertQueryService.handle(new GetAllAlertsQuery());
        if (alerts.isEmpty()) return ResponseEntity.noContent().build();

        var alertResources = alerts.stream()
                .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(alertResources);
    }

    // ----------------------------------------------------------------------
    // DELETE ALL ALERTS BY USER ID
    // ----------------------------------------------------------------------
    @Operation(summary = "Delete all alerts by user ID",
            description = "Deletes all alerts created by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All alerts for the user deleted successfully"),
            @ApiResponse(responseCode = "404", description = "No alerts found for the user")
    })
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteAllAlertsByUserId(@PathVariable Long userId) {
        log.warn("Deleting all alerts for user ID: {}", userId);

        try {
            alertCommandService.handle(new DeleteAllAlertsByUserIdCommand(userId));
            return ResponseEntity.ok("All alerts for user ID " + userId + " deleted successfully");
        } catch (IllegalArgumentException e) {
            log.error("Invalid user ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting alerts for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    // DELETE ALL ALERTS BY REPORT ID
    // ----------------------------------------------------------------------
    @Operation(summary = "Delete all alerts by report ID",
            description = "Deletes all alerts associated with a specific report.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All alerts for the report deleted successfully"),
            @ApiResponse(responseCode = "404", description = "No alerts found for the report")
    })
    @DeleteMapping("/report/{reportId}")
    public ResponseEntity<?> deleteAllAlertsByReportId(@PathVariable Long reportId) {
        log.warn("Deleting all alerts for report ID: {}", reportId);

        try {
            alertCommandService.handle(new DeleteAllAlertsByReportIdCommand(reportId));
            return ResponseEntity.ok("All alerts for report ID " + reportId + " deleted successfully");
        } catch (IllegalArgumentException e) {
            log.error("Invalid report ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting alerts for report ID {}: {}", reportId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
