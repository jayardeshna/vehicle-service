package com.vehicle.management.service.impl;

import com.vehicle.management.model.GeofenceEvent;
import com.vehicle.management.repository.GeofenceEventRepository;
import com.vehicle.management.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final GeofenceEventRepository geofenceEventRepository;

    @Override
    public List<GeofenceEvent> getEventsWithFilter(String vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        if (vehicleId != null && startDate != null && endDate != null) {
            return geofenceEventRepository.findByVehicleIdAndEntryTimeBetween(vehicleId, startDate, endDate);
        } else if (vehicleId != null) {
            return geofenceEventRepository.findByVehicleId(vehicleId);
        } else if (startDate != null && endDate != null) {
            return geofenceEventRepository.findByEntryTimeBetween(startDate, endDate);
        } else {
            return geofenceEventRepository.findAll();
        }
    }
}
