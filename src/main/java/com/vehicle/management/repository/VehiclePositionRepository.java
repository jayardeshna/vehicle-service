package com.vehicle.management.repository;

import com.vehicle.management.model.VehiclePosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclePositionRepository extends JpaRepository<VehiclePosition, Long> {
}
