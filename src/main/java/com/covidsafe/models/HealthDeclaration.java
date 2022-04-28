package com.covidsafe.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "health_declarations")
@NoArgsConstructor
public class HealthDeclaration extends AuditMetadata {

    @Id
    private String id;

    @Field("qr_code")
    private Binary qrCode;

    @Field("user_id")
    private String userId;

    @Field("full_name")
    private String fullName;

    @Field("year_of_birth")
    private String yearOfBirth;

    private String identification;

    private boolean gender;

    @DBRef
    private Nationality nationality;

    @DBRef
    private Subnational province;

    @DBRef
    private Subnational district;

    @DBRef
    private Subnational ward;

    private String address;

    @Field("phone_number")
    private String phoneNumber;

    private String email;

    private boolean visit;

    @Field("visit_detail")
    private String visitDetail;

    private boolean symptoms;

    @Field("symptoms_detail")
    private String symptomsDetail;

    @Field("contact_sick_people")
    private boolean contactSickPeople;

    @Field("contact_epidemic_area")
    private boolean contactEpidemicArea;

    @Field("contact_symptoms_people")
    private boolean contactSymptomsPeople;
}
