package com.covidsafe.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties("twilio")
@PropertySource("classpath:twilio.properties")
@Data
public class TwilioProperty {

    private String accountSid;
    private String authToken;
    private String trialNumber;
}
