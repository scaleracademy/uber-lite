package com.scaler.uber.lite.models;

import lombok.*;

import javax.persistence.*;

/**
 * @author mohit@interviewbit.com on 05/01/21
 **/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car extends BaseEntity {
    @ManyToOne
    private Color color; // entity

    private String plateNumber;

    private String brandAndModel;

    @Enumerated(value = EnumType.STRING)
    private CarType carType;

    @OneToOne
    private Driver driver;
}
