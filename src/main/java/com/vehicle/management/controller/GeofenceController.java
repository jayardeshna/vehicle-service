package com.vehicle.management.controller;

import com.vehicle.management.model.Geofence;
import com.vehicle.management.model.rest.RestResponse;
import com.vehicle.management.service.GeofenceService;
import com.vehicle.management.transfer.GeofenceRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/geofence")
public class GeofenceController {

    private final GeofenceService geofenceService;

    @PostMapping
    public RestResponse createGeofence(@RequestBody GeofenceRequest geofenceRequest) {
        Geofence geofence = geofenceService.createGeofence(geofenceRequest);
        return new RestResponse(true, "Geofence created successfully");
    }

    @GetMapping
    public RestResponse getAllGeofences() {
        return new RestResponse(true, geofenceService.getAllGeofences());
    }

    @GetMapping("/{geofenceId}")
    public RestResponse getGeofence(@PathVariable Long geofenceId) {
        return new RestResponse(true, geofenceService.getGeofence(geofenceId));
    }

    @PutMapping("/{geofenceId}")
    public RestResponse updateGeofence(@PathVariable Long geofenceId, @RequestBody GeofenceRequest geofenceRequest) {
        geofenceService.updateGeofence(geofenceId, geofenceRequest);
        return new RestResponse(true, "geofence updated successfully");
    }


}
