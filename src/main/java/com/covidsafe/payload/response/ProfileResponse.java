package com.covidsafe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@Data
public class ProfileResponse {

    private String id;

    @JsonProperty("user_id")
    private String userId;

    @NotBlank
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("qr_code")
    private String qrCode;

    private String avatar;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @NotNull
    private boolean gender;

    @NotBlank
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank
    private String identification;

    private String email;

    @NotBlank
    private String province;

    @NotBlank
    private String district;

    @NotBlank
    private String ward;

    private String address;

    @JsonProperty("health_insurance_number")
    private String healthInsuranceNumber;

    private String nationality;

    private String ethnic;

    private String religion;

    private String occupation;

    @JsonProperty("last_modified_date")
    private Instant lastModifiedDate;

}
