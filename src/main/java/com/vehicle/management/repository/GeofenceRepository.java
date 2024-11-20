package com.vehicle.management.repository;

import com.vehicle.management.model.Geofence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GeofenceRepository extends JpaRepository<Geofence, Long> {

    @Query("SELECT g FROM Geofence g WHERE ST_Contains(g.polygon, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) = true")
    Optional<Geofence> findGeofenceByLocation(@Param("latitude") double latitude, @Param("longitude") double longitude);

}
