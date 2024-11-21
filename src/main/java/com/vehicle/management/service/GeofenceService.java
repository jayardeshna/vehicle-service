package com.vehicle.management.service;

import com.vehicle.management.model.Geofence;
import com.vehicle.management.transfer.GeofenceRequest;

import java.util.List;

public interface GeofenceService {
    Geofence createGeofence(GeofenceRequest geofenceRequest);

    List<Geofence> getAllGeofence();

    Geofence updateGeofence(Long geofenceId, GeofenceRequest geofenceRequest);

    Geofence getGeofence(Long geofenceId);

    void deleteGeofence(Long geofenceId);
}
