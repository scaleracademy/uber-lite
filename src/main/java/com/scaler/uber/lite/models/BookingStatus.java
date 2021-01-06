package com.scaler.uber.lite.models;

import lombok.Getter;

/**
 * @author mohit@interviewbit.com on 05/01/21
 **/
@Getter
public enum BookingStatus {
    CANCELLED("The booking has been cancelled due to one of many reasons"),
    ASSIGNING_DRIVER("The passenger has requested the booking, but a driver is yet to be assigned"),
    REACHING_PICKUP_LOCATION("The driver has been assigned and is own their way to the pickup location"),
    CAB_ARRIVED("The driver has arrived at pickup location and is waiting for passenger"),
    IN_RIDE("The ride is currently in progress"),
    COMPLETED("The ride has already been completed");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }
}
