package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.OTP;
import com.covidsafe.models.Verification;
import com.covidsafe.payload.request.OTPRequest;
import com.covidsafe.payload.request.VerificationRequest;
import com.covidsafe.payload.response.VerificationResponse;
import com.covidsafe.repository.TwilioRepository;
import com.covidsafe.repository.VerificationRepository;
import com.covidsafe.security.services.TwilioService;
import com.twilio.exception.InvalidRequestException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class TwilioServiceImpl implements TwilioService {
    @Autowired
    TwilioRepository twilioRepository;

    @Autowired
    VerificationRepository verificationRepository;

    @Override
    public ResponseEntity<?> sendVerificationCode(VerificationRequest verificationRequest) {
        String deviceId = verificationRequest.getDeviceId();
        String phoneNumber = verificationRequest.getPhoneNumber();

        if (isPhoneNumberValid(phoneNumber)) {
            PhoneNumber to = new PhoneNumber(phoneNumber);
            PhoneNumber from = new PhoneNumber("+19125135625");
            String otp = generateRandomNumberString();

            MessageCreator creator = Message.creator(to, from, otp);
            creator.create();
            log.info("Send sms {}", phoneNumber);

            saveVerificationCode(deviceId, otp);
        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + phoneNumber + "] is not a valid number"
            );
        }

        return ResponseEntity.ok(null);
    }

    private String generateRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%06d", number);
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        // TODO: Implement phone number validator
        return true;
    }

    private void saveVerificationCode(String deviceId, String verificationCode) {
        OTP otp = new OTP(deviceId, verificationCode);
        twilioRepository.save(otp);
    }

    @Override
    public ResponseEntity<?> verification(OTPRequest OTPRequest) {
        String deviceId = OTPRequest.getDeviceId();
        String verificationCode = OTPRequest.getOtp();

        OTP otp = twilioRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("OTP", "Device", deviceId));

        if (verificationCode.equals(otp.getOtp())) {
            twilioRepository.delete(otp);

            String verificationToken = UUID.randomUUID().toString();
            Verification verification = new Verification(deviceId, verificationToken);
            verificationRepository.save(verification);

            VerificationResponse verificationResponse = new VerificationResponse(verificationToken, 60000);
            return ResponseEntity.ok(verificationResponse);
        }

        throw new InvalidRequestException("Invalid One Time Password!");

    }
}
