package com.vehicle.management.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

@Data
public class GeofenceRequest {

    @NotBlank(message = "Geofence name is required")
    private String name;

    @Size(min = 3, message = "At least 3 coordinates are required")
    private List<String> coordinates;

    private List<String> authorizedVehicles;

}
