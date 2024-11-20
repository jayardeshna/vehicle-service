package com.vehicle.management.service.impl;

import com.vehicle.management.exception.NotFoundException;
import com.vehicle.management.model.Geofence;
import com.vehicle.management.model.GeofenceVehicle;
import com.vehicle.management.repository.GeofenceRepository;
import com.vehicle.management.repository.GeofenceVehicleRepository;
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

    private final GeofenceVehicleRepository geofenceVehicleRepository;

    private final VehicleService vehicleService;

    @Transactional
    @Override
    public Geofence createGeofence(GeofenceRequest geofenceRequest) {
        Geofence geofence = new Geofence();
        geofence.setName(geofenceRequest.getName());
        geofence.setPolygon(geofenceRequest.getPolygon());
        Geofence savedGeofence = geofenceRepository.save(geofence);
        saveGeofenceVehicle(geofenceRequest.getAuthorizedVehicleIds(), savedGeofence);
        return savedGeofence;
    }

    private void saveGeofenceVehicle(List<Long> authorizedVehicleIds, Geofence geofence) {
        List<GeofenceVehicle> geofenceVehicles = new ArrayList<>();
        for (Long vehicleId : authorizedVehicleIds) {
            GeofenceVehicle geofenceVehicle = new GeofenceVehicle();
            geofenceVehicle.setGeofence(geofence);
            geofenceVehicle.setVehicle(vehicleService.getVehicle(vehicleId));
            geofenceVehicles.add(geofenceVehicle);
        }
        geofenceVehicleRepository.saveAll(geofenceVehicles);
    }

    @Override
    public List<Geofence> getAllGeofences() {
        return geofenceRepository.findAll();
    }

    @Transactional
    @Override
    public void updateGeofence(Long geofenceId, GeofenceRequest geofenceRequest) {
        Geofence currentGeofence = getGeofence(geofenceId);
        currentGeofence.setName(geofenceRequest.getName());
        currentGeofence.setPolygon(geofenceRequest.getPolygon());
        geofenceRepository.save(currentGeofence);
        saveGeofenceVehicle(geofenceRequest.getAuthorizedVehicleIds(), currentGeofence);
    }

    @Override
    public Geofence getGeofence(Long geofenceId) {
        return geofenceRepository.findById(geofenceId).orElseThrow(()->
                new NotFoundException(NotFoundException.NotFoundType.GEOFENCE_NOT_FOUND));
    }
}
