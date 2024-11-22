package com.vehicle.management.service;

import com.vehicle.management.exception.NotFoundException;
import com.vehicle.management.model.Geofence;
import com.vehicle.management.repository.GeofenceRepository;
import com.vehicle.management.service.VehicleService;
import com.vehicle.management.service.impl.GeofenceServiceImpl;
import com.vehicle.management.transfer.GeofenceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Test_GeofenceService {

    @Mock
    private GeofenceRepository geofenceRepository;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private GeofenceServiceImpl geofenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGeofence_shouldCreateGeofenceSuccessfully() {
        GeofenceRequest request = new GeofenceRequest();
        request.setName("Test Geofence");
        request.setCoordinates(Arrays.asList("10.0,20.0", "15.0,25.0"));
        request.setAuthorizedVehicles(Arrays.asList("Vehicle1", "Vehicle2"));

        when(geofenceRepository.findByName(request.getName())).thenReturn(null);
        when(geofenceRepository.save(any(Geofence.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Geofence result = geofenceService.createGeofence(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getCoordinates(), result.getCoordinates());
        assertEquals(request.getAuthorizedVehicles(), result.getAuthorizedVehicles());
        verify(geofenceRepository).save(any(Geofence.class));
    }

    @Test
    void createGeofence_shouldThrowExceptionWhenGeofenceAlreadyExists() {
        GeofenceRequest request = new GeofenceRequest();
        request.setName("Existing Geofence");

        when(geofenceRepository.findByName(request.getName())).thenReturn(new Geofence());

        assertThrows(NotFoundException.class, () -> geofenceService.createGeofence(request));
        verify(geofenceRepository, never()).save(any());
    }

    @Test
    void updateGeofence_shouldUpdateExistingGeofence() {
        Long geofenceId = 1L;
        Geofence existingGeofence = new Geofence();
        existingGeofence.setId(geofenceId);
        existingGeofence.setName("Old Geofence");

        GeofenceRequest request = new GeofenceRequest();
        request.setName("Updated Geofence");
        request.setCoordinates(Arrays.asList("10.0,20.0", "15.0,25.0"));
        request.setAuthorizedVehicles(Arrays.asList("Vehicle1"));

        when(geofenceRepository.findById(geofenceId)).thenReturn(Optional.of(existingGeofence));
        when(geofenceRepository.save(any(Geofence.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Geofence updatedGeofence = geofenceService.updateGeofence(geofenceId, request);

        assertNotNull(updatedGeofence);
        assertEquals(request.getName(), updatedGeofence.getName());
        assertEquals(request.getCoordinates(), updatedGeofence.getCoordinates());
        assertEquals(request.getAuthorizedVehicles(), updatedGeofence.getAuthorizedVehicles());
        verify(geofenceRepository).save(existingGeofence);
    }

    @Test
    void updateGeofence_shouldThrowExceptionWhenGeofenceNotFound() {
        Long geofenceId = 1L;
        GeofenceRequest request = new GeofenceRequest();
        request.setName("Updated Geofence");

        when(geofenceRepository.findById(geofenceId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> geofenceService.updateGeofence(geofenceId, request));
        verify(geofenceRepository, never()).save(any());
    }

    @Test
    void getAllGeofence_shouldReturnListOfGeofences() {
        List<Geofence> geofences = Arrays.asList(new Geofence(), new Geofence());
        when(geofenceRepository.findAll()).thenReturn(geofences);

        List<Geofence> result = geofenceService.getAllGeofence();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(geofenceRepository).findAll();
    }

    @Test
    void deleteGeofence_shouldDeleteGeofenceSuccessfully() {
        Long geofenceId = 1L;
        Geofence geofence = new Geofence();
        geofence.setId(geofenceId);

        when(geofenceRepository.findById(geofenceId)).thenReturn(Optional.of(geofence));
        doNothing().when(geofenceRepository).delete(geofence);

        geofenceService.deleteGeofence(geofenceId);

        verify(geofenceRepository).delete(geofence);
    }

    @Test
    void deleteGeofence_shouldThrowExceptionWhenGeofenceNotFound() {
        Long geofenceId = 1L;
        when(geofenceRepository.findById(geofenceId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> geofenceService.deleteGeofence(geofenceId));
        verify(geofenceRepository, never()).delete(any());
    }

    @Test
    void getGeofence_shouldReturnGeofenceWhenFound() {
        Long geofenceId = 1L;
        Geofence geofence = new Geofence();
        geofence.setId(geofenceId);

        when(geofenceRepository.findById(geofenceId)).thenReturn(Optional.of(geofence));

        Geofence result = geofenceService.getGeofence(geofenceId);

        assertNotNull(result);
        assertEquals(geofenceId, result.getId());
        verify(geofenceRepository).findById(geofenceId);
    }

    @Test
    void getGeofence_shouldThrowExceptionWhenGeofenceNotFound() {
        Long geofenceId = 1L;

        when(geofenceRepository.findById(geofenceId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> geofenceService.getGeofence(geofenceId));
        verify(geofenceRepository).findById(geofenceId);
    }




}
