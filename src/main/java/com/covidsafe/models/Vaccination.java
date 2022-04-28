package com.covidsafe.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "vaccinations")
@NoArgsConstructor
public class Vaccination extends AuditMetadata {

    @Id
    private String id;

    @Field("identification")
    private String identification;

    @Field("vaccine_registration_id")
    private String vaccineRegistrationId;

    @Field("full_name")
    private String fullName;

    @Field("date_of_birth")
    private Date dateOfBirth;

    private boolean gender;

    private String address;

    @Field("vaccination_date")
    private Date vaccinationDate;

    @Field("vaccination_type")
    private String vaccinationType;

    @Field("vaccination_center")
    private String vaccinationCenter;
}
