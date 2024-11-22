package com.vehicle.management.service;
import com.vehicle.management.enums.AlertType;
import com.vehicle.management.model.Alert;
import com.vehicle.management.repository.AlertRepository;
import com.vehicle.management.service.impl.AlertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Test_AlertService {

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertServiceImpl alertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAlerts_withAllFilters() {
        String vehicleId = "V123";
        String geofenceName = "Geofence1";
        AlertType alertType = AlertType.UNAUTHORIZED;

        List<Alert> expectedAlerts = Arrays.asList(new Alert(), new Alert());
        when(alertRepository.findAlerts(vehicleId, geofenceName, alertType))
                .thenReturn(expectedAlerts);

        List<Alert> result = alertService.getAlerts(vehicleId, geofenceName, alertType);

        assertEquals(expectedAlerts, result);
        verify(alertRepository).findAlerts(vehicleId, geofenceName, alertType);
        verifyNoMoreInteractions(alertRepository);
    }

    @Test
    void getAlerts_withSomeFilters() {
        String vehicleId = "V123";
        String geofenceName = null;
        AlertType alertType = AlertType.UNAUTHORIZED;

        List<Alert> expectedAlerts = Collections.singletonList(new Alert());
        when(alertRepository.findAlerts(vehicleId, geofenceName, alertType))
                .thenReturn(expectedAlerts);

        List<Alert> result = alertService.getAlerts(vehicleId, geofenceName, alertType);

        assertEquals(expectedAlerts, result);
        verify(alertRepository).findAlerts(vehicleId, geofenceName, alertType);
        verifyNoMoreInteractions(alertRepository);
    }

    @Test
    void getAlerts_withNoFilters() {
        String vehicleId = null;
        String geofenceName = null;
        AlertType alertType = null;

        List<Alert> expectedAlerts = Arrays.asList(new Alert(), new Alert(), new Alert());
        when(alertRepository.findAlerts(vehicleId, geofenceName, alertType))
                .thenReturn(expectedAlerts);

        List<Alert> result = alertService.getAlerts(vehicleId, geofenceName, alertType);

        assertEquals(expectedAlerts, result);
        verify(alertRepository).findAlerts(vehicleId, geofenceName, alertType);
        verifyNoMoreInteractions(alertRepository);
    }


}
