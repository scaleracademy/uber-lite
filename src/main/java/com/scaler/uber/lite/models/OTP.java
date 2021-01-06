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
public class OTP extends BaseEntity {
    private String code;
    private String sentToNumber;

    public static OTP make(String phoneNumber) {
        return OTP.builder()
                .code("0000") // random number generator
                .sentToNumber(phoneNumber)
                .build();
    }

    public boolean validateEnteredOTP(OTP otp) {
        if (!code.equals(otp.getCode())) {
            return false;
        }
        return true;
    }
}