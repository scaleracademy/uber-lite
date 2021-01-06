package com.scaler.uber.lite.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author mohit@interviewbit.com on 05/01/21
 **/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exactlocation")
public class ExactLocation extends BaseEntity {
    private Double latitude;
    private Double longitude;

    public double distanceKm(ExactLocation other) {
        final Double R = 6371e3; // metres
        if ((latitude.equals(other.getLatitude())) && (longitude.equals(other.getLongitude()))) {
            return 0;
        }
        double theta = longitude - other.longitude;
        double dist = Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(other.latitude)) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(other.latitude)) * Math.cos(Math.toRadians(theta));
        return Math.toDegrees(Math.acos(dist)) * 60 * 1.85316;
    }
}
