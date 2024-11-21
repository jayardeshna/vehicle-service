package com.vehicle.management.controller;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.vehicle.management.model.GeofenceEvent;
import com.vehicle.management.model.rest.RestResponse;
import com.vehicle.management.repository.GeofenceEventRepository;
import com.vehicle.management.service.ReportService;
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
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/historical")
    public RestResponse getHistoricalReport(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return new RestResponse(true, reportService.getEventsWithFilter(vehicleId, startDate, endDate));
    }


}
