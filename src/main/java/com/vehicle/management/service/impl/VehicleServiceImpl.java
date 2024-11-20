package com.vehicle.management.service.impl;

import com.vehicle.management.enums.AlertType;
import com.vehicle.management.exception.NotFoundException;
import com.vehicle.management.model.*;
import com.vehicle.management.repository.*;
import com.vehicle.management.service.VehicleService;
import com.vehicle.management.transfer.VehiclePositionRequest;
import com.vehicle.management.transfer.VehicleTransfer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    private final GeofenceRepository geofenceRepository;

    private final GeofenceVehicleRepository geofenceVehicleRepository;

    private final AlertRepository alertRepository;

    private final GeofenceEventRepository geofenceEventRepository;

    @Override
    public void addVehicle(VehicleTransfer vehicleTransfer) {
        Vehicle vehicle = new Vehicle(vehicleTransfer.getVehicleName());
        vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id).orElseThrow(()->new NotFoundException(NotFoundException.NotFoundType.VEHICLE_NOT_FOUND));
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicle(id);
        vehicleRepository.delete(vehicle);
    }

    @Override
    public void updateVehicle(Long id, VehicleTransfer vehicleTransfer) {
        Vehicle currVehicle = getVehicle(id);
        currVehicle.setName(vehicleTransfer.getVehicleName());
        vehicleRepository.save(currVehicle);
    }

    @Override
    public void updateVehiclePosition(VehiclePositionRequest vehiclePositionRequest) {
        Optional<Geofence> geofenceOptional = geofenceRepository.findGeofenceByLocation(vehiclePositionRequest.getLatitude(), vehiclePositionRequest.getLongitude());
        if (geofenceOptional.isPresent()){
            Geofence geofence = geofenceOptional.get();
            Optional<GeofenceVehicle> geofenceVehicle = geofenceVehicleRepository.findByGeofenceAndVehicleId(geofence.getId(), vehiclePositionRequest.getVehicleId());
            boolean isAuthorized = geofenceVehicle.isPresent();
            Vehicle vehicle = getVehicle(vehiclePositionRequest.getVehicleId());
            if (!isAuthorized) {
                // Log an unauthorized entry alert
                Alert alert = new Alert();
                alert.setVehicle(vehicle);
                alert.setGeofence(geofence);
                alert.setAlertType(AlertType.UNAUTHORIZED);
                alert.setTimestamp(vehiclePositionRequest.getTimestamp());
                alertRepository.save(alert);
            }

            Optional<GeofenceEvent> ongoingEvent = geofenceEventRepository.findOngoingEvent(vehiclePositionRequest.getVehicleId(), geofence.getId());

            if (ongoingEvent.isEmpty()) {
                // Record a new entry
                GeofenceEvent geofenceEvent = new GeofenceEvent();
                geofenceEvent.setVehicle(vehicle);
                geofenceEvent.setGeofence(geofence);
                geofenceEvent.setEntryTime(vehiclePositionRequest.getTimestamp());
                geofenceEvent.setIsAuthorized(isAuthorized);
                geofenceEventRepository.save(geofenceEvent);
            }


        } else {
            Optional<GeofenceEvent> ongoingEvent = geofenceEventRepository.findOngoingEventByVehicle(vehiclePositionRequest.getVehicleId());
            if (ongoingEvent.isPresent()) {
                GeofenceEvent geofenceEvent = ongoingEvent.get();
                geofenceEvent.setExitTime(vehiclePositionRequest.getTimestamp());
                geofenceEventRepository.save(geofenceEvent);
                Duration duration = Duration.between(geofenceEvent.getEntryTime(), vehiclePositionRequest.getTimestamp());
                log.info("Vehicle exited geofence. Duration:{}", duration.toMinutes());
            }
        }

    }
}
