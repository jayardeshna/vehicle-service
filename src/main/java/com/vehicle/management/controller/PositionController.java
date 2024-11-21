package com.vehicle.management.controller;

import com.vehicle.management.model.rest.RestResponse;
import com.vehicle.management.service.PositionTrackingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

    private final PositionTrackingService positionTrackingService;

    @PostMapping("/update")
    RestResponse updateVehiclePosition(@RequestParam String vehicleId, @RequestParam double latitude, @RequestParam double longitude){
        positionTrackingService.processVehiclePosition(vehicleId, latitude, longitude, LocalDateTime.now());
        return new RestResponse(true, "vehicle's position updated successfully");
    }

}
