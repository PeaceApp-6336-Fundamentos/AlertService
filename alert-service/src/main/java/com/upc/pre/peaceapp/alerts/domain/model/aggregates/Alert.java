package com.upc.pre.peaceapp.alerts.domain.model.aggregates;
import com.upc.pre.peaceapp.alerts.domain.model.valueobjects.AlertType;
import com.upc.pre.peaceapp.shared.documentation.models.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "alerts")
public class Alert extends AuditableAbstractAggregateRoot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "location", nullable = false, length = 100)
    private String location;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private AlertType type;
    @Column(name = "description")
    private String description;
    @Column(name = "id_user", nullable = false)
    private Long userId;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "id_report")
    private Long reportId;
    public Alert(String location, AlertType type, String description, Long userId, String imageUrl, Long reportId) {
        this.location = location;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.reportId = reportId;
    }
}
