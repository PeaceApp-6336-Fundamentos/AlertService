package com.upc.pre.peaceapp.alerts.domain.model.aggregates;
import com.upc.pre.peaceapp.alerts.domain.model.valueobjects.AlertType;
import com.upc.pre.peaceapp.shared.documentation.models.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the Alert aggregate entity within the Alerts bounded context.
 * An Alert is typically generated in response to a report or an immediate incident,
 * detailing the location, type, and associated user/report information.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "alerts")
public class Alert extends AuditableAbstractAggregateRoot {

    /**
     * The unique identifier for the alert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A description of the incident's location (e.g., street name or landmark).
     */
    @Column(name = "location", nullable = false, length = 100)
    private String location;

    /**
     * The type of alert, defined by the {@link AlertType} enumeration.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private AlertType type;

    /**
     * A detailed description of the alert or incident.
     */
    @Column(name = "description")
    private String description;

    /**
     * The ID of the user who generated or is related to the alert.
     */
    @Column(name = "id_user", nullable = false)
    private Long userId;

    /**
     * The URL of the image evidence related to the alert.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * The ID of the report associated with this alert (can be null).
     */
    @Column(name = "id_report")
    private Long reportId;

    /**
     * Constructor for creating a new Alert instance.
     *
     * @param location The description of the location.
     * @param type The type of alert ({@link AlertType}).
     * @param description The detailed description of the alert.
     * @param userId The ID of the user associated with the alert.
     * @param imageUrl The URL of the image evidence.
     * @param reportId The ID of the related report (can be null).
     */
    public Alert(String location, AlertType type, String description, Long userId, String imageUrl, Long reportId) {
        this.location = location;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.reportId = reportId;
    }
}