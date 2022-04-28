package com.covidsafe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
public class HealthDeclarationResponse implements Serializable {
    private String id;

    @JsonProperty("qr_code")
    private String qrCode;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("year_of_birth")
    private String yearOfBirth;

    private String identification;

    private boolean gender;

    private String nationality;

    private String province;

    private String district;

    private String ward;

    private String address;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    private boolean visit;

    @JsonProperty("visit_detail")
    private String visitDetail;

    private boolean symptoms;

    @JsonProperty("symptoms_detail")
    private String symptomsDetail;

    @JsonProperty("contact_sick_people")
    private boolean contactSickPeople;

    @JsonProperty("contact_epidemic_area")
    private boolean contactEpidemicArea;

    @JsonProperty("contact_symptoms_people")
    private boolean contactSymptomsPeople;

    @JsonProperty("created_date")
    private Instant createdDate;
}
