package com.vehicle.management.model;

import com.vehicle.management.enums.AlertType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alert")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "geofence_id")
    private Geofence geofence;

    @Column(name = "alert_type")
    private AlertType alertType;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
