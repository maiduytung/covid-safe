package com.covidsafe.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OTPRequest {
    @NotBlank
    private String otp;

    @NotBlank
    @JsonProperty("device_id")
    private String deviceId;
}
