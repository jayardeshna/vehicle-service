package com.vehicle.management.repository;

import com.vehicle.management.model.GeofenceVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GeofenceVehicleRepository extends JpaRepository<GeofenceVehicle, Long> {

    @Query("SELECT gv FROM GeofenceVehicle gv WHERE gv.geofence.id = :geofenceId AND gv.vehicle.id = :vehicleId")
    Optional<GeofenceVehicle> findByGeofenceAndVehicleId(Long geofenceId, Long vehicleId);

}
