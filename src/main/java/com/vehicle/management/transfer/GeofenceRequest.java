package com.vehicle.management.transfer;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

@Data
public class GeofenceRequest {

    private String name;
    private Geometry polygon;
    private List<Long> authorizedVehicleIds;

}
