package com.covidsafe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class VerificationResponse {

    @NotBlank
    @JsonProperty("verification_key")
    private String verificationKey;

    @JsonProperty("expires_in")
    private int expiresIn;
}
