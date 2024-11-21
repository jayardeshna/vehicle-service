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

    @Column(name = "vehicle_id")
    private String vehicleId;

    @Column(name = "geofence_name")
    private String geofenceName;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "alert_type")
    @Enumerated(EnumType.STRING)
    private AlertType alertType;

    public Alert(String vehicleId, String geofenceName, LocalDateTime timestamp, AlertType alertType){
        this.vehicleId  = vehicleId;
        this.geofenceName = geofenceName;
        this.timestamp = timestamp;
        this.alertType = alertType;
    }

}
