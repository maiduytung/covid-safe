package com.covidsafe.controllers;

import com.covidsafe.payload.request.OTPRequest;
import com.covidsafe.payload.request.VerificationRequest;
import com.covidsafe.security.services.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/otp")
public class TwilioController {

    @Autowired
    TwilioService twilioService;

    @PostMapping
    public ResponseEntity<?> getVerificationCode(@Valid @RequestBody VerificationRequest verificationRequest) {
        return twilioService.sendVerificationCode(verificationRequest);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verification(@Valid @RequestBody OTPRequest OTPRequest) {
        return twilioService.verification(OTPRequest);
    }

}
