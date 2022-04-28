package com.covidsafe.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class VaccineRegistrationRequest {
    private String id;

    @NotBlank
    @JsonProperty("full_name")
    private String fullName;

    @NotNull
    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @NotNull
    private boolean gender;

    @NotBlank
    private String identification;

    @JsonProperty("health_insurance_number")
    private String healthInsuranceNumber;

    @JsonProperty("preferred_vaccination_date")
    private String preferredVaccinationDate;

    @NotBlank
    private String occupation;

    @NotBlank
    private String priority;

    @NotBlank
    private String province;

    @NotBlank
    private String district;

    @NotBlank
    private String ward;

    private String address;

    private String ethnic;

    private String nationality;

    private int anaphylaxis;

    private int covid19;

    @JsonProperty("other_vaccinations")
    private int otherVaccinations;

    private int cancer;

    @JsonProperty("taking_medicine")
    private int takingMedicine;

    @JsonProperty("acute_illness")
    private int acuteIllness;

    @JsonProperty("chronic_progressive_disease")
    private int chronicProgressiveDisease;

    @JsonProperty("chronic_treated_well")
    private int chronicTreatedWell;

    private int pregnant;

    @JsonProperty("more_than_65")
    private int moreThan65;

    private int coagulation;

    private int allergy;
}
