package com.covidsafe.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "vaccine_registrations")
@NoArgsConstructor
public class VaccineRegistration extends AuditMetadata {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("qr_code")
    private Binary qrCode;

    @Field("full_name")
    private String fullName;

    @Field("date_of_birth")
    private String dateOfBirth;

    private boolean gender;

    private String identification;

    @Field("health_insurance_number")
    private String healthInsuranceNumber;

    @Field("preferred_vaccination_date")
    private String preferredVaccinationDate;

    private String occupation;

    @DBRef
    private Priority priority;

    @DBRef
    private Subnational province;

    @DBRef
    private Subnational district;

    @DBRef
    private Subnational ward;

    private String address;

    @DBRef
    private Ethnic ethnic;

    @DBRef
    private Nationality nationality;

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

    private int status;

}
