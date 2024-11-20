package com.vehicle.management.repository;

import com.vehicle.management.model.GeofenceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GeofenceEventRepository extends JpaRepository<GeofenceEvent, Long> {

    @Query("SELECT e FROM GeofenceEvent e WHERE e.vehicleId = :vehicleId AND e.geofence.id = :geofenceId AND e.exitTime IS NULL")
    Optional<GeofenceEvent> findOngoingEvent(@Param("vehicleId") Long vehicleId, @Param("geofenceId") Long geofenceId);

    @Query("SELECT e FROM GeofenceEvent e WHERE e.vehicleId = :vehicleId AND e.exitTime IS NULL")
    Optional<GeofenceEvent> findOngoingEventByVehicle(@Param("vehicleId") Long vehicleId);

}
