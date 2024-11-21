package com.vehicle.management.service;

import com.vehicle.management.enums.AlertType;
import com.vehicle.management.model.Alert;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertService {

    List<Alert> getAlerts(String vehicleId, String geofenceName, AlertType alertType);

}
