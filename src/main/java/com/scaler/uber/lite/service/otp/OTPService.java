package com.scaler.uber.lite.service.otp;

import com.scaler.uber.lite.models.OTP;

public interface OTPService {
    void sendPhoneNumberConfirmationOTP(OTP otp);

    void sendRideStartOTP(OTP otp);
}
