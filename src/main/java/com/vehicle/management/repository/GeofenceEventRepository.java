package com.vehicle.management.repository;

import com.vehicle.management.model.GeofenceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface GeofenceEventRepository extends JpaRepository<GeofenceEvent, Long> {

    List<GeofenceEvent> findByVehicleId(String vehicleId);

    List<GeofenceEvent> findByEntryTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<GeofenceEvent> findByVehicleIdAndEntryTimeBetween(String vehicleId, LocalDateTime startDate, LocalDateTime endDate);
}

