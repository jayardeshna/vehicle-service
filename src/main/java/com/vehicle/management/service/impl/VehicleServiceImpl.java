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

    }

}
