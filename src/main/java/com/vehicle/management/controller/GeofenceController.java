package com.vehicle.management.controller;

import com.vehicle.management.model.Geofence;
import com.vehicle.management.model.rest.RestResponse;
import com.vehicle.management.service.GeofenceService;
import com.vehicle.management.transfer.GeofenceRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/geofence")
public class GeofenceController {

    private final GeofenceService geofenceService;

    @PostMapping
    public RestResponse createGeofence(@Valid @RequestBody GeofenceRequest geofenceRequest) {
        Geofence geofence = geofenceService.createGeofence(geofenceRequest);
        return new RestResponse(true, "Geofence created successfully");
    }

    @GetMapping
    public RestResponse getAllGeofence() {
        return new RestResponse(true, geofenceService.getAllGeofence());
    }

    @GetMapping("/{geofenceId}")
    public RestResponse getGeofence(@PathVariable Long geofenceId) {
        return new RestResponse(true, geofenceService.getGeofence(geofenceId));
    }

    @PutMapping("/{geofenceId}")
    public RestResponse updateGeofence(@PathVariable Long geofenceId, @Valid @RequestBody GeofenceRequest geofenceRequest) {
        return new RestResponse(true, geofenceService.updateGeofence(geofenceId, geofenceRequest));
    }

    @DeleteMapping("/{geofenceId}")
    public RestResponse deleteGeofence(@PathVariable Long geofenceId) {
        geofenceService.deleteGeofence(geofenceId);
        return new RestResponse(true, "Geofence deleted successfully");
    }


}
