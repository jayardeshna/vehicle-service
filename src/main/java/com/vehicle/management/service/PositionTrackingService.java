package com.vehicle.management.service;

import java.time.LocalDateTime;

public interface PositionTrackingService {

    void processVehiclePosition(String vehicleId, double latitude, double longitude, LocalDateTime timestamp);

}
