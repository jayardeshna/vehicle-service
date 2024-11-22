package com.vehicle.management.service.impl;

import com.vehicle.management.enums.AlertType;
import com.vehicle.management.model.Alert;
import com.vehicle.management.model.Geofence;
import com.vehicle.management.model.GeofenceEvent;
import com.vehicle.management.repository.AlertRepository;
import com.vehicle.management.repository.GeofenceEventRepository;
import com.vehicle.management.repository.GeofenceRepository;
import com.vehicle.management.service.PositionTrackingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
@Service
public class PositionTrackingServiceImpl implements PositionTrackingService {

    private final GeofenceRepository geofenceRepository;

    private final GeofenceEventRepository eventRepository;

    private final AlertRepository alertRepository;

    private Map<String, GeofenceEvent> activeGeofenceEntries = new ConcurrentHashMap<>();

    public void processVehiclePosition(String vehicleId, double latitude, double longitude, LocalDateTime timestamp) {

        Geofence matchingGeofence = findGeofenceContainingPoint(latitude, longitude);

        GeofenceEvent previousEntry = activeGeofenceEntries.get(vehicleId);

        if (matchingGeofence != null) {
            handleGeofenceEntry(vehicleId, matchingGeofence, timestamp);
        } else if (previousEntry != null) {
            handleGeofenceExit(previousEntry, timestamp);
        }
    }

    public void handleGeofenceEntry(String vehicleId, Geofence geofence, LocalDateTime timestamp) {
        GeofenceEvent existingEntry = activeGeofenceEntries.get(vehicleId);

        if (existingEntry == null || !existingEntry.getGeofenceName().equals(geofence.getName())) {
            boolean isAuthorized = checkVehicleAuthorization(vehicleId, geofence);

            GeofenceEvent newEntry = new GeofenceEvent();
            newEntry.setVehicleId(vehicleId);
            newEntry.setGeofenceName(geofence.getName());
            newEntry.setEntryTime(timestamp);
            newEntry.setAuthorized(isAuthorized);
            if (isAuthorized) {
                newEntry.setAlertType(AlertType.AUTHORIZED);
            } else {
                newEntry.setAlertType(AlertType.UNAUTHORIZED);
            }
            eventRepository.save(newEntry);
            activeGeofenceEntries.put(vehicleId, newEntry);

            if (!isAuthorized) {
                raiseUnauthorizedAlert(newEntry);
            }
        }
    }

    public void handleGeofenceExit(GeofenceEvent previousEntry, LocalDateTime exitTimestamp) {
        Duration stayDuration = Duration.between(previousEntry.getEntryTime(), exitTimestamp);
        previousEntry.setExitTime(exitTimestamp);
        previousEntry.setStayDuration(stayDuration.toMinutes());
        eventRepository.save(previousEntry);
        activeGeofenceEntries.remove(previousEntry.getVehicleId());
    }

    public void raiseUnauthorizedAlert(GeofenceEvent event) {
        alertRepository.save(new Alert(
                event.getVehicleId(),
                event.getGeofenceName(),
                LocalDateTime.now(),
                AlertType.UNAUTHORIZED
        ));
    }

    private Geofence findGeofenceContainingPoint(double latitude, double longitude) {
        log.info("\nSearching for geofence containing point: {},{} " ,latitude ,longitude);

        List<Geofence> geofences = geofenceRepository.findAll();
        log.info("Found geofences: {}", geofences.size());

        for (Geofence geofence : geofences) {
            log.info("Checking geofence: {}", geofence.getName());

            if (isPointInPolygon(latitude, longitude, geofence.getCoordinates())) {
                log.info("Point is inside this geofence!");
                return geofence;
            }
        }
        log.info("Point is not inside any geofence");
        return null;
    }

    //Ray-casting algorithm
    private boolean isPointInBoundingBox(double latitude, double longitude, List<String> polygonCoordinates) {
        if (polygonCoordinates == null || polygonCoordinates.isEmpty()) {
            return false;
        }

        double minLat = Double.MAX_VALUE;
        double maxLat = -Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = -Double.MAX_VALUE;

        for (String coord : polygonCoordinates) {
            String[] parts = coord.split(",");
            if (parts.length != 2) {
                continue;
            }
            try {
                double lat = Double.parseDouble(parts[0].trim());
                double lon = Double.parseDouble(parts[1].trim());

                minLat = Math.min(minLat, lat);
                maxLat = Math.max(maxLat, lat);
                minLon = Math.min(minLon, lon);
                maxLon = Math.max(maxLon, lon);
            } catch (NumberFormatException e) {
                log.error("Error parsing coordinate: {}", coord);
            }
        }

        return latitude >= minLat && latitude <= maxLat &&
                longitude >= minLon && longitude <= maxLon;
    }


    //Ray-casting algorithm
    public boolean isPointInPolygon(double latitude, double longitude, List<String> polygonCoordinates) {
        System.out.println("\nDebug: Point-in-polygon check for " + latitude + "," + longitude);
        int i;
        int j;
        boolean result = false;
        int sides = polygonCoordinates.size();

        System.out.println("Number of polygon sides: " + sides);

        for (i = 0, j = sides - 1; i < sides; j = i++) {
            String[] coordI = polygonCoordinates.get(i).split(",");
            String[] coordJ = polygonCoordinates.get(j).split(",");

            double latI = Double.parseDouble(coordI[0]);
            double lonI = Double.parseDouble(coordI[1]);
            double latJ = Double.parseDouble(coordJ[0]);
            double lonJ = Double.parseDouble(coordJ[1]);

            System.out.println("Checking edge from " + latJ + "," + lonJ + " to " + latI + "," + lonI);

            if ((lonI < longitude && lonJ >= longitude) || (lonJ < longitude && lonI >= longitude)) {
                if (latI + (longitude - lonI) / (lonJ - lonI) * (latJ - latI) < latitude) {
                    result = !result;
                    log.info("Intersection found, current result: {}", result);
                }
            }
        }

        System.out.println("Final result: " + result);
        return result;
    }

    private Point parseCoordinate(String coordinate) {
        String[] parts = coordinate.split(",");
        return new Point(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1])
        );
    }

    private static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean checkVehicleAuthorization(String vehicleId, Geofence geofence) {
        return geofence.getAuthorizedVehicles().contains(vehicleId);
    }

    private GeofenceEvent createOrUpdateGeofenceEvent(String vehicleId, String geofenceName, boolean authorized, LocalDateTime timestamp) {
        GeofenceEvent event = new GeofenceEvent();
        event.setVehicleId(vehicleId);
        event.setGeofenceName(geofenceName);
        event.setAuthorized(authorized);
        event.setEntryTime(timestamp);
        return eventRepository.save(event);
    }


}
