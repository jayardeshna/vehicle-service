package com.vehicle.management.repository;

import com.vehicle.management.enums.AlertType;
import com.vehicle.management.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("""
       SELECT a FROM Alert a WHERE 
       (:vehicleId IS NULL OR a.vehicleId = :vehicleId) AND 
       (:geofenceName IS NULL OR a.geofenceName = :geofenceName) AND 
       (:alertType IS NULL OR a.alertType = :alertType)
       """)
    List<Alert> findAlerts(String vehicleId, String geofenceName, AlertType alertType);

}
