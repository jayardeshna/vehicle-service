package com.vehicle.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "geofence")
@Entity
public class Geofence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ElementCollection
    private List<String> coordinates;

    @ElementCollection
    private List<String> authorizedVehicles;

}