package com.scaler.uber.lite.models;

import com.scaler.uber.lite.utils.DateUtils;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mohit@interviewbit.com on 05/01/21
 **/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;
    private String phoneNumber;
    private Gender gender;
    private String name;

    @OneToOne
    private Review avgRating; // will be updated by a nightly cron job

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private Car car;
    private String licenseDetails;
    @Temporal(value = TemporalType.DATE)
    private Date dob;

    @Enumerated(value = EnumType.STRING)
    private DriverApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "driver")
    private List<Booking> bookings; // bookings that the driver actually drove

    @ManyToMany(mappedBy = "notifiedDrivers", cascade = CascadeType.PERSIST)
    private Set<Booking> acceptableBookings = new HashSet<>(); // bookings that the driver can currently accept

    @OneToOne
    private Booking activeBooking = null;  // driver.active_booking_id  either be null or be a foreign key

    private City activeCity;

    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private ExactLocation home;

    public boolean canAcceptBooking(int maxWaitTimeForPreviousRide) {
        if (activeBooking == null) {
            return true;
        }
        return activeBooking.getExpectedCompletionTime().before(
                DateUtils.addMinutes(new Date(), maxWaitTimeForPreviousRide));
    }
}
