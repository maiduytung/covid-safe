package com.covidsafe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class VaccinationResponse implements Serializable {

    private String id;

    @JsonProperty("identification")
    private String identification;

    @JsonProperty("vaccine_registration_id")
    private String vaccineRegistrationId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    private boolean gender;

    private String address;

    @JsonProperty("vaccination_date")
    private Date vaccinationDate;

    @JsonProperty("vaccination_type")
    private String vaccinationType;

    @JsonProperty("vaccination_center")
    private String vaccinationCenter;
}
