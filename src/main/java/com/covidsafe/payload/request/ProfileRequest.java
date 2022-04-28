package com.covidsafe.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProfileRequest {

    private String id;

    @NotBlank
    @JsonProperty("full_name")
    private String fullName;

    private String avatar;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy hh:mm:ss a")
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @NotNull
    private boolean gender;

    @NotNull
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

}
