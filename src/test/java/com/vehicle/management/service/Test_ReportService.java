package com.vehicle.management.service;
import com.vehicle.management.model.GeofenceEvent;
import com.vehicle.management.repository.GeofenceEventRepository;
import com.vehicle.management.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Test_ReportService {

    @Mock
    private GeofenceEventRepository geofenceEventRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEventsWithFilter_withAllFilters() {
        String vehicleId = "V123";
        LocalDateTime startDate = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 10, 23, 59);

        List<GeofenceEvent> expectedEvents = Arrays.asList(new GeofenceEvent(), new GeofenceEvent());
        when(geofenceEventRepository.findByVehicleIdAndEntryTimeBetween(vehicleId, startDate, endDate))
                .thenReturn(expectedEvents);

        List<GeofenceEvent> result = reportService.getEventsWithFilter(vehicleId, startDate, endDate);

        assertEquals(expectedEvents, result);
        verify(geofenceEventRepository).findByVehicleIdAndEntryTimeBetween(vehicleId, startDate, endDate);
        verifyNoMoreInteractions(geofenceEventRepository);
    }

    @Test
    void getEventsWithFilter_withVehicleIdOnly() {
        String vehicleId = "V123";

        List<GeofenceEvent> expectedEvents = Collections.singletonList(new GeofenceEvent());
        when(geofenceEventRepository.findByVehicleId(vehicleId)).thenReturn(expectedEvents);

        List<GeofenceEvent> result = reportService.getEventsWithFilter(vehicleId, null, null);

        assertEquals(expectedEvents, result);
        verify(geofenceEventRepository).findByVehicleId(vehicleId);
        verifyNoMoreInteractions(geofenceEventRepository);
    }

    @Test
    void getEventsWithFilter_withDateRangeOnly() {
        LocalDateTime startDate = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 10, 23, 59);

        List<GeofenceEvent> expectedEvents = Arrays.asList(new GeofenceEvent(), new GeofenceEvent());
        when(geofenceEventRepository.findByEntryTimeBetween(startDate, endDate)).thenReturn(expectedEvents);

        List<GeofenceEvent> result = reportService.getEventsWithFilter(null, startDate, endDate);

        assertEquals(expectedEvents, result);
        verify(geofenceEventRepository).findByEntryTimeBetween(startDate, endDate);
        verifyNoMoreInteractions(geofenceEventRepository);
    }

    @Test
    void getEventsWithFilter_withNoFilters() {
        List<GeofenceEvent> expectedEvents = Arrays.asList(new GeofenceEvent(), new GeofenceEvent(), new GeofenceEvent());
        when(geofenceEventRepository.findAll()).thenReturn(expectedEvents);

        List<GeofenceEvent> result = reportService.getEventsWithFilter(null, null, null);

        assertEquals(expectedEvents, result);
        verify(geofenceEventRepository).findAll();
        verifyNoMoreInteractions(geofenceEventRepository);
    }

}
