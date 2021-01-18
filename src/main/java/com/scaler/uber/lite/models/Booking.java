package com.scaler.uber.lite.models;

/**
 * @author mohit@interviewbit.com on 05/01/21
 **/

import lombok.*;

import javax.persistence.*;
import java.util.*;


@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking", indexes = {
        @Index(columnList = "passenger_id"),
        @Index(columnList = "driver_id"),
})
public class Booking extends BaseEntity {
    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Driver driver;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Driver> notifiedDrivers = new HashSet<>(); // store which drivers can potentially accept this booking

    @Enumerated(value = EnumType.STRING)
    private BookingType bookingType;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    @OneToOne
    private Review reviewByPassenger;
    @OneToOne
    private Review reviewByDriver;
    private boolean isPaid;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = {@Index(columnList = "booking_id")}
    )
    @OrderColumn(name = "location_index")
    private List<ExactLocation> route = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_completed_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = {@Index(columnList = "booking_id")}
    )
    @OrderColumn(name = "location_index")
    private List<ExactLocation> completedRoute = new ArrayList<>();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime; // actual start time

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime; // actual end time

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expectedCompletionTime; // filled by the location Tracking service

    private Long totalDistanceMeters; // also be tracked by the location tracking service

    @OneToOne
    private OTP rideStartOTP; // a cron job deleted otps of completed rides
}

