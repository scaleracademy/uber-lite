package com.scaler.uber.lite.controllers;

import com.scaler.uber.lite.exceptions.InvalidBookingException;
import com.scaler.uber.lite.exceptions.InvalidDriverException;
import com.scaler.uber.lite.models.Booking;
import com.scaler.uber.lite.models.Driver;
import com.scaler.uber.lite.models.OTP;
import com.scaler.uber.lite.models.Review;
import com.scaler.uber.lite.repositories.BookingRepository;
import com.scaler.uber.lite.repositories.DriverRepository;
import com.scaler.uber.lite.repositories.ReviewRepository;
import com.scaler.uber.lite.service.BookingService;
import com.scaler.uber.lite.service.ServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/driver")
@RestController
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ServiceConstants constants;

    @GetMapping("/{driverId}")
    public Driver getDriverDetails(@PathVariable(name = "driverId") Long driverId) {
        return getDriverFromId(driverId);
    }

    @GetMapping("{driverId}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name = "driverId") Long driverId) {
        Driver driver = getDriverFromId(driverId);
        return driver.getBookings();
    }

    @GetMapping("{driverId}/bookings/{bookingId}")
    public Booking getBooking(@PathVariable(name = "driverId") Long driverId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        return getDriverBookingFromId(bookingId, driver);
    }

    @PostMapping("{driverId}/bookings/{bookingId}")
    public void acceptBooking(@PathVariable(name = "driverId") Long driverId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        Booking booking = getBookingFromId(bookingId);
        bookingService.acceptBooking(driver, booking);
    }

    @DeleteMapping("{driverId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "driverId") Long driverId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        bookingService.cancelByDriver(driver, booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/start")
    public void startRide(@PathVariable(name = "driverId") Long driverId,
                          @PathVariable(name = "bookingId") Long bookingId,
                          @RequestBody OTP otp) {
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        bookingService.startRide(booking, otp, constants.getRideStartOTPExpiryMinutes());
        bookingRepository.save(booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/end")
    public void endRide(@PathVariable(name = "driverId") Long driverId,
                        @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        bookingService.endRide(booking);
        driverRepository.save(driver);
        bookingRepository.save(booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "driverId") Long driverId,
                         @PathVariable(name = "bookingId") Long bookingId,
                         @RequestBody Review data) {
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        Review review = Review.builder()
                .note(data.getNote())
                .ratingOutOfFive(data.getRatingOutOfFive())
                .build();
        booking.setReviewByDriver(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }

    private Driver getDriverFromId(Long driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if (!driver.isPresent()) {
            throw new InvalidDriverException("No driver with id " + driverId);
        }
        return driver.get();
    }

    private Booking getBookingFromId(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (!booking.isPresent()) {
            throw new InvalidBookingException("No booking with id " + booking);
        }
        return booking.get();
    }

    private Booking getDriverBookingFromId(Long bookingId, Driver driver) {
        Booking booking = getBookingFromId(bookingId);
        if (!booking.getDriver().equals(driver)) {
            throw new InvalidBookingException("Driver " + driver.getBookings() + " has no such booking " + bookingId);
        }
        return booking;
    }
}