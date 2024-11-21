package com.vehicle.management.controller;

import com.vehicle.management.enums.AlertType;
import com.vehicle.management.model.Alert;
import com.vehicle.management.model.rest.RestResponse;
import com.vehicle.management.service.AlertService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/alert")
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public RestResponse getAlerts(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String geofenceName,
            @RequestParam(required = false) AlertType alertType) {

        List<Alert> alerts = alertService.getAlerts(vehicleId, geofenceName, alertType);
        return new RestResponse(true, alerts);
    }
}

