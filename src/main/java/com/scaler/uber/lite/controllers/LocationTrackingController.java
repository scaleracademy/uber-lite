package com.scaler.uber.lite.controllers;

import com.scaler.uber.lite.exceptions.InvalidDriverException;
import com.scaler.uber.lite.exceptions.InvalidPassengerException;
import com.scaler.uber.lite.models.Driver;
import com.scaler.uber.lite.models.ExactLocation;
import com.scaler.uber.lite.models.Passenger;
import com.scaler.uber.lite.repositories.DriverRepository;
import com.scaler.uber.lite.repositories.PassengerRepository;
import com.scaler.uber.lite.service.LocationTrackingService;
import com.scaler.uber.lite.service.ServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationTrackingController {
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private LocationTrackingService locationTrackingService;
    @Autowired
    private ServiceConstants constants;

    public Driver getDriverFromId(Long driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if (!driver.isPresent()) {
            throw new InvalidDriverException("No driver with id " + driverId);
        }
        return driver.get();
    }

    @PutMapping("/driver/{driverId}")
    public void updateDriverLocation(@PathVariable Long driverId,
                                     @RequestBody ExactLocation data) {
        Driver driver = getDriverFromId(driverId);
        // todo: check if the driver has an active booking
        //       update the bookings completedRoute based on the driver's location
        //       update the expected completion time

        ExactLocation location = ExactLocation.builder()
                .longitude(data.getLongitude())
                .latitude(data.getLatitude())
                .build();
    }

    public Passenger getPassengerFromId(Long passengerId) {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if (!passenger.isPresent()) {
            throw new InvalidPassengerException("No passenger with id " + passengerId);
        }
        return passenger.get();
    }

    @PutMapping("/passenger/{passengerId}")
    public void updatePassengerLocation(@PathVariable Long passengerId,
                                        @RequestBody ExactLocation location) {
        // only triggers every 30 secs if the passenger is active
        Passenger passenger = getPassengerFromId(passengerId);
        passenger.setLastKnownLocation(ExactLocation.builder()
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build());
        passengerRepository.save(passenger);
    }
}
