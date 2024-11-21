package com.vehicle.management.service;

import com.vehicle.management.enums.AlertType;
import com.vehicle.management.model.Alert;
import com.vehicle.management.model.Geofence;
import com.vehicle.management.model.GeofenceEvent;
import com.vehicle.management.repository.AlertRepository;
import com.vehicle.management.repository.GeofenceEventRepository;
import com.vehicle.management.repository.GeofenceRepository;
import com.vehicle.management.service.impl.PositionTrackingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Test_PositionTrackingService {

    @InjectMocks
    private PositionTrackingServiceImpl positionTrackingService;

    @Mock
    private GeofenceRepository geofenceRepository;

    @Mock
    private GeofenceEventRepository eventRepository;

    @Mock
    private AlertRepository alertRepository;

    private Map<String, GeofenceEvent> activeGeofenceEntries;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activeGeofenceEntries = new ConcurrentHashMap<>();
        positionTrackingService = new PositionTrackingServiceImpl(
                geofenceRepository, eventRepository, alertRepository, activeGeofenceEntries);
    }

    @Test
    void testProcessVehiclePosition_InsideGeofence_Authorized() {
        String vehicleId = "V123";
        double latitude = 12.34;
        double longitude = 56.78;
        LocalDateTime timestamp = LocalDateTime.now();

        Geofence geofence = new Geofence();
        geofence.setName("TestGeofence");
        geofence.setAuthorizedVehicles(Arrays.asList("V123", "V456"));
        geofence.setCoordinates(Arrays.asList("10,50", "15,50", "15,60", "10,60"));

        when(geofenceRepository.findAll()).thenReturn(Collections.singletonList(geofence));

        positionTrackingService.processVehiclePosition(vehicleId, latitude, longitude, timestamp);

        verify(eventRepository, times(1)).save(any(GeofenceEvent.class));
        verify(alertRepository, never()).save(any(Alert.class));
    }

    @Test
    void testProcessVehiclePosition_InsideGeofence_Unauthorized() {
        String vehicleId = "V789";
        double latitude = 12.34;
        double longitude = 56.78;
        LocalDateTime timestamp = LocalDateTime.now();

        Geofence geofence = new Geofence();
        geofence.setName("TestGeofence");
        geofence.setAuthorizedVehicles(Arrays.asList("V123", "V456"));
        geofence.setCoordinates(Arrays.asList("10,50", "15,50", "15,60", "10,60"));

        when(geofenceRepository.findAll()).thenReturn(Collections.singletonList(geofence));

        positionTrackingService.processVehiclePosition(vehicleId, latitude, longitude, timestamp);

        verify(eventRepository, times(1)).save(any(GeofenceEvent.class));
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void testProcessVehiclePosition_ExitGeofence() {
        String vehicleId = "V123";
        LocalDateTime entryTime = LocalDateTime.now().minusMinutes(30);
        LocalDateTime exitTime = LocalDateTime.now();

        GeofenceEvent existingEvent = new GeofenceEvent();
        existingEvent.setVehicleId(vehicleId);
        existingEvent.setGeofenceName("TestGeofence");
        existingEvent.setEntryTime(entryTime);

        activeGeofenceEntries.put(vehicleId, existingEvent);

        positionTrackingService.processVehiclePosition(vehicleId, 0, 0, exitTime);

        verify(eventRepository, times(1)).save(existingEvent);
        assertFalse(activeGeofenceEntries.containsKey(vehicleId));
        assertEquals(30, existingEvent.getStayDuration());
    }

    @Test
    void testIsPointInPolygon_PointInsidePolygon() {
        List<String> polygonCoordinates = Arrays.asList(
                "10,10", "20,10", "20,20", "10,20"
        );

        boolean result = positionTrackingService.isPointInPolygon(15, 15, polygonCoordinates);
        assertTrue(result);
    }

    @Test
    void testIsPointInPolygon_PointOutsidePolygon() {
        List<String> polygonCoordinates = Arrays.asList(
                "10,10", "20,10", "20,20", "10,20"
        );

        boolean result = positionTrackingService.isPointInPolygon(25, 25, polygonCoordinates);
        assertFalse(result);
    }

    @Test
    void testCheckVehicleAuthorization_Authorized() {
        Geofence geofence = new Geofence();
        geofence.setAuthorizedVehicles(Arrays.asList("V123", "V456"));

        boolean result = positionTrackingService.checkVehicleAuthorization("V123", geofence);
        assertTrue(result);
    }

    @Test
    void testCheckVehicleAuthorization_Unauthorized() {
        Geofence geofence = new Geofence();
        geofence.setAuthorizedVehicles(Arrays.asList("V123", "V456"));

        boolean result = positionTrackingService.checkVehicleAuthorization("V789", geofence);
        assertFalse(result);
    }

    @Test
    void testHandleGeofenceEntry_NewEntry() {
        String vehicleId = "V123";
        LocalDateTime timestamp = LocalDateTime.now();

        Geofence geofence = new Geofence();
        geofence.setName("TestGeofence");
        geofence.setAuthorizedVehicles(Arrays.asList("V123", "V456"));

        positionTrackingService.handleGeofenceEntry(vehicleId, geofence, timestamp);

        verify(eventRepository, times(1)).save(any(GeofenceEvent.class));
        assertTrue(activeGeofenceEntries.containsKey(vehicleId));
    }

    @Test
    void testHandleGeofenceExit() {
        LocalDateTime entryTime = LocalDateTime.now().minusMinutes(30);
        LocalDateTime exitTime = LocalDateTime.now();

        GeofenceEvent geofenceEvent = new GeofenceEvent();
        geofenceEvent.setEntryTime(entryTime);
        geofenceEvent.setVehicleId("V123");

        positionTrackingService.handleGeofenceExit(geofenceEvent, exitTime);

        verify(eventRepository, times(1)).save(geofenceEvent);
        assertEquals(30, geofenceEvent.getStayDuration());
    }

    @Test
    void testRaiseUnauthorizedAlert() {
        GeofenceEvent event = new GeofenceEvent();
        event.setVehicleId("V123");
        event.setGeofenceName("TestGeofence");

        positionTrackingService.raiseUnauthorizedAlert(event);

        verify(alertRepository, times(1)).save(any(Alert.class));
    }

}
