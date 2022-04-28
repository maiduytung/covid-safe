package com.covidsafe.config;

import com.twilio.Twilio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TwilioConfig {

    @Autowired
    public TwilioConfig(TwilioProperty twilioProperty) {
        Twilio.init(
                twilioProperty.getAccountSid(),
                twilioProperty.getAuthToken()
        );
        log.info("Twilio initialized ... with account sid {} ", twilioProperty.getAccountSid());
    }
}
