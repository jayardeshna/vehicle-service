package com.vehicle.management.service;

import com.vehicle.management.exception.NotFoundException;
import com.vehicle.management.model.Vehicle;
import com.vehicle.management.repository.AlertRepository;
import com.vehicle.management.repository.GeofenceEventRepository;
import com.vehicle.management.repository.GeofenceRepository;
import com.vehicle.management.repository.VehicleRepository;
import com.vehicle.management.service.impl.VehicleServiceImpl;
import com.vehicle.management.transfer.VehiclePositionRequest;
import com.vehicle.management.transfer.VehicleTransfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Test_VehicleService {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GeofenceRepository geofenceRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private GeofenceEventRepository geofenceEventRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addVehicle() {
        VehicleTransfer vehicleTransfer = new VehicleTransfer();
        vehicleTransfer.setVehicleName("vehicle1");
        Vehicle vehicle = new Vehicle(vehicleTransfer.getVehicleName());

        vehicleService.addVehicle(vehicleTransfer);

        verify(vehicleRepository).save(vehicle);
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    void getVehicles() {
        List<Vehicle> vehicles = Arrays.asList(new Vehicle("Vehicle1"), new Vehicle("Vehicle2"));
        when(vehicleRepository.findAll()).thenReturn(vehicles);

        List<Vehicle> result = vehicleService.getVehicles();

        assertEquals(vehicles, result);
        verify(vehicleRepository).findAll();
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    void getVehicle_whenVehicleExists() {
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle("Vehicle1");
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getVehicle(vehicleId);

        assertEquals(vehicle, result);
        verify(vehicleRepository).findById(vehicleId);
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    void getVehicle_whenVehicleNotFound() {
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.getVehicle(vehicleId));

        verify(vehicleRepository).findById(vehicleId);
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    void deleteVehicle_whenVehicleExists() {
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle("Vehicle1");
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(vehicleId);

        verify(vehicleRepository).findById(vehicleId);
        verify(vehicleRepository).delete(vehicle);
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    void deleteVehicle_whenVehicleNotFound() {
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.deleteVehicle(vehicleId));

        verify(vehicleRepository).findById(vehicleId);
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    void updateVehicle_whenVehicleExists() {
        Long vehicleId = 1L;
        Vehicle existingVehicle = new Vehicle("Vehicle1");
        VehicleTransfer vehicleTransfer = new VehicleTransfer();
        vehicleTransfer.setVehicleName("UpdatedVehicle");
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));

        vehicleService.updateVehicle(vehicleId, vehicleTransfer);

        assertEquals("UpdatedVehicle", existingVehicle.getName());
        verify(vehicleRepository).findById(vehicleId);
        verify(vehicleRepository).save(existingVehicle);
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    void updateVehicle_whenVehicleNotFound() {
        Long vehicleId = 1L;
        VehicleTransfer vehicleTransfer = new VehicleTransfer();
        vehicleTransfer.setVehicleName("UpdatedVehicle");
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.updateVehicle(vehicleId, vehicleTransfer));

        verify(vehicleRepository).findById(vehicleId);
        verifyNoMoreInteractions(vehicleRepository);
    }

}
