package com.vehicle.management.service.impl;

import com.vehicle.management.exception.NotFoundException;
import com.vehicle.management.model.Geofence;
import com.vehicle.management.repository.GeofenceRepository;
import com.vehicle.management.service.GeofenceService;
import com.vehicle.management.service.VehicleService;
import com.vehicle.management.transfer.GeofenceRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class GeofenceServiceImpl implements GeofenceService {

    private final GeofenceRepository geofenceRepository;

    private final VehicleService vehicleService;

    @Transactional
    @Override
    public Geofence createGeofence(GeofenceRequest geofenceRequest) {
        if (geofenceRepository.findByName(geofenceRequest.getName()) != null) {
            throw new NotFoundException(NotFoundException.NotFoundType.GEOFENCE_NOT_FOUND);
        }
        Geofence geofence = new Geofence();
        geofence.setName(geofenceRequest.getName());
        geofence.setCoordinates(geofenceRequest.getCoordinates());
        geofence.setAuthorizedVehicles(
                geofenceRequest.getAuthorizedVehicles() != null
                        ? geofenceRequest.getAuthorizedVehicles()
                        : new ArrayList<>()
        );
        return geofenceRepository.save(geofence);
    }

    @Transactional
    public Geofence updateGeofence(Long id, GeofenceRequest geofenceRequest) {
        Geofence existingGeofence = getGeofence(id);

        existingGeofence.setName(geofenceRequest.getName());
        existingGeofence.setCoordinates(geofenceRequest.getCoordinates());
        existingGeofence.setAuthorizedVehicles(geofenceRequest.getAuthorizedVehicles());

        return geofenceRepository.save(existingGeofence);
    }

    @Override
    public List<Geofence> getAllGeofence() {
        return geofenceRepository.findAll();
    }

    @Override
    public void deleteGeofence(Long geofenceId) {
        Geofence geofence = getGeofence(geofenceId);
        geofenceRepository.delete(geofence);
    }

    @Override
    public Geofence getGeofence(Long geofenceId) {
        return geofenceRepository.findById(geofenceId).orElseThrow(()->
                new NotFoundException(NotFoundException.NotFoundType.GEOFENCE_NOT_FOUND));
    }
}
