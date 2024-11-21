package com.vehicle.management.model;

import com.vehicle.management.enums.AlertType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "geofence_event")
public class GeofenceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehicleId;

    @Column(nullable = false)
    private String geofenceName;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private long stayDuration;

    private boolean authorized;

    private AlertType alertType;

}
