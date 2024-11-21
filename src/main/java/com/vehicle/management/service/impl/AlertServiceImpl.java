package com.vehicle.management.service.impl;

import com.vehicle.management.enums.AlertType;
import com.vehicle.management.model.Alert;
import com.vehicle.management.repository.AlertRepository;
import com.vehicle.management.service.AlertService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@AllArgsConstructor
@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    @Override
    public List<Alert> getAlerts(String vehicleId, String geofenceName, AlertType alertType) {
        return alertRepository.findAlerts(vehicleId, geofenceName, alertType);
    }

}
