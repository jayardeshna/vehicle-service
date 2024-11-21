package com.vehicle.management.repository;

import com.vehicle.management.model.Geofence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GeofenceRepository extends JpaRepository<Geofence, Long> {

    Geofence findByName(String name);

}
