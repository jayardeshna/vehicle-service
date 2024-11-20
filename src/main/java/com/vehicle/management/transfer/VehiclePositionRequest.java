package com.vehicle.management.transfer;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehiclePositionRequest {

    private Long vehicleId;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;

}
