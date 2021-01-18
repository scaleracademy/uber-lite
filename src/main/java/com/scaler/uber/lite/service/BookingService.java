package com.scaler.uber.lite.service;

import com.scaler.uber.lite.exceptions.InvalidActionForBookingStateException;
import com.scaler.uber.lite.exceptions.InvalidBookingException;
import com.scaler.uber.lite.exceptions.InvalidOTPException;
import com.scaler.uber.lite.models.*;
import com.scaler.uber.lite.repositories.BookingRepository;
import com.scaler.uber.lite.repositories.DriverRepository;
import com.scaler.uber.lite.repositories.PassengerRepository;
import com.scaler.uber.lite.service.otp.OTPService;
import com.scaler.uber.lite.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private OTPService otpService;
    @Autowired
    private ServiceConstants serviceConstants;
    @Autowired
    private DriverRepository driverRepository;

    public void createBooking(Booking booking) {
        if (booking.getStartTime().after(new Date())) {
            throw new InvalidBookingException("Scheduled Rides are not supported");
        }
        booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
        otpService.sendRideStartOTP(booking.getRideStartOTP());
        bookingRepository.save(booking);
        passengerRepository.save(booking.getPassenger());
    }

    public void acceptBooking(Driver driver, Booking booking) {
        if (!booking.getBookingStatus().equals(BookingStatus.ASSIGNING_DRIVER)) {
            return;
        }
        if (!canAcceptBooking(driver, serviceConstants.getMaxWaitTimeForPreviousRide())) {
            return;
        }
        booking.setDriver(driver);
        driver.setActiveBooking(booking);
        booking.getNotifiedDrivers().clear();
        driver.getAcceptableBookings().clear();
        bookingRepository.save(booking);
        driverRepository.save(driver);
    }

    public void cancelByDriver(Driver driver, Booking booking) {
        booking.setDriver(null);
        driver.setActiveBooking(null);
        driver.getAcceptableBookings().remove(booking);
        retryBooking(booking);
    }

    public void cancelByPassenger(Passenger passenger, Booking booking) {
        try {
            cancel(booking);
            bookingRepository.save(booking);
        } catch (InvalidActionForBookingStateException inner) {
            throw inner;
        }
    }

    public void updateRoute(Booking booking, List<ExactLocation> route) {
        if (!canChangeRoute(booking)) {
            throw new InvalidActionForBookingStateException("Ride has already been completed or cancelled");
        }
        booking.setRoute(route);
        bookingRepository.save(booking);
    }

    public void startRide(Booking booking, OTP otp, int rideStartOTPExpiryMinutes) {
        if (!booking.getBookingStatus().equals(BookingStatus.CAB_ARRIVED)) {
            throw new InvalidActionForBookingStateException("Cannot start the ride before the driver has reached the pickup point");
        }
        if (!booking.getRideStartOTP().validateEnteredOTP(otp))
            throw new InvalidOTPException();
        booking.setStartTime(new Date());
        booking.getPassenger().setActiveBooking(booking);
        booking.setBookingStatus(BookingStatus.IN_RIDE);
    }

    public boolean canChangeRoute(Booking booking) {
        BookingStatus bookingStatus = booking.getBookingStatus();
        return bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER)
                || bookingStatus.equals(BookingStatus.CAB_ARRIVED)
                || bookingStatus.equals(BookingStatus.IN_RIDE)
                || bookingStatus.equals(BookingStatus.REACHING_PICKUP_LOCATION);
    }

    public void endRide(Booking booking) {
        if (!booking.getBookingStatus().equals(BookingStatus.IN_RIDE)) {
            throw new InvalidActionForBookingStateException("The ride hasn't started yet");
        }
        booking.getDriver().setActiveBooking(null);
        booking.setEndTime(new Date());
        booking.getPassenger().setActiveBooking(null);
        booking.setBookingStatus(BookingStatus.COMPLETED);
    }

    public void retryBooking(Booking booking) {
        createBooking(booking);
    }

    public void cancel(Booking booking) {
        BookingStatus bookingStatus = booking.getBookingStatus();
        if (!(bookingStatus.equals(BookingStatus.REACHING_PICKUP_LOCATION)
                || bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER)
                || bookingStatus.equals(BookingStatus.CAB_ARRIVED))) {
            throw new InvalidActionForBookingStateException("Cannot cancel the booking now.");
        }
        bookingStatus = BookingStatus.CANCELLED;
        booking.setDriver(null);
        booking.setNotifiedDrivers(new HashSet<>());
    }

    private boolean canAcceptBooking(Driver driver, int maxWaitTimeForPreviousRide) {
        Booking activeBooking = driver.getActiveBooking();
        if (activeBooking == null) {
            return true;
        }
        return activeBooking.getExpectedCompletionTime().before(
                DateUtils.addMinutes(new Date(), maxWaitTimeForPreviousRide));
    }
}
