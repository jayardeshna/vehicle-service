package com.vehicle.management.service;

import com.vehicle.management.model.GeofenceEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    List<GeofenceEvent> getEventsWithFilter(String vehicleId, LocalDateTime startDate, LocalDateTime endDate);
}
