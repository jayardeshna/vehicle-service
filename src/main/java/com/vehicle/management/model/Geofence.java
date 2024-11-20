package com.vehicle.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "geofences")
@Entity
public class Geofence {

    @Id  // Change to jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "polygon", nullable = false, columnDefinition = "geometry(Polygon, 4326)")
    private Geometry polygon;

}