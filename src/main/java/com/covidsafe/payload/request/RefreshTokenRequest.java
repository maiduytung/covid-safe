package com.covidsafe.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;

    @NotBlank
    @JsonProperty("device_id")
    private String deviceId;
}
