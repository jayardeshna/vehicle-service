package com.vehicle.management.service;

import com.vehicle.management.model.Vehicle;
import com.vehicle.management.transfer.VehiclePositionRequest;
import com.vehicle.management.transfer.VehicleTransfer;

import java.util.List;

public interface VehicleService {
    void addVehicle(VehicleTransfer vehicleTransfer);

    List<Vehicle> getVehicles();

    Vehicle getVehicle(Long id);

    void deleteVehicle(Long id);

    void updateVehicle(Long id, VehicleTransfer vehicleTransfer);

    void updateVehiclePosition(VehiclePositionRequest vehiclePositionRequest);

}
