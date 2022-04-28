package com.covidsafe.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @JsonProperty("verification_key")
    private String verificationKey;

    @NotBlank
    @JsonProperty("device_id")
    private String deviceId;

}
