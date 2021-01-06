package com.scaler.uber.lite.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mohit@interviewbit.com on 05/01/21
 **/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passenger extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings = new ArrayList<>();
    @OneToOne
    private Booking activeBooking = null;
    @Temporal(value = TemporalType.DATE)
    private Date dob;
    private String phoneNumber;
    @OneToOne
    private ExactLocation home;
    @OneToOne
    private ExactLocation work;
    @OneToOne
    private ExactLocation lastKnownLocation;
    @OneToOne
    private Review avgRating;
}
