package com.vehicle.management.controller;

import com.vehicle.management.model.rest.RestResponse;
import com.vehicle.management.service.VehicleService;
import com.vehicle.management.transfer.VehiclePositionRequest;
import com.vehicle.management.transfer.VehicleTransfer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("/api/v1/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    RestResponse addVehicle(@RequestBody VehicleTransfer vehicleTransfer){
        vehicleService.addVehicle(vehicleTransfer);
        return new RestResponse(true, "Vehicle added successfully");
    }

    @PutMapping("/{id}")
    RestResponse updateVehicle(@PathVariable("id") Long id,@RequestBody VehicleTransfer vehicleTransfer){
        vehicleService.updateVehicle(id, vehicleTransfer);
        return new RestResponse(true, "Vehicle added successfully");
    }

    @GetMapping
    RestResponse getVehicles(){
        return new RestResponse(true, vehicleService.getVehicles());
    }

    @GetMapping("/{id}")
    RestResponse getVehicle(@PathVariable("id") Long id){
        return new RestResponse(true, vehicleService.getVehicle(id));
    }

    @DeleteMapping("/{id}")
    RestResponse deleteVehicle(@PathVariable("id") Long id){
        vehicleService.deleteVehicle(id);
        return new RestResponse(true, "Vehicle deleted successfully");
    }

    @PostMapping("/vehicle-positions")
    RestResponse updateVehiclePosition(@RequestBody VehiclePositionRequest vehiclePositionRequest){
        vehicleService.updateVehiclePosition(vehiclePositionRequest);
        return new RestResponse(true, "vehicle position updated successfully");
    }

}
