package com.covidsafe.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class HealthDeclarationRequest {
    private String id;

    @NotBlank
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank
    @JsonProperty("year_of_birth")
    private String yearOfBirth;

    @NotBlank
    private String identification;

    @NotNull
    private boolean gender;

    @NotBlank
    private String nationality;

    @NotBlank
    private String province;

    @NotBlank
    private String district;

    @NotBlank
    private String ward;

    @NotBlank
    private String address;

    @NotBlank
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    @NotNull
    private boolean visit;

    @JsonProperty("visit_detail")
    private String visitDetail;

    @NotNull
    private boolean symptoms;

    @JsonProperty("symptoms_detail")
    private String symptomsDetail;

    @NotNull
    @JsonProperty("contact_sick_people")
    private boolean contactSickPeople;

    @NotNull
    @JsonProperty("contact_epidemic_area")
    private boolean contactEpidemicArea;

    @NotNull
    @JsonProperty("contact_symptoms_people")
    private boolean contactSymptomsPeople;
}
