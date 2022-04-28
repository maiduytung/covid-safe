package com.covidsafe.security.services;

import com.covidsafe.payload.request.OTPRequest;
import com.covidsafe.payload.request.VerificationRequest;
import org.springframework.http.ResponseEntity;

public interface TwilioService {

    ResponseEntity<?> sendVerificationCode(VerificationRequest verificationRequest);

    ResponseEntity<?> verification(OTPRequest OTPRequest);

}
