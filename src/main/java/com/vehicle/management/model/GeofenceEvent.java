package com.vehicle.management.model;

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

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "geofence_id", nullable = false)
    private Geofence geofence;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Transient
    private Duration duration;

    @Column(name = "is_authorized", nullable = false)
    private Boolean isAuthorized;

    public Duration getDuration() {
        if (entryTime != null && exitTime != null) {
            return Duration.between(entryTime, exitTime);
        }
        return null;
    }
}
